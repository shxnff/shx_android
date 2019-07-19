package com.shx.base.bean;

import com.ashlikun.mulittypegson.BaseMultiData;
import com.ashlikun.mulittypegson.BaseMultiParentData;
import com.ashlikun.mulittypegson.GsonHelper;
import com.shx.base.okHttp.RbBean;
import com.shx.base.okHttp.RbBean;

import java.util.ArrayList;
import java.util.List;

public class DemoBean extends RbBean {


    /**
     ****************************************数据模型****************************************

     {
         "code":1,
         "msg":"操作成功",
         "data":[
                     {
                         "type":"baseInfo",
                         "data":{
                                 "goods_id":"123",
                                 "goods_name":"name"
                         }
                     },

                     {
                         "type":"extInfo",
                         "list":[
                                 {
                                     "goods_id":"123",
                                     "goods_name":"name"
                                 },

                                 {
                                     "goods_id":"123",
                                     "goods_name":"name"
                                 }
                        ]
                     }
                ]
     }
     */

    public String code;
    public ArrayList<DataList> data;

    @Override
    public <T> T parse(Class<T> cls, String json) {
        return GsonHelper.getMultiTypeGsonBuilder()
                .registerTypeElementName("type")
                .registerTargetParentClass(DataList.class)
                .registerTypeElementClass("baseInfo", Info.class)
                .registerTypeElementClass("extInfo", InfoExt.class)
                .build()
                .create().fromJson(json, cls);
    }

    public static class DataList implements BaseMultiParentData {
        public String type;
        public List<BaseMultiData> list;
        public BaseMultiData data;

        @Override
        public <T> List<T> getArrayData() {
            return (List<T>) list;
        }

        @Override
        public <T> T getObjectData() {
            return (T) data;
        }
    }

    public static class Info implements BaseMultiData {
        public String goods_id;
        public String goods_name;
    }

    public static class InfoExt implements BaseMultiData {
        public String goods_id;
        public String goods_name;
    }

}
