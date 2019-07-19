package com.shx.base.utils;

import com.shx.base.constant.ConstantBase;
import com.shx.base.constant.Sp;

public class HostUtils {
    public static String AddHost(String url){
//        boolean isRelease = SPUtils.getBoolean(AppBaseUtils.getApplication(), Sp.is_Release,false);
//        if(isRelease){
//            return ConstantBase.Host_release + url;
//        }else {
            return ConstantBase.Host_test + url;
//        }
    }
}
