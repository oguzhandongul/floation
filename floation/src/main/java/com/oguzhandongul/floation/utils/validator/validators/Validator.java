package com.oguzhandongul.floation.utils.validator.validators;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;
import com.oguzhandongul.floation.utils.validator.ValidationHolder;
import com.oguzhandongul.floation.utils.validator.utility.ValidationCallback;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validator {

    protected ArrayList<ValidationHolder> mValidationHolderList;
    private boolean mHasFailed = false;

    Context mContext;
    FormValidator.ValidationErrorListener validationErrorListener;

    Validator() {
        mValidationHolderList = new ArrayList<>();
    }

    public void set(Floation editText, Pattern regex, String errMsg) {
        ValidationHolder validationHolder = new ValidationHolder(editText, regex, errMsg);
        mValidationHolderList.add(validationHolder);
    }


    protected boolean checkFields(ValidationCallback callback, Floation floation) {
        boolean result = true;
        mHasFailed = false;
        //if field is not required and left empty, return true.
        if (floation !=null && TextUtils.isEmpty(floation.getText()) && !floation.isRequired()) {
            return true;
        }

        for (ValidationHolder validationHolder : mValidationHolderList) {
            if (validationHolder.isRegexType()) {
                result = checkRegexTypeField(validationHolder, callback) && result;
            } else if (validationHolder.isRangeType()) {
                result = checkRangeTypeField(validationHolder, callback) && result;
            }
        }
        return result;
    }

    private boolean checkRegexTypeField(ValidationHolder validationHolder, ValidationCallback callback) {
        Matcher matcher = validationHolder.getPattern().matcher(validationHolder.getText());
        try {
            if (!matcher.matches()) {
                executeCallback(callback, validationHolder, matcher);
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
        return true;
    }

    private boolean checkRangeTypeField(ValidationHolder validationHolder, ValidationCallback callback) {
        boolean valid;
        try {
            valid = validationHolder.getNumericRange().isValid(validationHolder.getText());
        } catch (NumberFormatException e) {
            valid = false;
        }
        if (!valid) {
            Matcher matcher = Pattern.compile("±*").matcher(validationHolder.getText());
            executeCallback(callback, validationHolder, matcher);
            return false;
        }
        return true;
    }

    private void executeCallback(ValidationCallback callback, ValidationHolder validationHolder, Matcher matcher) {
        callback.execute(validationHolder, matcher);
        requestFocus(validationHolder);
    }

    private void requestFocus(ValidationHolder validationHolder) {
        if (!mHasFailed) {
            EditText editText = validationHolder.getEditText();
            editText.requestFocus();
            editText.setSelection(editText.getText().length());
            mHasFailed = true;
        }
    }

    public abstract boolean trigger();

    public abstract void halt();


    public void setContext(Context context) {
        mContext = context;

    }

    public void setListener(FormValidator.ValidationErrorListener validationErrorListener) {
        this.validationErrorListener = validationErrorListener;
    }

}
