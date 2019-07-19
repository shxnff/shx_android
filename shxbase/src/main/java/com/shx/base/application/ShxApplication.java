package com.shx.base.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shx.base.imgFacebook.ShxImageUtils;
import com.shx.base.okHttp.HttpUtils;
import com.shx.base.utils.AppBaseUtils;

public class ShxApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppBaseUtils.init(this);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //ARouter初始化
        ARouter.init(this);

        //网络请求初始化
        HttpUtils.init();

        //图片加载
        ShxImageUtils.getInstance().init();

    }

}
