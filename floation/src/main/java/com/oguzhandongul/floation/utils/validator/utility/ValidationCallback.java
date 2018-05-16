package com.oguzhandongul.floation.utils.validator.utility;


import com.oguzhandongul.floation.utils.validator.ValidationHolder;

import java.util.regex.Matcher;

public interface ValidationCallback {

    void execute(ValidationHolder validationHolder, Matcher matcher);

}