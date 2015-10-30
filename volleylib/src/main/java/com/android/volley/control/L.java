package com.android.volley.control;

import android.util.Log;

import com.android.volley.core.VolleyConfiguration;

/**
 * Created by ruoyun on 14-7-24. 日志输出控制类 (Description)
 */
public class L {
    /**
     * 日志输出时的TAG
     */
    private static String mTag = "zyh";

    private static boolean isDebug = VolleyConfiguration.isDebug;

    public static void d(String message) {
        if (isDebug) {
            Log.d(mTag, message);
        }
    }

    public static void e(Exception e) {
        if (isDebug) {
            Log.e(mTag, e.getMessage(), e);
        }
    }

}
