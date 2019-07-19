package com.shx.base.utils;

import android.support.multidex.BuildConfig;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shx.base.R;

/**
 * 功能介绍：吐司专用工具类
 */
public class ToastUtils {

    private static Toast toast;
    /**
     * 自定义的吐司全局
     */
    private static Toast customToast;
    private static long lastClickTime;

    public static void toastShort(String str) {
        showToast(str);
    }

    public static void toastShort2(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (toast == null) {
                toast = Toast.makeText(AppBaseUtils.getApplication(), "", Toast.LENGTH_SHORT);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(str);
            toast.show();
        }
    }

    public static void toastShort(int id) {
        CharSequence str = AppBaseUtils.getApplication().getResources().getText(id);
        if (!TextUtils.isEmpty(str)) {
            toast = Toast.makeText(AppBaseUtils.getApplication(), "", Toast.LENGTH_SHORT);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(str);
            toast.show();
        }
    }

    public static void toastDebug(String str) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(str)) {
            toast = Toast.makeText(AppBaseUtils.getApplication(), "", Toast.LENGTH_SHORT);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(str);
            toast.show();
        }
    }

    /**
     * Toast long
     *
     * @param str
     */
    public static void toastLong(String str) {
        if (!TextUtils.isEmpty(str)) {
            toast = Toast.makeText(AppBaseUtils.getApplication(), "", Toast.LENGTH_LONG);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(str);
            toast.show();
        }
    }

    public static void toastLong(int id) {
        CharSequence str = AppBaseUtils.getApplication().getResources().getText(id);
        if (!TextUtils.isEmpty(str)) {
            toast = Toast.makeText(AppBaseUtils.getApplication(), "", Toast.LENGTH_LONG);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(str);
            toast.show();
        }
    }

    public static void showToast(CharSequence content) {

        if (TextUtils.isEmpty(content)) {
            return;
        }

        if (isFastClick(1000)) {
            return;
        }

        ViewGroup toastView = (ViewGroup) LayoutInflater.from(AppBaseUtils.getApplication()).inflate(R.layout.shx_layout_toast_custom, null, false);
        TextView textView = toastView.findViewById(R.id.textContent);
        textView.setText(content);

        if (customToast == null) {
            customToast = new Toast(AppBaseUtils.getApplication());
        }
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.CENTER, 0, 0);
        customToast.setView(toastView);
        customToast.show();
    }

    public static boolean isFastClick(int duration) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < duration) {
            lastClickTime = time;
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
