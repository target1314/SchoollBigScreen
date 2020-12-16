package com.xjj.schoollbigscreen.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxDeviceTool;
import com.xjj.schoollbigscreen.Constant;
import com.xjj.schoollbigscreen.R;
import com.xjj.schoollbigscreen.config.AppContext;
import com.xjj.schoollbigscreen.info.BaseResponeInfo;
import com.xjj.schoollbigscreen.utils.JsonUtils;
import com.xjj.schoollbigscreen.utils.LogUtils;
import com.xjj.schoollbigscreen.utils.LoggerUtils;
import com.xjj.schoollbigscreen.utils.Preconditions;
import com.xjj.schoollbigscreen.utils.SharedPreferencesUtils;
import com.xjj.schoollbigscreen.view.CommDialog;
import com.xjj.schoollbigscreen.view.X5WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tony.Fan on 2018/3/26 11:38
 * * <p/>
 * 功能 :一个标准 的webview展示页面 ,可以支持 扩展
 * <p/>
 * 用法 :
 * 可以传入的参数
 * <p>"title" (String)  标题</p>
 * <p>"url"   (String)       url地址</p>
 * <p/>
 * 可以被继承
 * <p/>
 * 若继承之类需要重写 以下方法
 * <p>onChildMoreBtnClick()</p>
 * <p>chageMoreBtnBackground()</p>
 * <p>initChildView()</p>
 * <p>getRightBtnEnable()</p>
 */
@SuppressLint("JavascriptInterface")
public class BaseX5Html5Activity extends BaseActivity {
    protected String mUrl;
    protected String titleStr;
    @BindView(R.id.progressBar)
    ProgressBar prograssBar;
    X5WebView x5WebView;
    @BindView(R.id.webView_layout)
    ViewGroup webViewLayout;
    private CommDialog netWorkDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_x5_layout);
        ButterKnife.bind(this);
        getDataByBundle();
        init();
    }

    /**
     * 获取getIntent附带数据
     */
    private void getDataByBundle() {
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            mUrl = intent.getStringExtra("url");
        }
    }

    private void init() {
        x5WebView= new X5WebView(this, null);
        webViewLayout.addView(x5WebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        x5WebView.loadUrl(mUrl);
        x5WebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissNewWorkDialog();
    }

    private void dismissNewWorkDialog() {
        if (null != netWorkDialog) {
            netWorkDialog.dismiss();
            netWorkDialog = null;
        }
    }

    /**
     * 退出弹框提示
     */
    private void showQuitDialog() {
        netWorkDialog = new CommDialog.Builder(BaseX5Html5Activity.this)
                .setMessage(getString(R.string.app_quit_tips))
                .setPositiveButton(this.getResources().getString(R.string.quit), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        SharedPreferencesUtils.setWebUrl("");
                        dismissNewWorkDialog();
                        getUnbundlyScreen();

                    }
                }).setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dismissNewWorkDialog();
                    }
                }).create();
        netWorkDialog.setCanceledOnTouchOutside(false);
        netWorkDialog.setCancelable(false);
        netWorkDialog.show();
    }

    /**
     * 解决大屏绑定设备
     */
    public void getUnbundlyScreen() {
        OkGo.<String>get(AppContext.appEnvConfig.getApiUrl2() + Constant.UNBUNDLYSCREEN)
                .tag(this)
                .params("deviceId", RxDeviceTool.getIMEI(BaseX5Html5Activity.this))
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
                                    startActivity(new Intent(BaseX5Html5Activity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }
                });
    }


    @Override
    public void networkChanged() {

    }
}
