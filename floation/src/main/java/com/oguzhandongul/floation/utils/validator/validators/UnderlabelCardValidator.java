package com.oguzhandongul.floation.utils.validator.validators;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.utils.FontCacheHelper;
import com.oguzhandongul.floation.utils.validator.ValidationHolder;
import com.oguzhandongul.floation.utils.validator.utility.ValidationCallback;
import com.oguzhandongul.floation.utils.validator.utility.ViewsInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class UnderlabelCardValidator extends Validator {

    private Context mContext;
    private ArrayList<ViewsInfo> mViewsInfos = new ArrayList<>();
    private int mColor;
    private boolean mHasFailed = false;
    private ValidationCallback mValidationCallback;
    private Floation editText;

    private void init() {
        mValidationCallback = new ValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder, Matcher matcher) {
                editText = validationHolder.getInput();
                if (((ViewGroup) editText.getParent()).getTag() != null && ((ViewGroup) editText.getParent()).getTag().equals("validator")) {
                    return;
                }
                TextView textView = replaceView(validationHolder);
                if (!mHasFailed) {
                    textView.setFocusable(true);
                    textView.setFocusableInTouchMode(true);
                    textView.setClickable(true);
                    textView.requestFocus();
                    mHasFailed = true;
                }

            }
        };
    }

    public void setContext(Context context) {
        mContext = context;
        init();
    }

    @Override
    public boolean trigger() {
        halt();
        mColor = ContextCompat.getColor(mContext, android.R.color.holo_red_dark);
        return checkFields(mValidationCallback, editText);
    }

    @Override
    public void halt() {
        for (ViewsInfo viewsInfo : mViewsInfos) {
            viewsInfo.restoreCardsViews();
        }
        if (mValidationHolderList.size() > 0) {
            mValidationHolderList.get(0).getEditText().requestFocus();
        }
        mViewsInfos.clear();
        mHasFailed = false;
    }

    private TextView replaceView(ValidationHolder validationHolder) {
        EditText editText = validationHolder.getEditText();
        ViewGroup parent = (ViewGroup) editText.getParent();
        int index = parent.indexOfChild(editText);
        LinearLayout newContainer = new LinearLayout(mContext);
        newContainer.setTag("validator");
        float weight = ((LinearLayout.LayoutParams) editText.getLayoutParams()).weight;
        if (weight > 0) {
            newContainer.setLayoutParams(new LinearLayout.LayoutParams(editText.getLayoutParams().width, LinearLayout.LayoutParams.WRAP_CONTENT, weight));
        } else {
            newContainer.setLayoutParams(new ViewGroup.LayoutParams(editText.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        newContainer.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(mContext);
        textView.setText(validationHolder.getErrMsg());
        textView.setTextColor(mColor);
        textView.setGravity(Gravity.RIGHT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        textView.setPadding(editText.getPaddingLeft(), 0, editText.getPaddingRight(), 0);
        textView.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        textView.setTypeface(FontCacheHelper.getFont("Regular.ttf", mContext));
        parent.removeView(editText);

        ViewGroup.LayoutParams etParam = (ViewGroup.LayoutParams) editText.getLayoutParams();
        etParam.width = LinearLayout.LayoutParams.MATCH_PARENT;
        editText.setLayoutParams(etParam);

        newContainer.addView(editText);
        newContainer.addView(textView);
        parent.addView(newContainer, index);
        mViewsInfos.add(new ViewsInfo(index, parent, newContainer, editText));
        return textView;
    }

}