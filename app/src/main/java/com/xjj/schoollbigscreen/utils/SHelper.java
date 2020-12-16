package com.xjj.schoollbigscreen.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * 简单控件/视图处理类
 */
public class SHelper {
    private static SHelper helper = null;

    public static SHelper getInstance() {
        if (helper == null) {
            helper = new SHelper();
        }
        return helper;
    }


    /**
     * @Description: 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @author Beta
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @Description: 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @author Beta
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * @Description: 控件显示设置
     * @author Beta
     */
    public static void vis(View... views) {
        for (View v : views) {
            if (null != v) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * @Description: 控件显示设置
     * @author Beta
     */
    public static void invis(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * @Description: 控件显示设置
     * @author Beta
     */
    public static void gone(View... views) {
        for (View v : views) {
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示动画
     *
     * @param views
     */
    public static void visAnim(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
            v.animate().alpha(1f).setDuration(300).setListener(null);
        }
    }

    /**
     * 隐藏动画
     *
     * @param views
     */
    public static void goneAnim(View... views) {
        for (View v : views) {
            v.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 检测view列表是否处于可见，只要有一个是可见 即可
     *
     * @param views
     * @return
     */
    public static boolean isVisInList(View... views) {
        for (View v : views) {
            if (isVis(v)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVis(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isGone(View view) {
        return view.getVisibility() == View.GONE;
    }

    public static boolean isInvis(View view) {
        return view.getVisibility() == View.INVISIBLE;
    }


    /**
     * @param @param  activity
     * @param @return 设定文件
     * @return ColorMatrixColorFilter    返回类型
     * @throws
     * @Title: setImageColor
     * @Description: 改变Image变暗效果
     * @author fanguangjun
     */
    public static ColorMatrixColorFilter setImageColorDim(Activity activity) {
        int brightness = -50; // RGB偏移量，变暗为负数
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
        return cmcf;
    }

    /**
     * @param activity
     * @return ColorMatrixColorFilter
     * @Description: 改变图片颜色变淡
     * @author Rlice
     */
    public static ColorMatrixColorFilter setImageColorAlap(Activity activity) {
        float brightness = 0.5f; // RGB偏移量，变暗为负数
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, brightness, 0});
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
        return cmcf;
    }

    /**
     * @param @param  activity
     * @param @return 设定文件
     * @return ColorMatrixColorFilter    返回类型
     * @throws
     * @Title: setImageColor
     * @Description: 改变Image变亮效果
     * @author fanguangjun
     */
    public static ColorMatrixColorFilter setImageColorBrighten(Activity activity) {
        int brightness = 0; // RGB偏移量，变亮
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
        return cmcf;
    }


    /**
     * string转成bitmap
     *
     * @param
     */
    public static Bitmap convertStringToIcon(String filename) {
        return BitmapFactory.decodeFile(filename);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (getDisplayMetrics(context).densityDpi / 160f);
    }

    public static int convertDpToPixelSize(float dp, Context context) {
        float pixels = convertDpToPixel(dp, context);
        final int res = (int) (pixels + 0.5f);
        if (res != 0) {
            return res;
        } else if (pixels == 0) {
            return 0;
        } else if (pixels > 0) {
            return 1;
        }
        return -1;
    }

    public static class ImageState {
        public float left;
        public float top;
        public float right;
        public float bottom;
    }

    /**
     * @Description: 得到屏幕高度
     * @author Beta
     */
    public static int getScreenH(Activity activity) {
        if (!activity.isFinishing()) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return 0;
        }
    }

    /**
     * @Description: 得到屏幕宽度
     * @author Beta
     */
    public static int getScrrenW(Activity activity) {
        if (!activity.isFinishing()) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return 0;
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScrrenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * @param @param  activity
     * @param @return 设定文件
     * @return float    返回类型
     * @throws
     * @Title: getDisplay
     * @Description: TODO屏幕密度
     * @author fanguangjun
     */
    public static float getDisplay(Activity activity) {
        return activity.getResources().getDisplayMetrics().density;
    }

    /**
     * 设置TextView 内容工具类，如果字符不为空则设置，如果为空 则gone
     *
     * @param tv       不能为空，否则会抛出异常
     * @param textName
     */
    public static void setTextOrGone(@NonNull TextView tv, String textName) {
        checkNotNull(tv);
        if (Preconditions.isNullOrEmpty(textName)) {
            gone(tv);
            return;
        }
        tv.setText(textName);
        vis(tv);
    }
}
