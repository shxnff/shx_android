package com.shx.base.easyPay;

import android.content.Intent;
import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EasyPay {
    /**
     * 每次start不能重复请求，间隔默认1秒，可以更改
     */
    public static int DELAY_START = 1000;
    public static long CURRENT_TIME = 0;
    /**
     * 请求code
     */
    public static final int REQUEST_CODE_PAYMENT = 1010;
    /**
     * 请求的数据intent  flag
     */
    public static final String INTENT_FLAG = "PAYDATA";
    /**
     * 返回给调用者的数据
     */
    public static final String PAY_RESULT = "PAY_RESULT";

    /**
     * 银联支付渠道
     */
    public static final int CHANNEL_UPACP = 1;
    /**
     * 微信支付渠道
     */
    public static final int CHANNEL_WECHAT = 2;

    /**
     * 支付宝支付渠道
     */
    public static final int CHANNEL_ALIPAY = 3;

    @IntDef({CHANNEL_UPACP, CHANNEL_WECHAT, CHANNEL_ALIPAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayChannel {
    }


    public static void startPay(Activity activity, PayEntity payEntity) {
        if ((System.currentTimeMillis() - CURRENT_TIME) > DELAY_START) {
            Intent intent = new Intent(activity, EasyPayActivity.class);
            intent.putExtra(INTENT_FLAG, payEntity);
            activity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
        CURRENT_TIME = System.currentTimeMillis();
    }

    public static void startPay(Fragment fragment, PayEntity payEntity) {
        if ((System.currentTimeMillis() - CURRENT_TIME) > DELAY_START) {
            Intent intent = new Intent(fragment.getActivity(), EasyPayActivity.class);
            intent.putExtra(INTENT_FLAG, payEntity);
            fragment.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
        CURRENT_TIME = System.currentTimeMillis();
    }

    public static PayResult getResult(Intent intent) {
        EasyPayActivity.payResult = null;
        EasyPayActivity.payEntity = null;
        if (intent == null || intent.getExtras() == null) {
            return null;
        }
        return (PayResult) intent.getExtras().getSerializable(EasyPay.PAY_RESULT);
    }
}
