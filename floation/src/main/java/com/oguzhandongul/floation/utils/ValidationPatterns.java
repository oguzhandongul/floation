package com.oguzhandongul.floation.utils;

import java.util.regex.Pattern;

/**
 * Created by oguzhandongul on 21.01.2018.
 */

public class ValidationPatterns {

    //min 2 length, only letter
    public static final Pattern FILLED = Pattern.compile("^.{1,}$");

    //min 2 length, only letter
    public static final Pattern NAME = Pattern.compile("[\\p{L}\\s]{2,}");

    //min 1 length, only letter
    public static final Pattern ONLY_LETTER = Pattern.compile("^.[a-zA-Z]{1,}$");

    //min 1 length, only number
    public static final Pattern ONLY_NUMBER = Pattern.compile("^.[0-9]{1,}$");

    //min 1 length, only lovercase letter
    public static final Pattern LOWERCASE = Pattern.compile("^.[a-z]{1,}$");

    //min 1 length, only uppercase letter
    public static final Pattern UPPERCASE = Pattern.compile("^.[A-Z]{1,}$");

    //
    public static final Pattern MAIL = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$");

    //
    public static final Pattern PHONE_TYPE_1 = Pattern.compile("^\\+[0-9]{3,}$");

    //
    public static final Pattern PHONE_TYPE_2 = Pattern.compile("");

    //min 6 length
    public static final Pattern PASSWORD_6_BASIC = Pattern.compile("^.{6,}$");

    //min 8 length
    public static final Pattern PASSWORD_8_BASIC = Pattern.compile("^.{8,}$");

    //min 6 length
    public static final Pattern PASSWORD_6_NUMCHAR = Pattern.compile("^.[a-zA-Z0-9]{6,}$");

    //min 8 length
    public static final Pattern PASSWORD_8_NUMCHAR = Pattern.compile("^.[a-zA-Z0-9]{8,}$");

    //min 8 length
    //At least one Uppercase letter
    //At least one number
    public static final Pattern PASSWORD_POWERFUL = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\S]{8,}$");

    //iban
    public static final Pattern IBAN = Pattern.compile("^.{26,40}$");
}
