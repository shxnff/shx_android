package com.shx.base.okHttp;

import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.shx.base.bean.PBean;
import com.shx.base.utils.AppBaseUtils;
import com.shx.base.utils.HostUtils;
import com.shx.base.utils.JsonUtils;
import com.shx.base.utils.NetworkUtils;
import com.shx.base.utils.MsStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 各接口的参数封装
 */
public class ShxRequest {

    public static void requestPost( Object requestTag,
                               int rbTag,
                               String url,
                               Map<String, String> params,
                               IBaseRespondMsg callback, Class<?> clz) {

//        if(params==null){
//            params = new HashMap<>();
//        }
//        int code = AppBaseUtils.getVersionCode();
//        params.put("AppCode",String.valueOf(code));

        ShxRequest.mBuilder(url)
                .requestTag(requestTag)
                .rbTag(rbTag)
                .params(params)
                .callback(callback)
                .clz(clz)
                .request(1);
    }

    public static void requestPost( Object requestTag,
                                    int rbTag,
                                    String url,
                                    ArrayList<PBean> list,
                                    IBaseRespondMsg callback, Class<?> clz) {

//        if(list==null){
//            list = new ArrayList<>();
//        }
//        int code = AppBaseUtils.getVersionCode();
//        list.add(new PBean("AppCode",String.valueOf(code)));

        ShxRequest.mBuilder(url)
                .requestTag(requestTag)
                .rbTag(rbTag)
                .params(JsonUtils.toJson(list))
                .callback(callback)
                .clz(clz)
                .request(2);
    }

    public static void requestPut( Object requestTag,
                                    int rbTag,
                                    String url,
                                    ArrayList<PBean> list,
                                    IBaseRespondMsg callback, Class<?> clz) {

//        if(list==null){
//            list = new ArrayList<>();
//        }
//        int code = AppBaseUtils.getVersionCode();
//        list.add(new PBean("AppCode",String.valueOf(code)));

        ShxRequest.mBuilder(url)
                .requestTag(requestTag)
                .rbTag(rbTag)
                .params(JsonUtils.toJson(list))
                .callback(callback)
                .clz(clz)
                .request(4);
    }

    public static void requestGet(Object requestTag,
                                   int rbTag,
                                   String url,
                                  IBaseRespondMsg callback, Class<?> clz) {

//        String code = String.valueOf(AppBaseUtils.getVersionCode());
//        if(url.contains("?")){
//            url = url + "&AppCode=" + code;
//        }else {
//            url = url + "?AppCode=" + code;
//        }

        ShxRequest.mBuilder(url)
                .requestTag(requestTag)
                .rbTag(rbTag)
                .callback(callback)
                .clz(clz)
                .request(3);
    }

    private static void onRequestFail(int code, String msg, Builder builder) {
        RbEntity entity = new RbEntity(code);
        entity.errorMsg = msg;
        entity.rbTag = builder.rbTag;
        builder.onRequestFail(entity);
    }

    private static void request(Builder builder,int type) {
        if (!NetworkUtils.checkNetworkConnection(AppBaseUtils.getApplication())) {
            onRequestFail(HttpCode.ERROR_NO_NERWORK, HttpCode.MSG_NO_NERWORK, builder);
            return;
        }

        /**
         * 添加返回实体
         */
        RbEntity entity = new RbEntity();
        entity.rbTag = builder.rbTag;
        entity.clz = builder.clz;


        builder.url = HostUtils.AddHost(builder.url);

        //普通接口
        switch (type){
            case 1:{
                HttpUtils.requestPost(
                        builder.requestTag,
                        builder.url,
                        builder.params,
                        new RbCallback(builder.callback, entity));
                break;
            }
            case 2:{
                HttpUtils.requestPost(
                        builder.requestTag,
                        builder.url,
                        builder.sParams,
                        new RbCallback(builder.callback, entity));
                break;
            }
            case 3:{
                HttpUtils.requestGet(
                        builder.requestTag,
                        builder.url,
                        new RbCallback(builder.callback, entity));
                break;
            }
            case 4:{
                HttpUtils.requestPut(
                        builder.requestTag,
                        builder.url,
                        builder.sParams,
                        new RbCallback(builder.callback, entity));
                break;
            }
        }


    }

    /**
     * 网络接口--获取Bitmap
     */
    public static void requestBitmap(Object requestTag, RbEntity entity, String url, IRespondMsg callback) {
        Map<String, String> params = new HashMap<>();
        HttpUtils.requestPost(requestTag, url, params, new RbBitmapCallback(callback, entity));
    }

    /**
     * 网络接口--获取Bitmap
     */
    public static void requestBitmap(Object tag, String url, BitmapCallback callback) {
        if (!MsStringUtils.isEmpty(url)) {
            HttpUtils.requestBitmap(tag, url, callback);
        }
    }

    /**
     * 网络接口--获取Bitmap
     */
    public static void requestBitmap(Object tag, String url, String jsonParams, BitmapCallback callback) {
        if (!MsStringUtils.isEmpty(url)) {
            HttpUtils.requestBitmap(tag, url, jsonParams, callback);
        }
    }

    /**
     * 网络接口--下载文件
     */
    public static void requestFile(Object tag, String url, FileCallback callback) {
        if (!MsStringUtils.isEmpty(url)) {
            HttpUtils.requestFile(tag, url, callback);
        }
    }

    public static void cancelRequest(Object requestTag) {
        HttpUtils.cancel(requestTag);
    }

    public static Builder mBuilder(String url) {
        return new Builder(url);
    }

    /**
     * 请求构建者
     */
    public static final class Builder {
        private Object requestTag;
        private int rbTag;
        private String url;
        private Map<String, String> params;
        private String sParams;
        private IBaseRespondMsg callback;
        private Class<?> clz = RbBean.class;

        public Builder(String url) {
            this.url = url;
        }

        public Builder requestTag(Object val) {
            requestTag = val;
            return this;
        }

        public Builder rbTag(int val) {
            rbTag = val;
            return this;
        }

        public Builder params(Map<String, String> val) {
            if (params != null) {
                params.putAll(val);
            } else {
                params = val;
            }
            return this;
        }

        public Builder params(String sParams) {
            this.sParams = sParams;
            return this;
        }


        public Builder addParams(String key, String value) {
            if (MsStringUtils.isEmpty(key)) {
                return this;
            }
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, value);
            return this;
        }

        public Builder callback(IBaseRespondMsg val) {
            callback = val;
            return this;
        }

        public Builder clz(Class<?> val) {
            clz = val;
            return this;
        }

        private void onRequestFail(RbEntity entity) {
            if (callback != null) {
                callback.onRequestFail(entity);
            }
        }

        public void request(int type) {
            if (params == null) {
                params = new HashMap<>();
            }
            ShxRequest.request(this,type);
        }
    }
}
