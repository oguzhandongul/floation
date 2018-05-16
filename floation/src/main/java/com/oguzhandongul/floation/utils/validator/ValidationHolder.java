package com.oguzhandongul.floation.utils.validator;

import android.widget.EditText;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.utils.validator.model.NumericRange;

import java.util.regex.Pattern;

public class ValidationHolder {

    private Floation mEditText;
    private Pattern mPattern;
    private NumericRange mNumericRange;
    private String mErrMsg;

    public ValidationHolder(Floation editText, Pattern pattern, String errMsg) {
        mEditText = editText;
        mPattern = pattern;
        mErrMsg = errMsg;
    }

    public ValidationHolder(Floation editText, NumericRange numericRange, String errMsg) {
        mEditText = editText;
        mNumericRange = numericRange;
        mErrMsg = errMsg;
    }

    public boolean isRegexType() {
        return mPattern != null;
    }

    public boolean isRangeType() {
        return mNumericRange != null;
    }

    public boolean isEditTextStyle() {
        return mEditText != null;
    }

    public Pattern getPattern() {
        return mPattern;
    }

    public NumericRange getNumericRange() {
        return mNumericRange;
    }

    public String getErrMsg() {
        return mErrMsg;
    }

    public String getText() {
        if (mEditText != null) {
            return mEditText.getText().toString();
        } else {
            return null;
        }
    }

    public EditText getEditText() {
        if (isEditTextStyle()) {
            return mEditText.getEditText();
        } else {
            return null;
        }
    }

    public Floation getInput() {
        if (isEditTextStyle()) {
            return mEditText;
        } else {
            return null;
        }
    }


}
