package com.xjj.schoollbigscreen.utils;

import android.text.TextUtils;
import android.util.Log;

import com.xjj.schoollbigscreen.AppApplication;
import com.xjj.schoollbigscreen.config.AppContext;

/**
 * 日志工具类
 * <p/>
 * 创建时间: 2014-8-19 上午11:58:53 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public class LogUtils {
    private static final boolean DEBUG = !AppContext.isRelease();
    private static StringBuffer sb;

    static {
        sb = AppApplication.getInstance().getStringBuffer();
    }


    public static void d(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message);
            writeLogFile(message);
        }
    }

    public static void d(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
            writeLogFile(message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.i(tag, message);
            writeLogFile(message);
        }
    }

    public static void i(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
            writeLogFile(message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.w(tag, message);
            writeLogFile(message);
        }
    }

    public static void w(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.w(tag, message, tr);
            writeLogFile(message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.e(tag, message);
            writeLogFile(message);
        }
    }

    public static void e(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
            writeLogFile(message);
        }
    }

    /**
     * http log method
     */
    public static void http(String className, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d("httpMessage", className + " : " + message);
        }
    }

    public static String makeLogTag(Class<?> cls) {
        return "Androidpn_" + cls.getSimpleName();
    }

    private static void writeLogFile(String logTag) {
        if (logTag.contains("android.bluetooth.device.action.FOUND")) {
            return;
        }
    }
}