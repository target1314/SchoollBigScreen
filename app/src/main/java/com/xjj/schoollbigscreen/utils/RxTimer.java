package com.xjj.schoollbigscreen.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxTimer {


    private RxTimer() {

    }

    private static class Holder {
        private static final RxTimer instance = new RxTimer();

    }

    public static RxTimer getInstance() {
        return Holder.instance;
    }

    private Disposable disposable;

    private Disposable minutsDisposable;

    /**
     * delay时间后执行特定任务
     */
    public void excuteTask(long delay, final DoAction doAction) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    /**
     * delay时间后执行特定任务
     */
    public void excuteSecondTask(long delay, final DoAction doAction) {
        Observable.timer(delay, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    /*****每,隔多少时间去执行任务*****/
    @SuppressLint("CheckResult")
    public void excuteMinutesTask(long delay, final DoAction doAction) {
        cancelTimer();
        disposable = Flowable.interval(delay, TimeUnit.MINUTES).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.e("------>", (aLong++) + "");
                        if (doAction != null) {
                            doAction.action(aLong);
                        }
                    }
                });
    }

    /*****每,隔多少时间去执行任务*****/
    @SuppressLint("CheckResult")
    public void excuteSecondsTask(long delay, final DoAction doAction) {
        cancelTimer();
        disposable = Flowable.interval(delay, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.e("------>", (aLong++) + "");
                        if (doAction != null) {
                            doAction.action(aLong);
                        }
                    }
                });
    }


    /*****定时多少时间去执行任务*****/

    public void interVal(long delay, final DoAction doAction) {
        Observable.interval(delay, TimeUnit.MINUTES)
                //指定观察者在Android主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                //指定被观察者在子线程执行
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*****倒计时*****/
    public void excutenterVal(long delay, final DoAction doAction) {
        cancelMinutesTimer();
        Observable.interval(0, 1, TimeUnit.MINUTES)
                .take(delay + 1)
                //指定观察者在Android主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                //指定被观察者在子线程执行
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        minutsDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (doAction != null) {
                            doAction.action(delay - aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /***在Activity销毁时一定要调用cancelTimer否则会造成内存泄露**/
    public void cancelTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /***在Activity销毁时一定要调用cancelTimer否则会造成内存泄露**/
    public void cancelMinutesTimer() {
        if (minutsDisposable != null && !minutsDisposable.isDisposed()) {
            minutsDisposable.dispose();
        }
    }


    public interface DoAction {
        void action(long count);
    }

}
