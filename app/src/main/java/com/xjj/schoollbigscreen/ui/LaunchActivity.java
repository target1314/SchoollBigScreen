package com.xjj.schoollbigscreen.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxDeviceTool;
import com.xjj.schoollbigscreen.Constant;
import com.xjj.schoollbigscreen.R;
import com.xjj.schoollbigscreen.config.AppContext;
import com.xjj.schoollbigscreen.info.BaseResponeInfo;
import com.xjj.schoollbigscreen.utils.CommUtils;
import com.xjj.schoollbigscreen.utils.JsonUtils;
import com.xjj.schoollbigscreen.utils.LogUtils;
import com.xjj.schoollbigscreen.utils.LoggerUtils;
import com.xjj.schoollbigscreen.utils.NetStateUtils;
import com.xjj.schoollbigscreen.utils.Preconditions;
import com.xjj.schoollbigscreen.utils.SharedPreferencesUtils;
import com.xjj.schoollbigscreen.view.CommDialog;

import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LaunchActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private final int RC_PERMISSION = 10;

    private boolean isLoadExternalBrowser = false;  //是否加载外部游览器

    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        checkPermissions();
    }


    /**
     * 检测权限
     */
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            checkNetWork();
        } else {
            EasyPermissions.requestPermissions(this, "打开权限", RC_PERMISSION, permissions);
        }
    }


    /**
     * 延迟跳转
     */
    private void handelTimeDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Preconditions.isNullOrEmpty(SharedPreferencesUtils.getWebUrl())) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        startBaseHtml(SharedPreferencesUtils.getWebUrl());
                        finish();
                    } else {
                        CommUtils.startParseUrl(LaunchActivity.this, SharedPreferencesUtils.getWebUrl());
                        isLoadExternalBrowser = true;
                    }
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, 3000);
    }

    /**
     * 打开绑定地址
     *
     * @param viewUrl
     */
    private void startBaseHtml(String viewUrl) {
        startActivity(new Intent(this, BaseX5Html5Activity.class).putExtra("url", viewUrl));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 检测网络
     */
    private void checkNetWork() {
        NetStateUtils.setPingListener(new NetStateUtils.PingNetWorkListener() {
            @Override
            public void onPingState(boolean isConnected) {
                // isConnected is the state of wlan
                if (isConnected) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handelTimeDelayed();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null == netWorkDialog || !netWorkDialog.isShowing()) {
                                showNetworkDialog();
                            }
                        }
                    });

                }
            }
        });

    }

    private CommDialog netWorkDialog = null;

    private void showNetworkDialog() {
        netWorkDialog = new CommDialog.Builder(LaunchActivity.this).setMessage(getString(R.string.app_exception_network_no))
                .setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommUtils.gotoWifiSetting(LaunchActivity.this);
                        dialog.dismiss();
                    }
                }).create();
        netWorkDialog.setCanceledOnTouchOutside(false);
        netWorkDialog.setCancelable(false);
        netWorkDialog.show();
    }


    private void dismissNewWorkDialog() {
        if (null != netWorkDialog) {
            netWorkDialog.dismiss();
            netWorkDialog = null;
        }
    }

    @Override
    public void networkChanged() {
        NetStateUtils.startPing();
        checkNetWork();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoadExternalBrowser) {
            getUnbundlyScreen();
        }
    }

    /**
     * 解决大屏绑定设备
     */
    public void getUnbundlyScreen() {
        OkGo.<String>get(AppContext.appEnvConfig.getApiUrl2() + Constant.UNBUNDLYSCREEN)
                .tag(this)
                .params("deviceId", RxDeviceTool.getIMEI(LaunchActivity.this))
                .execute(new StringCallback() {

                    @Override
                    public void onError(Response<String> response) {
                        LogUtils.d("解绑返回数据错误", response.message());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        LoggerUtils.fLog().i("解绑返回数据" + response.body());
                        if (!Preconditions.isNullOrEmpty(response.body())) {
                            BaseResponeInfo baseResponeInfo = JsonUtils.parseObject(response.body(), BaseResponeInfo.class);
                            if (!Preconditions.isNullOrEmpty(baseResponeInfo)) {
                                if (baseResponeInfo.isSuccess()) {
                                    SharedPreferencesUtils.setWebUrl("");
                                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        dismissNewWorkDialog();
        super.onDestroy();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case RC_PERMISSION:
                if (perms.contains(Manifest.permission.READ_PHONE_STATE) &&
                        perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    handelTimeDelayed();
                }
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case RC_PERMISSION:
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    new AppSettingsDialog.Builder(this).setTitle(getString(R.string.permission_warning))
                            .setRationale(getString(R.string.permission_info)).setRequestCode(RC_PERMISSION).build().show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            handelTimeDelayed();
        }
    }
}
