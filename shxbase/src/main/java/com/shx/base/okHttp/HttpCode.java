package com.shx.base.okHttp;

/**
 * 功能介绍：Http请求的code,包含服务器返回的，和http本身的
 */
public interface HttpCode {
    /**
     * 成功
     */
    public static final int CODE_SUCCESS = 1;
    /**
     * 接口请求标识
     * 101：普通接口
     * 202：加密接口
     * 303：获取加密接口信息的接口
     */
    public static final int CODE_HTTP_FLAG_DEFAULT = 101;
    public static final int CODE_HTTP_FLAG_RSA = 202;
    public static final int CODE_HTTP_FLAG_RSA_GET = 303;
    /**
     * 没有网络
     */
    public static final int ERROR_NO_NERWORK = -600;
    public static String MSG_NO_NERWORK = "请检查网络设置";
    /**
     * 没有登录
     */
    public static final int ERROR_NO_LOGIN = -601;
    public static String MSG_NO_LOGIN = "请您先登录";
    /**
     * 数据解析错误 对象为null
     */
    public static int ERROR_HTTP_DATA_NULL = -602;
    /**
     * 数据解析错误  http返回的body为null或“”
     */
    public static int ERROR_HTTP_DATA_BODY_NULL = -603;
    /**
     * 数据解析错误  json解析错误
     */
    public static int ERROR_HTTP_DATA_JSON_ERROR = -604;

    public static String MSG_DATA_ERROR = "操作失败";//"数据解析错误";

    /**
     * 接口请求失败
     */
    public final static int ERROR_SERVER_PROBLEM = 10000;
    public final static int ERROR_BAD_PARAS = 10001;


}
