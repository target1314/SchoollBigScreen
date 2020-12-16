package com.xjj.schoollbigscreen.config;

import android.content.Context;

public class AppContext {
    public static Context instance;
    public static AppEnvConfig appEnvConfig;

    public static void init(Context context, AppEnvConfig envConfig) {
        if (null == instance) {
            instance = context;
            appEnvConfig = envConfig;
        }
    }

    public static boolean isRelease() {
        if (AppEnvConfig.RELEASE.getIndex() == appEnvConfig.getIndex()) {
            return true;
        }
        return false;
    }


    public static Context getContext() {
        return instance;
    }

    public static AppEnvConfig getAppEnv() {
        return appEnvConfig;
    }


}
