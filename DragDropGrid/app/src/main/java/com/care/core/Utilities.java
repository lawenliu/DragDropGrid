package com.care.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by wliu37 on 7/30/2016.
 */
public class Utilities {

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
