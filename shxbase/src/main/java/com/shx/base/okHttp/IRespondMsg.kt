package com.shx.base.okHttp

/**
 * Created by Administrator on 2018/1/2 0002.
 */
interface IRespondMsg :IBaseRespondMsg{
    /**
     * 发起请求的入口
     * @param tag
     */
    abstract fun doRequest(tag: Int, vararg objs: Any)


}