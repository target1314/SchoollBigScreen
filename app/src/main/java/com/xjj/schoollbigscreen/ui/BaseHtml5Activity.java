package com.xjj.schoollbigscreen.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.xjj.schoollbigscreen.utils.SHelper;
import com.xjj.schoollbigscreen.utils.SharedPreferencesUtils;
import com.xjj.schoollbigscreen.view.CommDialog;

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
public class BaseHtml5Activity extends BaseActivity {
    protected String mUrl;
    protected String titleStr;
    @BindView(R.id.webview_layout_html)
    WebView mWebview;
    @BindView(R.id.progressBar)
    ProgressBar prograssBar;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上
    private CommDialog netWorkDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_html5_layout);
        ButterKnife.bind(this);
        getDataByBundle();
        initUI();
        initChildView();
        addWebviewListener();
        showWebView();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initUI() {
        mWebview.addJavascriptInterface(new JSActionInterface(this), JSActionInterface.JSACTION_PRFIX);
        mWebview.setWebViewClient(new HtmlWebViewClient());
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

    /**
     * 主要是webview 的一些设置
     */
    private void addWebviewListener() {
      //  new BaseWebviewSetting().setBaseWebview(this, mWebview);
        //设置先加载网络数据再加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog,
                                          boolean userGesture, Message resultMsg) {
                return super.onCreateWindow(view, dialog, userGesture,
                        resultMsg);
            }

            /**
             * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url,
                                            String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

            /**
             * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
             */
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {

                return super.onJsConfirm(view, url, message, result);
            }

            /**
             * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
             * window.prompt('请输入您的域名地址', '618119.com');
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, final JsPromptResult result) {

                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                prograssBar.setProgress(newProgress);
                if (newProgress == 80) {
                    SHelper.gone(prograssBar);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
            }

            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        0);
                return true;
            }
        });
    }

    @Override
    public void networkChanged() {

    }

    /**
     * 自定义WebViewClient 防跳转至手机浏览器打开
     */
    class HtmlWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith(".apk")) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse(url)));
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prograssBar != null) {
                prograssBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();//接受证书
        }
    }

    private void showWebView() {
        mWebview.loadUrl(mUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissNewWorkDialog();
    }

    /**
     * 子类重写右边按钮的监听事件
     */
    protected void onChildMoreBtnClick() {
    }

    /**
     * 分享内容，可以重写，也可以通过传分享内容进来
     */
    protected void onChildShareBtnClick() {
    }

    /**
     * 如果有其他的新增的组件 进行初始化
     */
    protected void initChildView() {

    }

    //设置可以打开图库
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadCallbackAboveL != null) {         // for android 5.0+
                mUploadCallbackAboveL.onReceiveValue(null);
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
            Uri uri = data.getData();
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
        }
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
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
        netWorkDialog = new CommDialog.Builder(BaseHtml5Activity.this)
                .setMessage(getString(R.string.app_quit_tips))
                .setPositiveButton(this.getResources().getString(R.string.quit), new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        SharedPreferencesUtils.setWebUrl("");
                        dialog.dismiss();
                        getUnbundlyScreen();

                    }
                }).setNegativeButton(this.getResources().getString(R.string.no), new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
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
                .params("deviceId", RxDeviceTool.getIMEI(BaseHtml5Activity.this))
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
                                    startActivity(new Intent(BaseHtml5Activity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }
                });
    }


}
