package com.shx.base.okHttp;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.luck.picture.lib.tools.Constant;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.shx.base.bean.MessageEvent;
import com.shx.base.bean.RbMsgBean;
import com.shx.base.constant.ConstantBase;
import com.shx.base.utils.MsStringUtils;
import com.shx.base.utils.ShxActivityStack;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;

/**
 * 接口请求的观察者回调
 */
public class RbCallback extends StringCallback {

    private RbEntity entity;
    private IBaseRespondMsg mRespond;

    public RbCallback(IBaseRespondMsg respond, RbEntity entity) {
        this.mRespond = respond;
        this.entity = entity;
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        if (response != null) {
            response.getException().printStackTrace();
            if (response != null) {
                entity.error_code = response.code() * 1000 + HttpCode.CODE_HTTP_FLAG_DEFAULT;
                entity.errorMsg = response.message();
            }
            if (response.getException() instanceof IllegalArgumentException) {
                entity.standbyInt = HttpCode.ERROR_BAD_PARAS;
            } else {
                entity.standbyInt = HttpCode.ERROR_SERVER_PROBLEM;
            }
        }
        if (mRespond != null) {
            mRespond.onRequestFail(entity);
        }
    }


    @Override
    public void onSuccess(final Response<String> response) {
        //检查数据
        if (response == null || MsStringUtils.isEmpty(response.body())) {
            onRequestFailToData(HttpCode.ERROR_HTTP_DATA_BODY_NULL, HttpCode.MSG_DATA_ERROR);
            return;
        }
        //子线程中解析json
        HttpUtils.runChildThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                try {

                    if (RbBean.class.isAssignableFrom(entity.clz)) {
                        try {
                            entity.data = ((RbBean) entity.clz.newInstance()).parse(entity.clz, response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                            entity.data = new RbBean().parse(entity.clz, response.body());
                        }
                    } else {
                        entity.data = new RbBean().parse(entity.clz, response.body());
                    }

                    if (entity.data == null) {
                        RbMsgBean mRbMsgBean = null;
                        if(!TextUtils.isEmpty(response.body())){
                            Gson gson = new Gson();
                            try {
                                mRbMsgBean = gson.fromJson(response.body(), RbMsgBean.class);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }

                        String msg = null;
                        if(mRbMsgBean!=null && !TextUtils.isEmpty(mRbMsgBean.message)){
                            msg = mRbMsgBean.message;
                        }else {
                            msg = HttpCode.MSG_DATA_ERROR;
                        }
                        postRequestFail(HttpCode.ERROR_HTTP_DATA_NULL, msg);
                        return;
                    }

                    RbBean mRbBean = (RbBean) entity.data;
                    if (mRbBean == null) {
                        postRequestFail(HttpCode.ERROR_HTTP_DATA_NULL, HttpCode.MSG_DATA_ERROR);
                        return;
                    }

                    if(mRbBean.isTokenErr()){
                        EventBus.getDefault().post(new MessageEvent(ConstantBase.EventBus_Token_Err));
                        postRequestFail(HttpCode.ERROR_HTTP_DATA_NULL, "token失效");
                        return;
                    }

                    mRbBean.saveToken();

                    postRequestSuccess();

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    postRequestFail(HttpCode.ERROR_HTTP_DATA_JSON_ERROR, HttpCode.MSG_DATA_ERROR);
                }
            }
        });

    }

    /**
     * 异常
     */
    private void onRequestFailToData(int code, String msg) {
        if (mRespond != null) {
            entity.error_code = code;
            entity.errorMsg = msg;
            mRespond.onRequestFail(entity);
        }
    }

    private void postRequestFail(final int code, final String msg) {
        HttpUtils.runmainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                onRequestFailToData(code, msg);
            }
        });
    }

    private void postRequestSuccess() {
        HttpUtils.runmainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (mRespond != null) {
                    mRespond.onRequestSuccess(entity);
                }
            }
        });
    }

}
