package com.shareit.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static boolean ToastEnable = false;
    public static void toast(Context context, String msg ){
        if(ToastEnable) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
