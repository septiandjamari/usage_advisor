package com.djamari.usageadvisor.app;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

class TypeFaceUtil {
    static void setDefaultFount(Context context, String staticTypeFieldName) {
       final Typeface regular = Typeface.createFromAsset(context.getAssets(), "font/quicksand.ttf");
       replaceFont(staticTypeFieldName, regular);
    }
    private static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
