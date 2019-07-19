package com.shx.base.easyPay.wxpay;

import android.content.Intent;

import com.shx.base.easyPay.EasyPayActivity;
import com.shx.base.easyPay.PayResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.shx.base.easyPay.EasyPayActivity.payResult;


public class WXIntentHandler implements IWXAPIEventHandler {
    EasyPayActivity activity;

    public WXIntentHandler(String appId, EasyPayActivity activity, Intent intent) {
        this.activity = activity;
        IWXAPI api = WXAPIFactory.createWXAPI(activity.getApplication(), appId);
        api.registerApp(appId);
        boolean result = api.handleIntent(intent, this);
        if (!result) {
            activity.setUnknownResult("微信返回失败");
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 方法功能：baseResp code : 0：支付成功   -1:支付失败    -2：支付取消
     */
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (payResult == null) {
                payResult = new PayResult();
            }
            payResult.setWxResult(baseResp);
            activity.setResutl();
        }
    }
}
