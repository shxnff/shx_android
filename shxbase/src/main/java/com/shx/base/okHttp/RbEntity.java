package com.shx.base.okHttp;


import android.text.TextUtils;

import com.shx.base.utils.ToastUtils;

/**
 * 请求返回的实体
 */

public class RbEntity {

    /**
     * 区分单个接口的请求标识
     */
    public int rbTag;
    public Class clz;
    public Object data;
    /**
     * 请求的code
     */
    public int error_code = -1;
    /**
     * 请求的msg
     */
    public String errorMsg;
    /**
     * 备用integer型参数
     */
    public int standbyInt = -1;

    public RbEntity() {

    }

    public RbEntity(int error_code) {
        this.error_code = error_code;
    }

    public <T> T getData() {
        return (T) data;
    }

    public void showMessasge(String s){
        if(!TextUtils.isEmpty(errorMsg)){
            ToastUtils.toastShort(errorMsg);
        }else if(!TextUtils.isEmpty(s)){
            ToastUtils.toastShort(s);
        }
    }

}
