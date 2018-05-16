package com.oguzhandongul.floation.utils.validator.validators;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;
import com.oguzhandongul.floation.R;
import com.oguzhandongul.floation.utils.validator.ValidationHolder;
import com.oguzhandongul.floation.utils.validator.utility.ValidationCallback;
import com.oguzhandongul.floation.utils.validator.utility.ViewsInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;


public class UnderLabelValidator extends Validator {

    private ArrayList<ViewsInfo> mViewsInfos = new ArrayList<>();
    private boolean mHasFailed = false;
    private ValidationCallback mValidationCallback;
    private boolean canShowMessage = true;
    private boolean canHidePopup = true;
    public Floation floation;

    private void init() {
        mValidationCallback = new ValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder, Matcher matcher) {
                floation = validationHolder.getInput();
                if (((ViewGroup) floation.getParent()).getTag() != null && ((ViewGroup) floation.getParent()).getTag().equals("validator")) {
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
                        validationErrorListener.onValidationFailed(validationHolder.getErrMsg(), floation);
                    } else {
                        floation.setFocusChangeListener(new Floation.FocusChangeListener() {
                            @Override
                            public void onFocusChanged(View view, boolean hasFocus) {
                                if (!hasFocus && canHidePopup) {
                                    hidePopup();
                                }
                            }
                        });

                        floation.addPopupHideListener(new Floation.PopupHideListener() {
                            @Override
                            public void onTextChanged(View view) {
                                hidePopup();
                            }
                        });

                        showError();
                        canHidePopup = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canHidePopup = true;
                            }
                        }, 100);

                    }
                }
            }
        };
    }


    @Override
    public boolean trigger() {
        halt();
        return checkFields(mValidationCallback, floation);
    }

    @Override
    public void halt() {
        mViewsInfos.clear();
        mHasFailed = false;
        for (ValidationHolder validationHolder : mValidationHolderList) {
            validationHolder.getEditText().setError(null);
        }
        hidePopup();
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

    private ErrorPopup mErrorPopup;

    private void hidePopup() {
        if (mErrorPopup != null) {
            if (mErrorPopup.isShowing()) {
                mErrorPopup.dismiss();
            }
            mErrorPopup = null;
        }
    }


    private void showError() {
        if (floation.getWindowToken() == null) {
            return;
        }

        if (mErrorPopup == null) {
            LayoutInflater inflater = LayoutInflater.from(floation.getContext());
            final TextView err = (TextView) inflater.inflate(floation.getErrorLabelLayout(), null);

            final float scale = floation.getResources().getDisplayMetrics().density;
            mErrorPopup =
                    new ErrorPopup(err, (int) (200 * scale + 0.5f), (int) (50 * scale + 0.5f));
            mErrorPopup.setFocusable(false);
            // The user is entering text, so the input method is needed.  We
            // don't want the popup to be displayed on top of it.
            mErrorPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        }

        TextView tv = (TextView) mErrorPopup.getContentView();
        chooseSize(mErrorPopup, floation.getErrorString(), tv);
        tv.setText(floation.getErrorString());

        mErrorPopup.showAsDropDown(floation, getErrorX(), getErrorY());

    }

    private static class ErrorPopup extends PopupWindow {
        private boolean mAbove = false;
        private final TextView mView;
        private int mPopupInlineErrorBackgroundId = 0;
        private int mPopupInlineErrorAboveBackgroundId = 0;

        ErrorPopup(TextView v, int width, int height) {
            super(v, width, height);
            mView = v;
            // Make sure the TextView has a background set as it will be used the first time it is
            // shown and positioned. Initialized with below background, which should have
            // dimensions identical to the above version for this to work (and is more likely).
//            mPopupInlineErrorBackgroundId = getResourceId(mPopupInlineErrorBackgroundId,
//                    com.android.internal.R.styleable.Theme_errorMessageBackground);
//            mView.setBackgroundResource(android.R.drawable.alert_dark_frame);
        }


        @Override
        public void update(int x, int y, int w, int h, boolean force) {
            super.update(x, y, w, h, force);

            boolean above = isAboveAnchor();
        }
    }


    private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
        int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        int ht = tv.getPaddingTop() + tv.getPaddingBottom();

        int defaultWidthInPixels = floation.getResources().getDimensionPixelSize(R.dimen.error_def_width);
        Layout l = new StaticLayout(text, tv.getPaint(), defaultWidthInPixels,
                Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        float max = 0;
        for (int i = 0; i < l.getLineCount(); i++) {
            max = Math.max(max, l.getLineWidth(i));
        }

        /*
         * Now set the popup size to be big enough for the text plus the border capped
         * to DEFAULT_MAX_POPUP_WIDTH
         */
        pop.setWidth(wid + (int) Math.ceil(max));
        pop.setHeight(ht + l.getHeight());
    }


    /**
     * Returns the X offset to make the pointy top of the error point
     * at the middle of the error icon.
     */
    private int getErrorX() {
        return 0;
    }

    /**
     * Returns the Y offset to make the pointy top of the error point
     * at the bottom of the error icon.
     */
    private int getErrorY() {

        /*
         * The "2" is the distance between the point and the top edge
         * of the background.
         */
//        final float scale = floatingLabelForm.getResources().getDisplayMetrics().density;
        return 0;//Bottom of the view
    }
}