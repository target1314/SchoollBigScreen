package com.xjj.schoollbigscreen.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.xjj.schoollbigscreen.config.AppEnvConfig;

/**
 * 设备工具类
 * <p/>
 */
public abstract class DeviceUtils {
    private final static String TAG = DeviceUtils.class.getSimpleName();


    /**
     * 获取用于显示的版本号(显示如：1.0.0)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取用于升级的版本号(内部识别号)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取应用程序的完整包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getAppPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取当前实例所在的父包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getPackageNameClass(Context context) {
        if (context == null || "".equals(context)) {
            return "";
        }
        return context.getPackageName();
    }

    /**
     * 检测该包名所对应的应用是否存在（eg.com.org）
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExists(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 检测该包名所对应类是否存在（eg.com.org.MainActivity）
     *
     * @param className
     * @return
     */
    public static boolean isClassExists(String className) {
        if (className == null || "".equals(className)) {
            return false;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    getAppPackageName(context), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(getAppPackageName(context),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static AppEnvConfig getAppEnv(Context context) {
        String app_env = getMetaData(context, "APP_ENV");
        return AppEnvConfig.typeOf(app_env);
    }
}
