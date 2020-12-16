package com.xjj.schoollbigscreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xjj.schoollbigscreen.services.AppServices;
import com.xjj.schoollbigscreen.ui.LaunchActivity;
import com.xjj.schoollbigscreen.utils.LoggerUtils;
import com.xjj.schoollbigscreen.utils.RxTimer;

/**
 * 广播监听中心
 */
public class AppReceiverManager extends BroadcastReceiver {

    /**
     * 网络连接
     */
    public static final String NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * 开机重启
     */
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    /**
     * 检测版本更新
     */
    public static final String RUN_CHECK_VERSION = "com.xjj.schoollbigscreen.run_check_version";


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (ACTION_BOOT.equals(action)) {
                RxTimer.getInstance().excuteSecondTask(3, new RxTimer.DoAction() {
                    @Override
                    public void action(long count) {
                        Intent startMainProess = new Intent();
                        startMainProess.setClass(context, LaunchActivity.class);
                        startMainProess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startMainProess.setAction("get_contacts");
                        context.startActivity(startMainProess);
                    }
                });
            } else if (action.equals(RUN_CHECK_VERSION)) {
                Intent netIntent = new Intent(context, AppServices.class);
                netIntent.setAction(AppServices.ACTION_CHECK_VERSION);
                context.startService(netIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}