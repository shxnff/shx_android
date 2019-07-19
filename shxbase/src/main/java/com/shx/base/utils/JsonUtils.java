package com.shx.base.utils;


import com.shx.base.bean.PBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

//    ArrayList<JsonUtils.GBean> list = new ArrayList<>();
//    list.add(new JsonUtils.GBean("pageNo", String.valueOf(page)));
//    String params = JsonUtils.toJson(list);

    public static String toJson(ArrayList<PBean> list){
        if(!CollectionUtils.isEmpty(list)){
            JSONObject obj= new JSONObject();
            for (PBean mGBean:list){
                try {
                    obj.put(mGBean.Key,mGBean.Value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return obj.toString();
        }
        return "";
    }
}
