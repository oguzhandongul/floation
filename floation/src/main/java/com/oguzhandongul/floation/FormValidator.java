package com.oguzhandongul.floation;

import android.app.Activity;

import com.oguzhandongul.floation.utils.validator.FormValidation;
import com.oguzhandongul.floation.utils.validator.ValidationStyle;

import static com.oguzhandongul.floation.utils.validator.ValidationStyle.TOAST_MESSAGE;

/**
 * Created by oguzhandongul on 21.01.2018.
 */

public class FormValidator {
    FormValidation formValidation;


    /**
     * Basic constructor
     * TOAST MESSAGE style is default, no custom listener
     *
     * @param context Activity context
     * @param inputs  FloatingLabelForm views
     */
    public FormValidator(Activity context, Floation... inputs) {
        formValidation = new FormValidation(TOAST_MESSAGE, context);
        for (Floation data :
                inputs) {
            formValidation.addValidation(data);
        }
    }


    /**
     * Constructor with validation listener
     * TOAST_MESSAGE style is default
     *
     * @param context                 Activity context
     * @param validationErrorListener callback for handling validation errors manually
     * @param inputs                  FloatingLabelForm views
     */
    public FormValidator(Activity context, ValidationErrorListener validationErrorListener, Floation... inputs) {
        formValidation = new FormValidation(TOAST_MESSAGE, context, validationErrorListener);
        for (Floation data :
                inputs) {
            formValidation.addValidation(data);
        }
    }


    /**
     * Constructor with custom style
     *
     * @param context Activity context
     * @param style   Custom style for showing error on UI
     * @param inputs  FloatingLabelForm views
     */
    public FormValidator(Activity context, ValidationStyle style, Floation... inputs) {
        formValidation = new FormValidation(style, context);
        for (Floation data :
                inputs) {
            formValidation.addValidation(data);
        }
    }


    /**
     * Constructor with custom validation listener and custom style
     *
     * @param context                 Activity context
     * @param style                   Custom style for showing error on UI
     * @param validationErrorListener callback for handling validation errors manually
     * @param inputs                  FloatingLabelForm views
     */
    public FormValidator(Activity context, ValidationStyle style, ValidationErrorListener validationErrorListener, Floation... inputs) {
        formValidation = new FormValidation(style, context, validationErrorListener);
        for (Floation data :
                inputs) {
            formValidation.addValidation(data);
        }
    }


    public boolean validate() {
        return formValidation.validate();
    }


    public interface ValidationErrorListener {
        void onValidationFailed(String errMsg, Floation view);
    }
}
