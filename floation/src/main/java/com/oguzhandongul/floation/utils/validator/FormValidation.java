package com.oguzhandongul.floation.utils.validator;

import android.content.Context;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;
import com.oguzhandongul.floation.utils.validator.validators.BasicValidator;
import com.oguzhandongul.floation.utils.validator.validators.ColorationValidator;
import com.oguzhandongul.floation.utils.validator.validators.ShakeValidator;
import com.oguzhandongul.floation.utils.validator.validators.ToastValidator;
import com.oguzhandongul.floation.utils.validator.validators.UnderLabelValidator;
import com.oguzhandongul.floation.utils.validator.validators.UnderlabelCardValidator;
import com.oguzhandongul.floation.utils.validator.validators.Validator;

public class FormValidation {

    private Validator mValidator = null;

    public FormValidation(ValidationStyle style, Context context) {
        switch (style) {
            case BASIC:
                if (mValidator == null || !(mValidator instanceof BasicValidator)) {
                    mValidator = new BasicValidator();
                }
                setContext(context);
                return;
            case UNDER_LABEL:
                if (mValidator == null || !(mValidator instanceof UnderLabelValidator)) {
                    mValidator = new UnderLabelValidator();
                }
                setContext(context);
                return;
            case SHAKE:
                if (mValidator == null || !(mValidator instanceof ShakeValidator)) {
                    mValidator = new ShakeValidator();
                }
                setContext(context);
                return;
            case SNACKBAR:
                if (mValidator == null || !(mValidator instanceof ColorationValidator)) {
                    mValidator = new ColorationValidator();
                }
                setContext(context);
                return;
            case TOAST_MESSAGE:
                if (mValidator == null || !(mValidator instanceof ToastValidator)) {
                    mValidator = new ToastValidator();
                }
                setContext(context);
                return;
            case RIGHT_ICON:
                if (mValidator == null || !(mValidator instanceof UnderlabelCardValidator)) {
                    mValidator = new UnderlabelCardValidator();
                }
                setContext(context);
                return;
            default:
        }
    }

    public FormValidation(ValidationStyle style, Context context, FormValidator.ValidationErrorListener validationErrorListener) {

        switch (style) {
            case BASIC:
                if (mValidator == null || !(mValidator instanceof BasicValidator)) {
                    mValidator = new BasicValidator();
                }
                setContext(context);
                return;
            case UNDER_LABEL:
                if (mValidator == null || !(mValidator instanceof UnderLabelValidator)) {
                    mValidator = new UnderLabelValidator();
                }
                setContext(context);
                return;
            case SHAKE:
                if (mValidator == null || !(mValidator instanceof ShakeValidator)) {
                    mValidator = new ShakeValidator();
                }
                setContext(context);
                setListener(validationErrorListener);
                return;
            case SNACKBAR:
                if (mValidator == null || !(mValidator instanceof ColorationValidator)) {
                    mValidator = new ColorationValidator();
                }
                setContext(context);
                setListener(validationErrorListener);
                return;
            case TOAST_MESSAGE:
                if (mValidator == null || !(mValidator instanceof ToastValidator)) {
                    mValidator = new ToastValidator();
                }
                setContext(context);
                setListener(validationErrorListener);
                return;
            case RIGHT_ICON:
                if (mValidator == null || !(mValidator instanceof UnderlabelCardValidator)) {
                    mValidator = new UnderlabelCardValidator();
                }
                setContext(context);
                setListener(validationErrorListener);
                return;
            default:
        }

    }

    public void setContext(Context context) {
        mValidator.setContext(context);
    }

    public void setListener(FormValidator.ValidationErrorListener validationErrorListener) {
        mValidator.setListener(validationErrorListener);
    }

    //use
    public void addValidation(Floation view) {
        mValidator.set(view, view.getValidationPatternString(), view.getErrorString());
    }

    public boolean validate() {
        return mValidator.trigger();
    }

    public void clear() {
        mValidator.halt();
    }

}
