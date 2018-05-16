package com.oguzhandongul.floation.utils.validator;

public enum ValidationStyle {

    SHAKE(0),
    SNACKBAR(1),
    TOAST_MESSAGE(2),
    DIALOG(3),
    BASIC(4),
    LEFT_ICON(5),
    RIGHT_ICON(6),
    UNDER_LABEL(7);

    private int mValue;

    ValidationStyle(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static ValidationStyle fromValue(int value) {
        switch (value) {
            case 0:
                return ValidationStyle.SHAKE;
            case 1:
                return ValidationStyle.SNACKBAR;
            case 2:
                return ValidationStyle.TOAST_MESSAGE;
            case 3:
                return ValidationStyle.DIALOG;
            case 4:
                return ValidationStyle.BASIC;
            case 5:
                return ValidationStyle.LEFT_ICON;
            case 6:
                return ValidationStyle.RIGHT_ICON;
            case 7:
                return ValidationStyle.UNDER_LABEL;
            default:
                throw new IllegalArgumentException("Unknown ValidationStyle value.");
        }
    }

}