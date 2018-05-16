package com.oguzhandongul.floation.utils.validator.utility;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ViewsInfo {

    private int mIndex;
    private ViewGroup mParent;
    private LinearLayout mNewContainer;
    private EditText mEditText;

    public ViewsInfo(int index, ViewGroup parent, LinearLayout newContainer, EditText editText) {
        mIndex = index;
        mParent = parent;
        mNewContainer = newContainer;
        mEditText = editText;
    }

    public void restoreViews() {
        mEditText.requestFocus();
        mNewContainer.removeView(mEditText);
        mParent.removeView(mNewContainer);
        mParent.addView(mEditText, mIndex);

        mParent.setTag(null);
    }

    public void restoreCardsViews() {
        mEditText.requestFocus();
        LinearLayout.LayoutParams etParam = (LinearLayout.LayoutParams) mEditText.getLayoutParams();
        etParam.width = 0;
        mEditText.setLayoutParams(etParam);
        mNewContainer.removeView(mEditText);
        mParent.removeView(mNewContainer);
        mParent.addView(mEditText, mIndex);
    }

    public int getIndex() {
        return mIndex;
    }

    public ViewGroup getParent() {
        return mParent;
    }

    public LinearLayout getNewContainer() {
        return mNewContainer;
    }

    public EditText getEditText() {
        return mEditText;
    }

}