package com.shx.base.easyPay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.shx.base.easyPay.wxpay.WXIntentHandler;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.Map;


/**
 * 功能介绍：微信支付返回的Activity
 * <activity-alias
 * android:name=".wxapi.WXPayEntryActivity"
 * android:exported="true"
 * android:targetActivity="com.ashlikun.easypay.EasyPayActivity"></activity-alias>
 */

public class EasyPayActivity extends Activity {
    private static final int SDK_PAY_FLAG = 2;
    /**
     * 支付的实体
     */
    public static PayEntity payEntity = null;
    /**
     * 支付结果
     */
    public static PayResult payResult = null;
    /**
     * 一个标记当前activity和微信支付是否是同一个，还是2个存在
     */
    public boolean activityIsNes = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent(getIntent());
    }

    public void parseIntent(Intent intent) {
        payResult = new PayResult();
        if (intent.hasExtra(EasyPay.INTENT_FLAG)) {
            //主动吊起的支付
            payEntity = intent.getParcelableExtra(EasyPay.INTENT_FLAG);
            activityIsNes = true;
            start();
        } else {
            //微信返回的支付结果
            new WXIntentHandler(payEntity.appId, this, intent);
        }
    }

    /**
     * 当获取到焦点的时候，判断是否是微信支付后返回来的
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (payEntity != null) {
            if (payEntity.channel == EasyPay.CHANNEL_WECHAT && payResult.resultChannel == EasyPay.CHANNEL_WECHAT) {
                setResutl();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * 设置返回结果
     */
    public void setResutl() {
        if (activityIsNes) {
            if (payResult == null) {
                payResult = new PayResult();
            }
            setResult(RESULT_OK, payResult.getResultIntent());
            finish();
            payResult = null;
            payEntity = null;
        } else {
            //微信的
            finish();
        }
    }

    /**
     * 设置未定义错误
     *
     * @param msg
     */
    public void setUnknownResult(String msg) {
        if (activityIsNes) {
            if (payResult != null) {
                payResult = new PayResult();
            }
            payResult.result = PayResult.RESULT_UNKNOWN;
            payResult.errorMsg = msg;
            setResult(RESULT_OK, payResult.getResultIntent());
            finish();
            payResult = null;
            payEntity = null;
        } else {
            //微信的
            finish();
        }
    }

    /**
     * 开始发起支付
     */
    private void start() {
        if (payEntity.channel == EasyPay.CHANNEL_ALIPAY) {
            alipay();
        } else if (payEntity.channel == EasyPay.CHANNEL_WECHAT) {
            wxpay();
        } else if (payEntity.channel == EasyPay.CHANNEL_UPACP) {
//            upPay();
        } else {
            setUnknownResult("不支持的支付渠道");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //银联支付的回掉
        if (data == null || data.getExtras() == null) {
            payResult.setFail();
            setResutl();
            return;
        }
        payResult.setUpResult(data);
        setResutl();
    }

    /**
     * 微信支付
     */
    public void wxpay() {
        try {
            IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplication(), payEntity.appId);
            msgApi.registerApp(payEntity.appId);
            PayReq req = new PayReq();
            req.appId = payEntity.appId;
            req.partnerId = payEntity.partnerId;
            req.prepayId = payEntity.prepayId;
            req.nonceStr = payEntity.nonceStr;
            req.timeStamp = payEntity.timeStamp;
            req.packageValue = TextUtils.isEmpty(payEntity.packageValue) ? "Sign=WXPay" : payEntity.packageValue;
            req.sign = payEntity.sign;
            req.extData = payEntity.extData;
            if (!req.checkArgs() || !msgApi.sendReq(req)) {
                //失败
                payResult.setFail();
                setResutl();
            }
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            setUnknownResult(payEntity.channel + "不支持该渠道: 。缺少微信的 SDK。");
        }
    }

    /**
     * 支付宝支付
     */
    private void alipay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(EasyPayActivity.this);
                    // 调用支付接口，获取支付结果
                    Map<String, String> result = alipay.payV2(payEntity.orderInfo, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                    setUnknownResult(payEntity.channel + "不支持该渠道: 。缺少支付宝的 SDK。");
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //支付宝
                case SDK_PAY_FLAG: {
                    if (payResult == null) {
                        payResult = new PayResult();
                    }
                    payResult.setAlipayResult((Map<String, String>) msg.obj);
                    setResutl();
                    break;
                }
                default:
                    break;
            }
        }
    };
}