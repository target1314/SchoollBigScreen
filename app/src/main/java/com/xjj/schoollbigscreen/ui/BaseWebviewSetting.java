package com.xjj.schoollbigscreen.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

/**
 * Created by Tony.Fan on 2018/3/26 11:54
 */
public class BaseWebviewSetting {

    /**
     * 当前窗口执行JS脚本
     *
     * @param jsScript
     */
    public static void executeJS(final Activity mContext, final WebView webView, final String jsScript) {
        try {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(jsScript);
                }
            });

        } catch (Exception e) {
        }
    }

    /**
     * 包含Client 的设置和基本设置
     *
     * @param mContext
     * @param mWebview
     */
    public void setAllWebview(final Activity mContext, final WebView mWebview) {
        setBaseWebview(mContext, mWebview);
        mWebview.setWebViewClient(new HtmlWebViewClient(mContext, mWebview));
    }

    /**
     * 基本设置
     *
     * @param mContext
     * @param mWebview
     */
    public void setBaseWebview(final Activity mContext, final WebView mWebview) {
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 database storage API 功能
        mWebview.getSettings().setDatabaseEnabled(true);
        //开启 Application Caches 功能
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setBlockNetworkImage(false);
        //设置可以打开图库
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        setImagesLoading(mWebview);
        //设置  Application Caches 缓存目录
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
    }

    //优化，对于版本小于19的，不立即加载图片
    public void setImagesLoading(WebView webLoadImg) {
        if (Build.VERSION.SDK_INT >= 19) {
            webLoadImg.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webLoadImg.getSettings().setLoadsImagesAutomatically(false);
        }
    }

    /**
     * 自定义WebViewClient 防跳转至手机浏览器打开
     * <p>
     * 基础的 webview设置，
     * <p>
     */
    class HtmlWebViewClient extends WebViewClient {
        Context ctx;
        WebView mWebview;

        public HtmlWebViewClient(Context ctx, WebView mWebview) {
            this.ctx = ctx;
            this.mWebview = mWebview;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return OverrideUrl(ctx, view, request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return OverrideUrl(ctx, view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mWebview.getSettings().getLoadsImagesAutomatically()) {
                mWebview.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.clearCache(true);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();//接受证书
        }
    }

    protected boolean OverrideUrl(Context ctx, WebView view, String url) {
        return false;
    }
}
