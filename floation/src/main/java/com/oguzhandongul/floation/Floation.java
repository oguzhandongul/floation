
package com.oguzhandongul.floation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.oguzhandongul.floation.utils.FontCacheHelper;
import com.oguzhandongul.floation.utils.ValidationPatterns;
import com.oguzhandongul.floation.utils.patternedtextwatcher.PatternedTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;


public class Floation<T extends Floation.Listable> extends RelativeLayout {

    private Context mContext;

    private AppCompatEditText input;
    private AppCompatTextView hint;
    private AppCompatSpinner spinner;
    private AppCompatAutoCompleteTextView mailInput;

    private AppCompatImageView ivIconLeft;
    private AppCompatImageView ivIconRight;

    private static int iconSize = 36;
    private static int iconMargin = 5;
    private static int iconPadding = 7;

    private final int STATE_UP = 1;
    private final int STATE_DOWN = 2;
    private int state = STATE_DOWN;
    private int editableType = 1;
    private int patternType = 1;
    private int valitadionType = 1;
    private int inputType;
    private int errorStyle;
    private int gravity;
    private int ellipsize;
    private int textSize;
    private int maxLines;
    private int minLines;
    private int backgroundResource;
    private int errorLabelLayout;
    private boolean isMultiLine;
    private boolean isFirstOptionHint;
    private boolean isPatternWatcherAdded;
    private boolean isRequired;
    private Pattern validationPatternString;
    private String errorString;

    @ColorInt
    private int colorTint;
    @ColorInt
    private int colorText;
    @ColorInt
    private int colorHint;
    @ColorInt
    private int colorOption;

    private String fontText;
    private String fontHint;
    private String fontOption;
    private String hintValue;

    private CharSequence[] entries;

    private MailsAutoCompleteAdapter newQueryAdapter;
    private ArrayList<String> queryResultList = new ArrayList<>();
    private ArrayList<String> domains = new ArrayList<>();

    private List<T> spinnerOptionsList = new ArrayList<>();

    private FocusChangeListener focusChangeListener;

