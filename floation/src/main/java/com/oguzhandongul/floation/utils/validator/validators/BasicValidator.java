package com.oguzhandongul.floation.utils.validator.validators;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;
import com.oguzhandongul.floation.utils.validator.ValidationHolder;
import com.oguzhandongul.floation.utils.validator.utility.ValidationCallback;
import com.oguzhandongul.floation.utils.validator.utility.ViewsInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;


public class BasicValidator extends Validator {

    private ArrayList<ViewsInfo> mViewsInfos = new ArrayList<>();
    private boolean mHasFailed = false;
    private ValidationCallback mValidationCallback;
    private boolean canShowMessage = true;
    private Floation editText;

    private void init() {
        mValidationCallback = new ValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder, Matcher matcher) {
                editText = validationHolder.getInput();
                if (((ViewGroup) editText.getParent()).getTag() != null && ((ViewGroup) editText.getParent()).getTag().equals("validator")) {
                    return;
                }
                if (canShowMessage) {
                    canShowMessage = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canShowMessage = true;
                        }
                    }, 1000);
                    if (validationErrorListener != null) {
                        validationErrorListener.onValidationFailed(validationHolder.getErrMsg(), editText);
                    } else {
                        validationHolder.getEditText().setError(validationHolder.getErrMsg());

                    }
                }
            }
        };
    }


    @Override
    public boolean trigger() {
        halt();
        return checkFields(mValidationCallback, editText);
    }

    @Override
    public void halt() {
        mViewsInfos.clear();
        mHasFailed = false;
        for (ValidationHolder validationHolder : mValidationHolderList) {
            validationHolder.getEditText().setError(null);
        }
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        init();
    }

    @Override
    public void setListener(FormValidator.ValidationErrorListener validationErrorListener) {
        super.setListener(validationErrorListener);
    }
}