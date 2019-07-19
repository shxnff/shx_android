package com.shx.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    private static long lastClickTime;

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int dp2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //快速点击
    public static boolean isFastClick(int duration) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < duration) {
            lastClickTime = time;
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 以宽为准，等比缩放高
     *
     * @param view      需要缩放的view
     * @param oldWidth  原始宽
     * @param oldHeight 原始高
     * @param newWidth  要缩放的宽
     */
    public static void scaleViewByWidth(View view, int oldWidth, int oldHeight, int newWidth) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = newWidth;
        if (oldWidth > 0) {
            int newHeight = newWidth * oldHeight / oldWidth;
            params.height = newHeight;
        } else {
            params.height = 0;
        }

        view.setLayoutParams(params);
    }

    public static void setWidth(View view, int newWidth) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = newWidth;
        view.setLayoutParams(params);
    }

    public static void setHeight(View view, int newHeight) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.height = newHeight;
        view.setLayoutParams(params);
    }

    public static void setView(View view, int newWidth, int newHeight) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = newWidth;
        params.height = newHeight;
        view.setLayoutParams(params);
    }


}
