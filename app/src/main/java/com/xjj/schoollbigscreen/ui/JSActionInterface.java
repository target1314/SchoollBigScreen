package com.xjj.schoollbigscreen.ui;

import android.app.Activity;

/**
 * Created by Tony.Fan on 2018/3/26 13:08
 * <p>
 * 提供给webview中页面js调用的接口
 */
public class JSActionInterface {

    public static String JSACTION_PRFIX = "XJJ";

    private Activity context;

    public JSActionInterface(Activity context) {
        this.context = context;
    }

}
