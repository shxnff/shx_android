package com.shx.base.easyPay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import static com.shx.base.easyPay.EasyPayActivity.payResult;


/**
 * 功能介绍：支付结果
 */

public class PayResult implements Serializable {
    //支付成功
    public static final String RESULT_SUCCESS = "SUCCESS";
    //支付失败
    public static final String RESULT_FAIL = "FAIL";
    //支付取消
    public static final String RESULT_CANCEL = "CANCEL";
    //未知错误
    public static final String RESULT_UNKNOWN = "UNKNOWN";

    @StringDef({RESULT_SUCCESS, RESULT_FAIL, RESULT_CANCEL, RESULT_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResultCode {
    }

    public static final int DEFAULT_STATUS = -10;

    //处理后的状态
    @ResultCode
    public String result = RESULT_FAIL;
    //支付原本状态
    public int sdkStatus = DEFAULT_STATUS;
    //支付宝数据
    public String alipayResult;
    //错误消息
    public String errorMsg;
    //支付结果的渠道
    @SuppressLint("WrongConstant")
    @EasyPay.PayChannel
    public int resultChannel = -1;

    //设置支付宝结果
    public void setAlipayResult(Map<String, String> rawResult) {
        resultChannel = EasyPay.CHANNEL_ALIPAY;
        if (rawResult == null) {
            return;
        }
        /**
         * 9000	订单支付成功
         * 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
         * 4000	订单支付失败
         * 5000	重复请求
         * 6001	用户中途取消
         * 6002	网络连接出错
         * 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
         * 其它	其它支付错误
         */
        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                int code = DEFAULT_STATUS;
                try {
                    code = Integer.valueOf(rawResult.get(key));
                } catch (NumberFormatException e) {
                }
                sdkStatus = code;
                if (sdkStatus == 9000) {
                    result = PayResult.RESULT_SUCCESS;
                } else if (sdkStatus == 4000) {
                    result = PayResult.RESULT_FAIL;
                } else if (sdkStatus == 6001) {
                    result = PayResult.RESULT_CANCEL;
                } else {
                    result = PayResult.RESULT_UNKNOWN;
                }
            } else if (TextUtils.equals(key, "result")) {
                alipayResult = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                errorMsg = rawResult.get(key);
            }
        }
    }

    //设置微信结果
    public void setWxResult(BaseResp baseResp) {
        resultChannel = EasyPay.CHANNEL_WECHAT;
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            sdkStatus = baseResp.errCode;
            errorMsg = baseResp.errStr;
            if (baseResp.errCode == 0) {
                result = PayResult.RESULT_SUCCESS;
            } else if (baseResp.errCode == -2) {
                result = PayResult.RESULT_CANCEL;
            } else if (baseResp.errCode == -1) {
                result = PayResult.RESULT_FAIL;
            } else {
                result = PayResult.RESULT_UNKNOWN;
            }
        } else {
            result = PayResult.RESULT_UNKNOWN;
        }
    }

    //设置银联的结果
    public void setUpResult(Intent intent) {
        resultChannel = EasyPay.CHANNEL_UPACP;
        String str = intent.getExtras().getString("pay_result");
        payResult.sdkStatus = 0;
        if (str.equalsIgnoreCase("success")) {
            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (intent.hasExtra("result_data")) {
                String result = intent.getExtras().getString("result_data");
                Log.e("result", result);
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            payResult.result = PayResult.RESULT_SUCCESS;
        } else if (str.equalsIgnoreCase("fail")) {
            payResult.result = PayResult.RESULT_FAIL;
        } else if (str.equalsIgnoreCase("cancel")) {
            payResult.result = PayResult.RESULT_CANCEL;
        } else {
            payResult.result = PayResult.RESULT_FAIL;
        }
    }

    public Intent getResultIntent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EasyPay.PAY_RESULT, this);
        intent.putExtras(bundle);
        return intent;
    }

    public void setFail() {
        result = PayResult.RESULT_FAIL;
        sdkStatus = -1;
    }

    public boolean isSuccess() {
        return RESULT_SUCCESS.equals(result);
    }

    public boolean isFail() {
        return RESULT_FAIL.equals(result);
    }

    public boolean isCancel() {
        return RESULT_CANCEL.equals(result);
    }

    public boolean isUnkown() {
        return RESULT_UNKNOWN.equals(result);
    }
}
