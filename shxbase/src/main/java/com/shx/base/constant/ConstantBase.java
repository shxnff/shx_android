package com.shx.base.constant;

import android.os.Environment;

public class ConstantBase {

    //测试环境
    public static final String Host_test = "http://192.168.1.201:9888";
    //正式环境
    public static final String Host_release = "http://120.27.63.106:8080";

    //************************SP_KEY***********************************************
    public final static String SP_ONE_LAUNCH_CODE = "SP_ONE_LAUNCH_CODE";
    //************************SP_KEY***********************************************

    //支付宝
    public static final String credential = "alipay_sdk=alipay-sdk-php-20161101&app_id=2019022863438383&biz_content=%7B%22body%22%3A+%22%E5%BC%80%E9%80%9A%E4%BC%9A%E5%91%98%22%2C%22subject%22%3A+%22%E5%BC%80%E9%80%9A%E4%BC%9A%E5%91%98%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A+%2220190411349b4c5f95cfw%22%2C%22timeout_express%22%3A+%223d%22%2C%22total_amount%22%3A+%22999.00%22%2C%22product_code%22%3A+%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fvideo.meiyayuming.com.cn%2Fapi%2ForderAlipayCall%2Findex.php&sign_type=RSA2&timestamp=2019-04-11+09%3A20%3A44&version=1.0&sign=M52cdjh9Kw8YeITSrYgqq%2BvA8aaRIlZhkQGwpx%2FTq46FBN2ZTF%2FcvHSnhxgnOBp63lOmDwoXDP01MiODBHNKMDa7Uz9GcyEigU2QtdgHSXInkaDzw0NWiLElp6ohlNqHnnmxZnNgs5hNj9ptvrKHj9uRa7DG0mdvgtoyEMcJKaJ1yeTRwCYKcSaVPB74%2BS8M6Fo89PMvvsjjNEEuhwXysv8foAyyw4gnLWFtvNAfqMRu6RPVg%2Bndd129PDvOxA6H1TH1Aqtq7XdPjnMGWi3Pb%2BCaPm0bEe7jBEhMts5gHxRM3%2B25b0w3nz1wF76Bqwv5yw0UcLxrXuCoie%2FuOYvWcw%3D%3D";

    //微信
    public static final String appId = "wx8d226abe38095983";
    public static final String partnerId = "1527023501";
    public static final String prepayId = "wx11092028541112474c97b6870191427379";
    public static final String sign = "8EB932834758A6E45D6BDED278F31CA2";
    public static final String nonceStr = "3CYSHG2ZHUESS0VAQVD9DX2F4F0TUMWS";
    public static final String timeStamp = "1554945628";
    public static final String packageValue = "Sign=WXPay";

    //文件下载路径
    public static final String downloaddir = Environment.getExternalStorageDirectory() + "/shx/apk";

    //token err
    public static final int EventBus_Token_Err = 10013;

}
