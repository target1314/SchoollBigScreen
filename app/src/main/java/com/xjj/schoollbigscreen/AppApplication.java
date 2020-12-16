package com.xjj.schoollbigscreen;

import com.xjj.schoollbigscreen.config.AppContext;
import com.xjj.schoollbigscreen.utils.DeviceUtils;

import androidx.multidex.MultiDexApplication;

public class AppApplication extends MultiDexApplication {

    private static final String TAG = AppApplication.class.getSimpleName();

    private static AppApplication instance = null;

    private StringBuffer sb;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sb = new StringBuffer();
        AppContext.init(getApplicationContext(), DeviceUtils.getAppEnv(getApplicationContext()));
    }


    public static AppApplication getInstance() {
        if (instance == null) {
            instance = new AppApplication();
        }
        return instance;
    }
    public StringBuffer getStringBuffer() {
        return sb;
    }


}
