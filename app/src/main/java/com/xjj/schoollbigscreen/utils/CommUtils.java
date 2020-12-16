package com.xjj.schoollbigscreen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.vondear.rxtools.RxAppTool;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * 工具类
 */
public class CommUtils {

    /**
     * 跳转到系统网络设置页面
     *
     * @param context
     * @author hwp
     * @since v0.0.1
     */
    public static void gotoWifiSetting(Activity context) {
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivityForResult(wifiSettingsIntent, 1);
    }


    /**
     * 打开外部游览器
     *
     * @param activity
     * @param url
     */
    public static void startParseUrl(Activity activity, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }

    /**
     * 静默安装
     */
    public static void installAppSilent(Context mContext, String update_localpath) {
        if (!TextUtils.isEmpty(update_localpath)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File apkFile = new File(update_localpath);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LogUtils.d("==============build  ", +Build.VERSION.SDK_INT + "   " + apkFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(mContext, RxAppTool.getAppPackageName(mContext) + ".fileprovider", apkFile);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            } else {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }

        }
    }
}