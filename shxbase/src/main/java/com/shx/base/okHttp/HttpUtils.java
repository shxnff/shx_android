package com.shx.base.okHttp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.shx.base.constant.Sp;
import com.shx.base.utils.AppBaseUtils;
import com.shx.base.utils.HostUtils;
import com.shx.base.utils.SPUtils;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by qzp on 2016/10/8 0008.
 */

public class HttpUtils {

    public static void init() {

//        HttpParams params = new HttpParams();
//        int code = AppBaseUtils.getVersionCode();
//        params.put("appCode", String.valueOf(code));

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //***********打印日志********************************************
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //***********打印日志********************************************

        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        //dns
        //builder.dns(OkHttpDns.getInstance(AppUtils.getApplication()));

        OkGo.getInstance().init(AppBaseUtils.getApplication())  //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonParams(params);                     //全局公共参数
//                .addCommonHeaders(headers)                    //全局公共头
    }

    /**
     * 普通请求
     */
    public static void requestPost(Object tag, String url, Map<String, String> params, AbsCallback callBack) {

        final String host = getHost(url);

        PostRequest br = OkGo.<String>post(url);
        br.tag(tag);
        br.headers("Host", host);
        String token = SPUtils.getString(AppBaseUtils.getApplication(), Sp.User_token, "");
        if (!TextUtils.isEmpty(token)) {
            br.headers("Authorization", token);
        }
        br.params(params);
        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }

        br.execute(callBack);
    }

    /**
     * 普通请求
     */
    public static void requestPost(Object tag, String url, String params, AbsCallback callBack) {
        final String host = getHost(url);
        PostRequest br = OkGo.<String>post(url);
        br.tag(tag);
        br.headers("Host", host);
        String token = SPUtils.getString(AppBaseUtils.getApplication(), Sp.User_token, "");
        if (!TextUtils.isEmpty(token)) {
            br.headers("Authorization", token);
        }
        br.upString(params, MediaType.parse("application/json;charset=utf-8"));
        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }
        br.execute(callBack);
    }

    public static void requestPut(Object tag, String url, String params, AbsCallback callBack) {
        final String host = getHost(url);
        PutRequest<String> br = OkGo.<String>put(url);
        br.tag(tag);
        br.headers("Host", host);
        String token = SPUtils.getString(AppBaseUtils.getApplication(), Sp.User_token, "");
        if (!TextUtils.isEmpty(token)) {
            br.headers("Authorization", token);
        }
        br.upString(params, MediaType.parse("application/json;charset=utf-8"));
        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }
        br.execute(callBack);
    }

    public static void requestGet(Object tag, String url, AbsCallback callBack) {

        final String host = getHost(url);

        GetRequest bg = OkGo.<String>get(url);
        bg.tag(tag);
        bg.headers("Host", host);
        String token = SPUtils.getString(AppBaseUtils.getApplication(), Sp.User_token, "");
        if (!TextUtils.isEmpty(token)) {
            bg.headers("Authorization", token);
        }
        bg.cacheMode(CacheMode.NO_CACHE);
        try {
            OkGo.getInstance().getOkHttpClient().newCall(bg.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }

        bg.execute(callBack);
    }

    /**
     * Bitmap请求
     */
    public static void requestBitmap(Object tag, String url, BitmapCallback callBack) {
        String host = getHost(url);

        PostRequest br = OkGo.<Bitmap>post(url)
                .tag(tag)
                .headers("Host", host);

        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }

        br.execute(callBack);

    }

    /**
     * Bitmap请求
     */
    public static void requestBitmap(Object tag, String url, String params, BitmapCallback callBack) {

        String host = getHost(url);

        PostRequest br = OkGo.<Bitmap>post(url).tag(tag).headers("Host", host).upJson(params);

        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }

        br.execute(callBack);
    }

    /**
     * File请求
     */
    public static void requestFile(Object tag, String url, FileCallback callBack) {

        String host = getHost(url);

        GetRequest<File> br = OkGo.<File>get(url).tag(tag).headers("Host", host);

        try {
            OkGo.getInstance().getOkHttpClient().newCall(br.getRequest());
        } catch (AssertionError error) {
            error.printStackTrace();
        }

        br.execute(callBack);
    }

    /**
     * 上传文件
     */
    public static void postFile(final String upTag, String url, String path) {

        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        PostRequest br = OkGo.<String>post(url)
                .params("file", file)
                .converter(new StringConvert());

        OkUpload.request(upTag, br)
                .save()
                .register(new UploadListener("uploadcontact") {
                    @Override
                    public void onStart(Progress progress) {

                    }

                    @Override
                    public void onProgress(Progress progress) {

                    }

                    @Override
                    public void onError(Progress progress) {
                        OkUpload.getInstance().removeTask(upTag);
                    }

                    @Override
                    public void onFinish(Object o, Progress progress) {
                        OkUpload.getInstance().removeTask(upTag);
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        OkUpload.getInstance().removeTask(upTag);
                    }
                }).start();
    }

    public static void postFile(Object tag, String url, String path, StringCallback callBack) {

        File file = new File(path);

        if (file == null || !file.exists()) {
            return;
        }

        PostRequest br = OkGo.<String>post(HostUtils.AddHost(url));
        br.tag(tag);
        br.headers("Content-Type", "multipart/form-data");
        br.isMultipart(true);
        String token = SPUtils.getString(AppBaseUtils.getApplication(), Sp.User_token, "");
        if (!TextUtils.isEmpty(token)) {
            br.headers("Authorization", token);
        }
        br.params("file", file);
        br.isMultipart(true);
        br.execute(callBack);
    }

    /**
     * 取消本tag下的所有请求
     */
    public static void cancel(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

    /**
     * 这个url对应的请求是否正在请求
     */
    public static boolean isStartUrl(String url) {
        if (url == null) {
            return false;
        }
        for (Call call : OkGo.getInstance().getOkHttpClient().dispatcher().queuedCalls()) {
            String startUrl = call.request().url().toString();
            if (startUrl != null && startUrl.startsWith(url)) {
                return true;
            }
        }
        for (Call call : OkGo.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            String startUrl = call.request().url().toString();
            if (startUrl != null && startUrl.startsWith(url)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取host
     */
    public static String getHost(String url) {
        if (!TextUtils.isEmpty(url)) {
            URI uri = URI.create(url);

            if (uri != null) {
                return uri.getHost();
            }
        }

        return "";
    }


    /**
     * 方法功能：运行主线程
     */
    public static void runmainThread(Consumer<Integer> next) {
        runmainThread(1, next);
    }

    public static void runmainThread(int id, Consumer<Integer> next) {
        Observable.just(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(next);
    }

    /**
     * 方法功能：运行在子线程
     */
    public static void runChildThread(Consumer<Integer> next) {
        runChildThread(1, next);
    }

    public static void runChildThread(int id, Consumer<Integer> next) {
        Observable.just(id).observeOn(Schedulers.computation())
                .subscribe(next);
    }
}
