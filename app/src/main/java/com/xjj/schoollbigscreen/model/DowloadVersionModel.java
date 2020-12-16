package com.xjj.schoollbigscreen.model;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxAppTool;
import com.xjj.schoollbigscreen.Constant;
import com.xjj.schoollbigscreen.config.AppContext;
import com.xjj.schoollbigscreen.info.AppUpgradeInfo;
import com.xjj.schoollbigscreen.utils.CommUtils;
import com.xjj.schoollbigscreen.utils.DeviceUtils;
import com.xjj.schoollbigscreen.utils.JsonUtils;
import com.xjj.schoollbigscreen.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;

/**
 * FileName: DowloadModel
 * Author: Target
 * Date: 2020/6/17 9:43 AM
 */
public class DowloadVersionModel {

    private static NumberFormat numberFormat;
    private Context mContext;

    //版本下载文件压缩包路径
    private String DM_TARGET_VERSION_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

    //文件名称
    private String FILE_VERSION_NAME;

    public DowloadVersionModel(Context context) {
        mContext = context;
        numberFormat = NumberFormat.getPercentInstance();
        NumberFormat.getPercentInstance().setMinimumFractionDigits(2);
    }


    /**
     * 版本更新参数
     *
     * @return
     */
    private String updateVersionParams() {
        // 业务交互数据(json字符串)
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectParams = new JSONObject();
        try {
            jsonObject.put("version", DeviceUtils.getVersionName(mContext));
            jsonObject.put("versionCode", DeviceUtils.getVersionCode(mContext));
            jsonObject.put("version", DeviceUtils.getVersionName(mContext));
            jsonObject.put("clientType", "android");
            jsonObject.put("packageName", DeviceUtils.getAppPackageName(mContext));
            jsonObject.put("app", "zhiliao");
            jsonObjectParams.put("clientInfo", jsonObject);
            jsonObjectParams.put("data", jsonObject);
            return jsonObjectParams.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 更新版本
     */
    public void updateVersion() {
        LogUtils.d("==yy==IncrementModel", "参数" + updateVersionParams());
        RequestBody body = RequestBody.create(Constant.JSON, updateVersionParams());
        OkGo.<String>post(AppContext.appEnvConfig.getApiUrl() + Constant.URL_VERSION_CHECK)
                .tag(this)
                .upRequestBody(body)
                .execute(new StringCallback() {

                    @Override
                    public void onError(Response<String> response) {
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        AppUpgradeInfo appUpgradeInfo = JsonUtils.parseObject(response.body(), AppUpgradeInfo.class);
                        LogUtils.d("==yy==IncrementModel", "成功" + appUpgradeInfo.getBizData().getDownLoadUrl());
                        if (DeviceUtils.getVersionCode(mContext) < appUpgradeInfo.getBizData().getVersionCode()) {
                            dowloadFile(appUpgradeInfo.getBizData().getDownLoadUrl(), appUpgradeInfo.getBizData().getVersion());
                        }
                    }
                });

    }

    /**
     * 下载Apk
     *
     * @param url
     */
    private void dowloadFile(String url, String versionName) {
        FILE_VERSION_NAME = versionName + ".apk";
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<File>get(url)
                        .execute(new FileCallback(DM_TARGET_VERSION_FOLDER, versionName + ".apk") {
                            @Override
                            public void onSuccess(Response<File> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<File> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        LogUtils.d("==yy==DowloadModel", "正在下载中...");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        String downloadLength = Formatter.formatFileSize(mContext, progress.currentSize);
                        String totalLength = Formatter.formatFileSize(mContext, progress.totalSize);
                        LogUtils.d("==yy==DowloadModel", downloadLength + "/" + totalLength);
                        String speed = Formatter.formatFileSize(mContext, progress.speed);
                        LogUtils.d("==yy==DowloadModel", String.format("%s/s", speed));
                        LogUtils.d("==yy==DowloadModel", numberFormat.format(progress.fraction));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        LogUtils.d("==yy==DowloadModel", "下载出错");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("==yy==DowloadModel", "下载完成进行安装");
                        CommUtils.installAppSilent(mContext, DM_TARGET_VERSION_FOLDER + FILE_VERSION_NAME);
                    }
                });
    }

}
