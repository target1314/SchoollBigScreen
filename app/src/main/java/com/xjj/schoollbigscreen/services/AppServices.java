package com.xjj.schoollbigscreen.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.xjj.schoollbigscreen.mange.AppThreadManager;
import com.xjj.schoollbigscreen.model.DowloadVersionModel;

import java.util.Calendar;

import androidx.annotation.Nullable;

/**
 * FileName: Appservices
 * Author: Target
 * Date: 2020/10/21 4:43 PM
 */
public class AppServices extends Service {

    /**
     * 版本检测
     */
    public static final String ACTION_CHECK_VERSION = "com.xjj.schoollbigscreen.ACTION_CHECK_VERSION";


    private DowloadVersionModel dowloadVersionModel;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timerBroadcastReceiver();
        dowloadVersionModel = new DowloadVersionModel(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doAction(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * doAction
     * @param intent
     */
    private void doAction(final Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        if (action.equals(ACTION_CHECK_VERSION)) {
            dowloadVersionModel.updateVersion();
        }
    }

    /**
     * 注册时间广播
     */
    private void timerBroadcastReceiver() {
        if (timerBroadcastReceiver == null) {
            timerBroadcastReceiver = new TimerBroadcastReceiver();
            IntentFilter intentFilterTimer = new IntentFilter();
            intentFilterTimer.addAction(Intent.ACTION_TIME_TICK);
            registerReceiver(timerBroadcastReceiver, intentFilterTimer);
        }
    }


    private TimerBroadcastReceiver timerBroadcastReceiver;

    /*
     * 执行定时任务的广播接收器
     */
    public class TimerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            AppThreadManager.getInstance().start(new Runnable() {

                @Override
                public void run() {
                    // 获取时间
                    Calendar calendar = Calendar.getInstance();
                    int calendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int calendarMinute = calendar.get(Calendar.MINUTE);
                    if (2 == calendarHour && 0 == calendarMinute) {
                        dowloadVersionModel.updateVersion();
                    }
                }
            });
        }
    }

    /**
     * 注销广播监听、IM服务
     */
    private void unregisterBroadCastReceiver() {
        try {
            if (timerBroadcastReceiver != null) {
                unregisterReceiver(timerBroadcastReceiver);
                timerBroadcastReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadCastReceiver();
    }
}
