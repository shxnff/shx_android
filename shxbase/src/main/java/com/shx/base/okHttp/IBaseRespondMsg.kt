package com.shx.base.okHttp

/**
 * 功能介绍：接口请求回调基类
 */
interface IBaseRespondMsg {
    /**
     * 请求成功
     * @param response 请求返回数据
     */
    abstract fun onRequestSuccess(response: RbEntity)

    /**
     * 请求失败
     * @param error 请求返回数据
     */
    abstract fun onRequestFail(error: RbEntity)
}