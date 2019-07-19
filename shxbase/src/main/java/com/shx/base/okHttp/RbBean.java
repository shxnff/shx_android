package com.shx.base.okHttp;


import android.text.TextUtils;

import com.ashlikun.mulittypegson.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shx.base.constant.Sp;
import com.shx.base.utils.AppBaseUtils;
import com.shx.base.utils.SPUtils;
import com.shx.base.utils.ToastUtils;

/**
 * 响应实体基类
 */
public class RbBean implements IRespondParse {

    private int code;
    private String message;
    private String additionalData;

    public void saveToken(){
        if(!TextUtils.isEmpty(additionalData)){
            SPUtils.putString(AppBaseUtils.getApplication(), Sp.User_token, additionalData);
        }
    }

    public boolean isSuccess(){
        if(code==200){
            return true;
        }else {
            return false;
        }
    }

    public boolean isTokenErr(){
        if(code==1111){
            return true;
        }else {
            return false;
        }
    }

    public void showMessasge(String s){
        if(!TextUtils.isEmpty(message)){
            ToastUtils.toastShort(message);
        }else if(!TextUtils.isEmpty(s)){
            ToastUtils.toastShort(s);
        }
    }

    @Override
    public <T> T parse(Class<T> cls, String json) {
        Gson gson = GsonHelper.getGson();
        T bean = null;
        try {
            bean = gson.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
