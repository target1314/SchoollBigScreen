package com.xjj.schoollbigscreen.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xjj.schoollbigscreen.listener.INetworkChange;

/**
 * FileName: BaseActivity
 * Author: Target
 * Date: 2020/6/3 3:25 PM
 * 基类
 */
public abstract class BaseActivity extends Activity implements INetworkChange {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hardwareAccelerate();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        overridePendingTransition(0, 0);
        netChangeReceive();
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    protected void hardwareAccelerate() {
        // 4.0以上支持硬件加速
        if (Build.VERSION.SDK_INT >= 14) {
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
    }


    private void netChangeReceive() {
        IntentFilter netFilter = new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netReceive, netFilter);
    }

    private BroadcastReceiver netReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkChanged();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netReceive != null) {
            unregisterReceiver(netReceive);
        }
    }

}
