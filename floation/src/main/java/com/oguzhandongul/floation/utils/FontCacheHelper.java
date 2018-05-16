package com.oguzhandongul.floation.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oguzhandongul on 01/11/2016.
 */

public class FontCacheHelper {

    public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();


    public static Typeface getFont(String typefaceName, Context context) {
        Typeface typeface = null;
        if (typefaceCache.containsKey(typefaceName)) {
            return typefaceCache.get(typefaceName);
        } else {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + typefaceName);
            } catch (Exception e) {
                return typeface;
            }

            typefaceCache.put(typefaceName, typeface);
        }
        return typeface;
    }

    public static Typeface getFont(Typeface typeface) {
        if (typefaceCache.containsKey("" + typeface.hashCode())) {
            return typefaceCache.get("" + typeface.hashCode());
        } else {

            typefaceCache.put("" + typeface.hashCode(), typeface);
        }
        return typeface;
    }
}
