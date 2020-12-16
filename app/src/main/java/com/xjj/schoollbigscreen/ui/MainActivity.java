package com.xjj.schoollbigscreen.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxDeviceTool;
import com.xjj.schoollbigscreen.Constant;
import com.xjj.schoollbigscreen.R;
import com.xjj.schoollbigscreen.config.AppContext;
import com.xjj.schoollbigscreen.info.BaseResponeInfo;
import com.xjj.schoollbigscreen.info.ScreenInfo;
import com.xjj.schoollbigscreen.receiver.AppReceiverManager;
import com.xjj.schoollbigscreen.utils.CommUtils;
import com.xjj.schoollbigscreen.utils.JsonUtils;
import com.xjj.schoollbigscreen.utils.LogUtils;
import com.xjj.schoollbigscreen.utils.LoggerUtils;
import com.xjj.schoollbigscreen.utils.NetStateUtils;
import com.xjj.schoollbigscreen.utils.Preconditions;
import com.xjj.schoollbigscreen.utils.RxTimer;
import com.xjj.schoollbigscreen.utils.SHelper;
import com.xjj.schoollbigscreen.utils.SharedPreferencesUtils;
import com.xjj.schoollbigscreen.view.CommDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.code_tv_1)
    TextView codeTv1;
    @BindView(R.id.code_tv_2)
    TextView codeTv2;
    @BindView(R.id.code_tv_3)
    TextView codeTv3;
    @BindView(R.id.code_tv_4)
    TextView codeTv4;
    @BindView(R.id.code_tv_5)
    TextView codeTv5;
    @BindView(R.id.code_tv_6)
    TextView codeTv6;
    @BindView(R.id.refresh_code_tv)
    TextView refreshCodeTv;
    @BindView(R.id.refresh_code_btn)
    Button refreshCodeBtn;
    private CommDialog netWorkDialog = null;
    private boolean isLoadExternalBrowser = false;  //是否加载外部游览器
    private int task = 0; // 定时请求状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        doInitRequest();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 获取大屏验证码、绑定地址
     *
     * @param isLoadCde 是否加载最新code
     */
    private void getScreenCodeAndUrl(boolean isLoadCde) {
        if (isLoadCde) {
            task = 0;
        } else {
            task = 1;
        }
        LoggerUtils.fLog().i("执行返回数据isLoadCde" + isLoadCde);
        OkGo.<String>get(AppContext.appEnvConfig.getApiUrl2() + Constant.CHECKSCREEN)
                .tag(this)
                .params("deviceId", RxDeviceTool.getIMEI(MainActivity.this))
                .params("task", task)
                .execute(new StringCallback() {

                    @Override
                    public void onError(Response<String> response) {
                        LogUtils.d("执行返回数据", response.message());
                        clearCodeStr();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        LoggerUtils.fLog().i("执行返回数据" + response.body());
                        if (!Preconditions.isNullOrEmpty(response.body())) {
                            ScreenInfo screenInfo = JsonUtils.parseObject(response.body(), ScreenInfo.class);
                            if (!Preconditions.isNullOrEmpty(screenInfo.getData())) {
                                if (isLoadCde) {
                                    if (!Preconditions.isNullOrEmpty(screenInfo.getData().getCheckCode())) {
                                        SHelper.invis(refreshCodeTv);
                                        setRefreshCodeTvCode(screenInfo.getData().getCheckCode());
                                        handelCountdownTime();
                                    }
                                } else {
                                    if (!Preconditions.isNullOrEmpty(screenInfo.getData().getViewUrl())) {
                                        SharedPreferencesUtils.setWebUrl(screenInfo.getData().getViewUrl());
                                        startBaseHtml(screenInfo.getData().getViewUrl());
                                    }
                                }

                            }
                        }
                    }
                });
    }


    /**
     * 进入首页后接口调用
     */
    private void doInitRequest() {
        sendBroadcast(new Intent(AppReceiverManager.RUN_CHECK_VERSION));
        getScreenCodeAndUrl(true);
        handelSecondTimeTask();
    }


    /**
     * 处理定时3秒请求绑定地址
     */
    private void handelSecondTimeTask() {
        RxTimer.getInstance().excuteSecondsTask(3, new RxTimer.DoAction() {
            @Override
            public void action(long count) {
                LoggerUtils.fLog().i("每3秒执行一次" + RxDeviceTool.getDeviceInfo(MainActivity.this));
                if (Preconditions.isNullOrEmpty(SharedPreferencesUtils.getWebUrl())) {
                    getScreenCodeAndUrl(false);
                }
            }
        });
    }


    /**
     * 打开绑定地址
     *
     * @param viewUrl
     */
    private void startBaseHtml(String viewUrl) {
        if (Build.VERSION.SDK_INT >= 21) {
            // TODO
            startActivity(new Intent(this, BaseX5Html5Activity.class).putExtra("url", viewUrl));
            finish();
        } else {
            isLoadExternalBrowser = true;
            CommUtils.startParseUrl(this, viewUrl);
        }
    }

    /**
     * 动态设置六位验证码
     */
    private void setRefreshCodeTvCode(String codeNumber) {
        codeTv1.setText(codeNumber.substring(0, codeNumber.length() - 5));
        codeTv2.setText(codeNumber.substring(1, codeNumber.length() - 4));
        codeTv3.setText(codeNumber.substring(2, codeNumber.length() - 3));
        codeTv4.setText(codeNumber.substring(3, codeNumber.length() - 2));
        codeTv5.setText(codeNumber.substring(4, codeNumber.length() - 1));
        codeTv6.setText(codeNumber.substring(5, codeNumber.length() - 0));
    }

    /**
     * 处理定时5分钟倒计时
     */
    private void handelCountdownTime() {
        RxTimer.getInstance().excutenterVal(5, new RxTimer.DoAction() {
            @Override
            public void action(long count) {
                LoggerUtils.fLog().i("倒计时5分钟执行" + count);
                if (count == 0) {
                    SHelper.vis(refreshCodeTv);
                }
            }
        });

    }

    @OnClick(R.id.refresh_code_btn)
    public void onViewClicked() {
        getScreenCodeAndUrl(true);
    }


    @Override
    public void networkChanged() {
        NetStateUtils.startPing();
        checkNetWork();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetStateUtils.startPing();
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
                .params("deviceId", RxDeviceTool.getIMEI(MainActivity.this))
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
                                    getScreenCodeAndUrl(true);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 检测网络
     */
    private void checkNetWork() {
        NetStateUtils.setPingListener(new NetStateUtils.PingNetWorkListener() {
            @Override
            public void onPingState(boolean isConnected) {
                // isConnected is the state of wlan
                if (!isConnected) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clearCodeStr();
                            if (null == netWorkDialog || !netWorkDialog.isShowing()) {
                                showNetworkDialog();
                            }
                        }
                    });
                }

            }
        });

    }


    /**
     * 网络提示弹框
     */
    private void showNetworkDialog() {
        dismissNewWorkDialog();
        netWorkDialog = new CommDialog.Builder(MainActivity.this).setMessage(getString(R.string.app_exception_network_no))
                .setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommUtils.gotoWifiSetting(MainActivity.this);
                        dialog.dismiss();
                    }
                }).create();
        netWorkDialog.setCanceledOnTouchOutside(false);
        netWorkDialog.setCancelable(false);
        netWorkDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxTimer.getInstance().cancelTimer();
        RxTimer.getInstance().cancelMinutesTimer();
        dismissNewWorkDialog();
    }

    private void dismissNewWorkDialog() {
        if (null != netWorkDialog) {
            netWorkDialog.dismiss();
            netWorkDialog = null;
        }
    }


    /**
     * 清楚验证码
     */
    private void clearCodeStr() {
        codeTv1.setText("");
        codeTv2.setText("");
        codeTv3.setText("");
        codeTv4.setText("");
        codeTv5.setText("");
        codeTv6.setText("");
    }

    private static final int TIME_EXIT = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
            finish();
            System.exit(0);
            return;
        } else {
            Toast.makeText(this, "再点击一次返回退出程序", Toast.LENGTH_SHORT).show();
            mBackPressed = System.currentTimeMillis();
        }
    }
}
