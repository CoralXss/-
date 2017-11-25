package com.coral.mobile.live.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xss on 2017/6/1.
 */

public class ViewUtils {

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return  wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 获取颜色
     * @param context
     * @param colorResId
     * @return
     */
    public static int getColor(Context context, int colorResId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return context.getResources().getColor(colorResId, null);
        }
        return context.getResources().getColor(colorResId);
    }

    /**
     * view设置background drawable
     *
     * @param view
     * @param radius  圆角半径
     * @param strokeWidth  边框宽度
     * @param strokeColor 边框颜色
     * @Param fillColor 填充颜色
     */
    @SuppressWarnings("deprecation")
    public static void setBackgroundDrawable(View view, int radius, int strokeWidth, String strokeColor, String fillColor) {
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor(fillColor));
        gd.setCornerRadius(radius);
        gd.setStroke(strokeWidth, Color.parseColor(strokeColor));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(gd);
        } else {
            view.setBackground(gd);
        }
    }

    /**
     * 动态设置边框
     * @param radius  圆角半径
     * @param strokeWidth  边框宽度
     * @param strokeColor 边框颜色
     * @Param fillColor 填充颜色
     * @return
     */
    public static Drawable getBgDrawable(int radius, int strokeWidth, String strokeColor, String fillColor) {
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor(fillColor));
        gd.setCornerRadius(radius);
        gd.setStroke(strokeWidth, Color.parseColor(strokeColor));
        return gd;
    }

}
