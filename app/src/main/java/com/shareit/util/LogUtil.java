package com.shareit.util;

import android.util.Log;

public class LogUtil {
    public static boolean LogEnable = false;
    public static void d(String tag, String message){
        Log.d(tag, message);
    }

    public static void e(String tag, String message){
        Log.e(tag, message);
    }

    public static void v(String tag, String message){
        Log.v(tag, message);
    }
}