    public Floation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, context);
    }

    public Floation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);

    }

    public Floation(Context context) {
        super(context);
        init(null, context);
    }

    private void init(AttributeSet attrs, Context mContext) {
        this.mContext = mContext;
        hint = new AppCompatTextView(mContext);


        setAttributeValues(attrs);


        switch (editableType) {
            case 1:
                //Default Edittext
                addInputView();
                addHintView();
                setInputFocusListener();
                break;
            case 2:
                //Clickable TextView
                input = new AppCompatEditText(mContext);
                addInputView();
                addHintView();
                setInputNonEditable();
                break;
            case 3:
                //Spinner Dropdown
                addInputView();
                addHintView();
                setInputNonEditable();
                addSpinnerView(true);
                break;
            case 4:
                //Spinner Dialog
                addInputView();
                addHintView();
                setInputNonEditable();
                addSpinnerView(false);
                break;
            case 5:
                //Email
                addMailInputView(mContext);
                addHintView();
                setInputFocusListener();
                break;
        }


    }

    /**
     * Sets attributes.
     *
     * @param attrs attribute set
     */
    private void setAttributeValues(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Floation);
            fontText = a.getString(R.styleable.Floation_flt_textFont);
            fontHint = a.getString(R.styleable.Floation_flt_hintFont);
            fontOption = a.getString(R.styleable.Floation_flt_optionFont);
            editableType = a.getInteger(R.styleable.Floation_flt_type, 1);
            patternType = a.getInteger(R.styleable.Floation_flt_patternType, 0);
            valitadionType = a.getInteger(R.styleable.Floation_flt_validationType, 0);

            errorString = a.getString(R.styleable.Floation_flt_errorMessage);
            hintValue = a.getString(R.styleable.Floation_android_hint);
            ellipsize = a.getInteger(R.styleable.Floation_android_ellipsize, 0);
            textSize = a.getDimensionPixelSize(R.styleable.Floation_android_textSize, 0);

            isRequired = a.getBoolean(R.styleable.Floation_flt_isRequired, false);
            inputType = a.getInteger(R.styleable.Floation_android_inputType, 0);
            errorStyle = a.getInteger(R.styleable.Floation_flt_errorStyle, 0);
            isMultiLine = inputType == TYPE_TEXT_FLAG_MULTI_LINE + TYPE_CLASS_TEXT;

            maxLines = a.getInt(R.styleable.Floation_android_maxLines, 0);
            minLines = a.getInt(R.styleable.Floation_android_minLines, 0);

            entries = a.getTextArray(R.styleable.Floation_android_entries);

            gravity = a.getInt(R.styleable.Floation_android_gravity, Gravity.LEFT);
            iconSize = a.getInt(R.styleable.Floation_flt_iconSize, iconSize);

            backgroundResource = a.getResourceId(R.styleable.Floation_android_background, -1);
            errorLabelLayout = a.getResourceId(R.styleable.Floation_flt_hintLabelResource, R.layout.textview_hint);

            colorTint = a.getColor(R.styleable.Floation_flt_drawableTint, -1);
            colorText = a.getColor(R.styleable.Floation_android_textColor, -1);
            colorHint = a.getColor(R.styleable.Floation_android_textColorHint, -1);
            colorOption = a.getColor(R.styleable.Floation_flt_textColorOption, -1);

            a.recycle();

        }
    }

    /**
     * Sets parameters and adds the input view.
     */
    private void addInputView() {
        input = new AppCompatEditText(mContext);
        //input
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, isMultiLine ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT);
        if (isMultiLine) {
            //set gravity
            input.setGravity(gravity);
        } else {
            //default
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }

        //set ellipsize
        if (ellipsize != 0) {
            switch (ellipsize) {
                case 1:
                    input.setEllipsize(TextUtils.TruncateAt.START);
                    break;
                case 2:
                    input.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    break;
                case 3:
                    input.setEllipsize(TextUtils.TruncateAt.END);
                    break;
                case 4:
                    input.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    break;
            }
        }

        //set validation
        if (valitadionType != 0) {
            switch (valitadionType) {
                case -1:
                    //filled
                    setValidationPatternString(ValidationPatterns.FILLED);
                    break;
                case 1:
                    //name
                    setValidationPatternString(ValidationPatterns.NAME);
                    break;
                case 2:
                    //mail
                    setValidationPatternString(ValidationPatterns.MAIL);
                    break;
                case 3:
                    //number only
                    setValidationPatternString(ValidationPatterns.ONLY_NUMBER);
                    break;
                case 4:
                    //text only
                    setValidationPatternString(ValidationPatterns.ONLY_LETTER);
                    break;
                case 5:
                    //uppercase
                    setValidationPatternString(ValidationPatterns.UPPERCASE);
                    break;
                case 6:
                    //lowercase
                    setValidationPatternString(ValidationPatterns.LOWERCASE);
                    break;
                case 7:
                    //iban
                    setValidationPatternString(ValidationPatterns.IBAN);
                    break;
                case 8:
                    //pass basic 6
                    setValidationPatternString(ValidationPatterns.PASSWORD_6_BASIC);
                    break;
                case 9:
                    //pass basic 8
                    setValidationPatternString(ValidationPatterns.PASSWORD_8_BASIC);
                    break;
                case 10:
                    //pass num char 6
                    setValidationPatternString(ValidationPatterns.PASSWORD_6_NUMCHAR);
                    break;
                case 11:
                    //pass num char 8
                    setValidationPatternString(ValidationPatterns.PASSWORD_8_NUMCHAR);
                    break;
                case 12:
                    //pass powerful
                    setValidationPatternString(ValidationPatterns.PASSWORD_POWERFUL);
                    break;
            }
        }


        //set TextSize
        if (textSize != 0) {
            input.setTextSize(textSize);
        } else {
            input.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        }

        //maxlines
        if (maxLines != 0) {
            input.setMaxLines(maxLines);
        }

        //minlines
        if (minLines != 0) {
            input.setMinLines(minLines);
        }

        //input type
        if (inputType != 0) {
            input.setInputType(inputType);
        }

        //input font
        if (fontText != null && !isInEditMode()) {
            input.setTypeface(FontCacheHelper.getFont(fontText, getContext()));
        }

        //input text color
        if (colorText != -1) {
            input.setTextColor(colorText);
        }

        //input drawable colors
        if (colorTint != -1) {
            setTextViewDrawableColor(colorTint);
        }
        //set ellipsize
        if (patternType != 0) {
            switch (patternType) {
                case 1:
                    //credit card
                    setPattern("#### #### #### ####");
                    input.setInputType(TYPE_CLASS_NUMBER);
                    break;
                case 2:
                    //phone
                    setPattern("0 (###) ### ## ##");
                    input.setInputType(TYPE_CLASS_NUMBER);
                    break;
                case 3:
                    //date
                    setPattern("##.##.####");
                    input.setInputType(TYPE_CLASS_NUMBER);
                    break;
            }
        }

        //params
        input.setLayoutParams(params);
        input.setPadding(convertDpiToPixel(15), isMultiLine ? convertDpiToPixel(25) : convertDpiToPixel(10), 0, isMultiLine ? convertDpiToPixel(15) : 0);

        //background
        if (backgroundResource != -1) {
            input.setBackgroundResource(backgroundResource);
        }

        this.addView(input);
    }


    /**
     * Sets parameters and adds the input view.
     */
    private void addMailInputView(Context mContext) {
        mailInput = new AppCompatAutoCompleteTextView(mContext);
        //input
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, isMultiLine ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT);
        if (isMultiLine) {
            //set gravity
            mailInput.setGravity(gravity);
        } else {
            //default
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }

        //set ellipsize
        if (ellipsize != 0) {
            switch (ellipsize) {
                case 1:
                    mailInput.setEllipsize(TextUtils.TruncateAt.START);
                    break;
                case 2:
                    mailInput.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    break;
                case 3:
                    mailInput.setEllipsize(TextUtils.TruncateAt.END);
                    break;
                case 4:
                    mailInput.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    break;
            }
        }

        if (entries != null) {
            //creates new domain list from xml attribute
            domains = new ArrayList<>();
            for (int i = 0; i < entries.length; i++) {
                domains.add(entries[i].toString());
            }
        }
        //inits & sets adapter
        newQueryAdapter = new MailsAutoCompleteAdapter(getContext(), R.layout.emails_spinner_item);
        mailInput.setAdapter(newQueryAdapter);
        mailInput.setThreshold(1);

        //adds text watcher
        mailInput.addTextChangedListener(watcher);


        setSuggestion(true);

        //set TextSize
        if (textSize != 0) {
            mailInput.setTextSize(textSize);
        } else {
            mailInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        }

        //maxlines
        if (maxLines != 0) {
            mailInput.setMaxLines(maxLines);
        }

        //minlines
        if (minLines != 0) {
            mailInput.setMinLines(minLines);
        }

        //input type
        if (inputType != 0) {
            mailInput.setInputType(inputType);
        }

        //input font
        if (fontText != null && !isInEditMode()) {
            mailInput.setTypeface(FontCacheHelper.getFont(fontText, getContext()));
        }

        //input text color
        if (colorText != -1) {
            mailInput.setTextColor(colorText);
        }

        //input drawable colors
        if (colorTint != -1) {
            setTextViewDrawableColor(colorTint);
        }

        //params
        mailInput.setLayoutParams(params);
        mailInput.setPadding(convertDpiToPixel(15), isMultiLine ? convertDpiToPixel(25) : convertDpiToPixel(10), 0, isMultiLine ? convertDpiToPixel(15) : 0);

        //background
        if (backgroundResource != -1) {
            mailInput.setBackgroundResource(backgroundResource);
        }

        this.addView(mailInput);
    }

    /**
     * Sets parameters and adds the hint view.
     */
    private void addHintView() {
        //hint
        RelativeLayout.LayoutParams paramsHint = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isMultiLine) {
            //multiline
            paramsHint.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paramsHint.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsHint.topMargin = (int) getResources().getDimension(R.dimen.pm_wide);
        } else {
            //default
            paramsHint.addRule(RelativeLayout.CENTER_VERTICAL);
        }

        //hint text size
        hint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        //hint text value
        if (!TextUtils.isEmpty(hintValue)) {
            hint.setText(hintValue);
        }

        //hint font
        if (fontHint != null && !isInEditMode()) {
            hint.setTypeface(FontCacheHelper.getFont(fontHint, getContext()));
        }

        //hint text color
        if (colorHint != -1) {
            hint.setTextColor(colorHint);
        }

        paramsHint.leftMargin = (int) getResources().getDimension(R.dimen.pm_wide);
        hint.setLayoutParams(paramsHint);
        //pivot values for animation
        hint.setPivotX(0f);//left
        hint.setPivotY(0f);//top
        this.addView(hint);
    }

    /**
     * Sets parameters and adds the spinner view.
     */
    private void addSpinnerView(boolean isDropdown) {
        spinner = new AppCompatSpinner(getContext(), isDropdown ? Spinner.MODE_DROPDOWN : Spinner.MODE_DIALOG);
//        spinner = new AppCompatSpinner(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, isMultiLine ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT);
        if (isMultiLine) {
            //set gravity
            spinner.setGravity(gravity);
        } else {
            //default
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        spinner.setLayoutParams(params);
        spinner.setPadding(convertDpiToPixel(15), isMultiLine ? convertDpiToPixel(25) : convertDpiToPixel(10), 0, isMultiLine ? convertDpiToPixel(15) : 0);
        spinner.setBackgroundResource(android.R.color.transparent);

        this.addView(spinner);

        //array adapter
        ArrayAdapter<String> adapter = new AdapterSpinner(getContext(), R.layout.spinner_item, spinnerOptionsList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        //default item select listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if first option is used as hint and first option is selected, sets input as empty string
                setText(isFirstOptionHint && i == 0 ? "" : spinnerOptionsList.get(i).getLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

//    private void setRightIcon(@ColorInt int iconTint) {
//        ivIconRight = new AppCompatImageView(getContext());
//        ivIconRight.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
//        RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(iconSize, iconSize);
//        paramsRight.setMargins(0, 0, iconMargin, 0);
//        paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
//        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        ivIconRight.setLayoutParams(paramsRight);
//        ivIconRight.setVisibility(GONE);
//        if (right != null) {
//            ivIconRight.setImageDrawable(right);
////            ivIconRight.setBackgroundResource(R.drawable.button_oval_white_trans);
//            ivIconRight.setColorFilter(iconTint, PorterDuff.Mode.SRC_ATOP);
//        }
//        frameLayout.addView(ivIconRight);
//
//    }

    public AppCompatSpinner getSpinner() {
        return spinner;
    }

    /**
     * Sets focus change listener for the input view.
     *
     * @param asHint if true, when the first option is selected. Hint will animate back.
     *               if false, all options can be selected as default
     */
    public void setFirstOptionAsHint(boolean asHint) {
        isFirstOptionHint = asHint;
    }

    /**
     * Sets spinner options and notify adapter
     *
     * @param options value for options list
     */
    public void setOptions(ArrayList<T> options) {
        spinnerOptionsList = options;
        ((ArrayAdapter) spinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Adds an option to the end of spinner options and notify adapter
     *
     * @param option value for a single option for spinner list
     */
    public void addOption(T option) {
        spinnerOptionsList.add(option);
        ((ArrayAdapter) spinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Adds an option to the end of spinner options to certain position and notify adapter
     *
     * @param option   value for a single option for spinner list
     * @param position value for position wanted for inserting single option to the list
     */
    public void addOptions(int position, T option) {
        spinnerOptionsList.add(position, option);
        ((ArrayAdapter) spinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Removes a certain option by position from the spinner and notify adapter
     *
     * @param position value for position which wanted to be removed
     */
    public void removeOption(int position) {
        spinnerOptionsList.remove(position);
        ((ArrayAdapter) spinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Removes a certain option from the spinner and notify adapter
     *
     * @param option value which wanted to be removed
     */
    public void removeOption(T option) {
        spinnerOptionsList.remove(option);
        ((ArrayAdapter) spinner.getAdapter()).notifyDataSetChanged();
    }

    public void setOptionListener(final OnOptionSelectedListener optionListener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if first option is used as hint and first option is selected, sets input as empty string
                setText(isFirstOptionHint && i == 0 ? "" : spinnerOptionsList.get(i).getLabel());
                //callback
                optionListener.onOptionSelected(spinnerOptionsList.get(i), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Sets focus change listener for the input view.
     */
    private void setInputFocusListener() {
        getInPutView().setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused && getInPutView().length() == 0) {
                    //animate up
                    animateUp();
                } else if (focused && getInPutView().length() > 0) {
                    //do not animate
                } else if (!focused && getInPutView().length() > 0) {
                    //do not animate
                } else if (!focused && getInPutView().length() == 0) {
                    //animate back
                    animateBack();
                }

                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChanged(view, focused);
                }
            }
        });
    }

    /**
     * Sets the input view non editable.
     * Use for click events on the input view
     */
    private void setInputNonEditable() {
        getInPutView().setFocusableInTouchMode(false);
        getInPutView().setEnabled(false);
        getInPutView().setClickable(true);
        View overlay = new View(getContext());
        RelativeLayout.LayoutParams paramsHint = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        overlay.setLayoutParams(paramsHint);
        overlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Floation.this.performClick();
            }
        });

        this.addView(overlay);
    }

    /**
     * Animates hint view.
     * Scale rate %70
     * Moves up 10 dp
     * Changes color of text
     * Duration 300 ms
     */
    private void animateUp() {
        if (state == STATE_DOWN) {
            hint.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));

            hint.animate()
                    .translationX(0)
                    .translationY(-convertDpiToPixel(10))
                    .scaleY(0.7f)
                    .scaleX(0.7f)
                    .setDuration(300);
            state = STATE_UP;
        }

    }

    /**
     * Animates hint view, back to normal .
     * Scale rate %100
     * Moves down 10 dp
     * Changes color of text
     * Duration 300 ms
     */
    private void animateBack() {
        if (state == STATE_UP) {
            hint.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            hint.animate()
                    .translationX(0)
                    .translationY(0)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300);
            state = STATE_DOWN;
        }

    }

    /**
     * Sets the drawable color tints for input view
     */
    private void setTextViewDrawableColor(@ColorInt int color) {

        for (Drawable drawable : getInPutView().getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /**
     * Sets text into input view
     *
     * @param text value for the text in input as String
     */
    public void setText(String text) {
        getInPutView().setText(text);

        if (TextUtils.isEmpty(text)) {
            animateBack();
        } else {
            animateUp();
        }
    }


    /**
     * Sets text into input view
     *
     * @param res value for the text in input as Resource Id
     */
    public void setText(int res) {
        getInPutView().setText(res);

        if (TextUtils.isEmpty(getInPutView().getText().toString())) {
            animateBack();
        } else {
            animateUp();
        }
    }

    public String getText() {
        return getInPutView().getText().toString();
    }

    /**
     * Returns only number characters from text inside the input view as String
     */
    public String getOnlyNumbers() {
        return getInPutView().getText().toString().replaceAll("\\D+", "");
    }

    /**
     * Returns input view as EditText
     */
    public EditText getEditText() {
        return getInPutView();
    }


    /**
     * Sets pattern and add text watcher for dynamic pattern
     *
     * @param pattern value for the text in input as Resource Id
     */
    public void setPattern(String pattern) {
        if (!isPatternWatcherAdded) {
            getInPutView().addTextChangedListener(new PatternedTextWatcher.Builder(pattern)
                    .fillExtraCharactersAutomatically(true)
                    .deleteExtraCharactersAutomatically(true)
                    .saveAllInput(false)
                    .respectPatternLength(true)
                    .debug(true)
                    .build());
            isPatternWatcherAdded = true;
        }
    }


    class AdapterSpinner<T extends Listable> extends ArrayAdapter<T> {

        List<T> mList = new ArrayList<>();

        public AdapterSpinner(Context context, int resource, List<T> items) {
            super(context, resource, items);
            this.mList = items;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(FontCacheHelper.getFont(fontText, getContext()));
            view.setTextColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            view.setVisibility(INVISIBLE);

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(FontCacheHelper.getFont(fontOption == null ? fontText : fontOption, getContext()));
            view.setTextColor(colorOption == -1 ? colorText : colorOption);
            view.setText(mList.get(position).getLabel());

            return view;
        }


    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getErrorString() {
        return errorString;
    }

    public Pattern getValidationPatternString() {
        return validationPatternString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public void setValidationPatternString(Pattern validationPatternString) {
        this.validationPatternString = validationPatternString;
    }

    public int convertDpiToPixel(int dpi) {
        float pixel = 0;
        try {
            Resources r = mContext.getResources();
            pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi,
                    r.getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) pixel;
    }

    public interface OnOptionSelectedListener<T extends Floation.Listable> {
        void onOptionSelected(T selection, int position);
    }


    /**
     * @return gets the result domain list.
     */
    private ArrayList<String> getList() {
        return queryResultList;
    }


    /**
     * ArrayAdapter which filters result of domain list
     */
    private class MailsAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList = new ArrayList<>();

        public MailsAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = getList();
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    /**
     * TextWatcher updates list of autocomplete domains on text changed every stroke
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //clears the query result before updating results
            queryResultList.clear();

            try {
                if (s.length() > 1 && getText().toString().endsWith("@") && canListDomains()) {
                    //if user press @, merges users input and mail domains
                    for (int i = 0; i < domains.size(); i++) {
                        queryResultList.add(getText().toString() + domains.get(i));
                    }

                } else if (s.length() > 1 && getText().toString().contains("@") && canListDomains()) {
                    //if user starts to write mail domain name, finds related domains and filters
                    String currentString = getText().toString();
                    String[] separated = currentString.split("@");
                    String suffix = separated[1];

                    for (int i = 0; i < domains.size(); i++) {
                        if (domains.get(i).startsWith(suffix)) {
                            queryResultList.add(separated[0] + "@" + domains.get(i));
                        }
                    }

                }

                //updates list
                newQueryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * @param arrayList The ArrayList to use for domains list.
     */
    public void setDomains(ArrayList<String> arrayList) {
        domains = new ArrayList<>(arrayList);
    }

    /**
     * @return if user enters '@' multiple times and prevents any mistaken suggestions, returns false. Otherwise returns true.
     */
    private boolean canListDomains() {
        String s = getText().toString();
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '@') {
                counter++;
            }
            if (counter >= 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param isSuggestionEnabled The boolean attribute value to 'enable/disable' suggestions for input.
     *                            default is 'enabled'
     */
    public void setSuggestion(boolean isSuggestionEnabled) {
        if (isSuggestionEnabled) {
            mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else {
            mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        }
    }


    private boolean isMultiLine(int input) {

        switch (input) {
            case TYPE_TEXT_FLAG_MULTI_LINE + TYPE_CLASS_TEXT:
                return true;
            case TYPE_TEXT_FLAG_MULTI_LINE + TYPE_CLASS_TEXT + TYPE_TEXT_FLAG_CAP_SENTENCES:
                return true;
            default:
                return false;
        }

    }


    private EditText getInPutView() {
        return input != null ? input : mailInput;
    }

    public int getErrorStyle() {
        return errorStyle;
    }


    public void setFocusChangeListener(FocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    public interface FocusChangeListener {
        void onFocusChanged(View view, boolean hasFocus);
    }

    public interface PopupHideListener {
        void onTextChanged(View view);
    }

    public interface Listable {
        String getLabel();
    }

    public void addPopupHideListener(final PopupHideListener popupHideListener) {
        getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                popupHideListener.onTextChanged(getEditText());
            }
        });
    }

    public int getErrorLabelLayout() {
        return errorLabelLayout;
    }

    public void setErrorLabelbackground(View view) {
        if (errorLabelLayout == R.layout.textview_hint) {
            view.setBackgroundResource(android.R.drawable.alert_dark_frame);
        }
    }
}

