package com.shx.base.adapter.demo.list2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.shx.base.adapter.bean.ListBean;
import com.shx.base.vLayout.ShxDelegateAdapter;
import com.shx.base.vLayout.ShxVlayoutAdapter;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.vLayout.ShxDelegateAdapter;
import com.shx.base.vLayout.ShxVlayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class List2Adapter extends ShxVlayoutAdapter {

    List2Item01Adapter adapter1;
    List2Item02Adapter adapter2;
    List2Item05Adapter adapter5;
    List2Item06Adapter adapter6;

    public List2Adapter(Context mContext, RecyclerView mRecyclerView) {
        super(mContext, mRecyclerView);
    }

    public void setAdapter(ListBean data) {
        delegateAdapter.setAdapters(setData(data));
    }

    public void addAdapter(ListBean data) {
        delegateAdapter.addAdapters(setData(data));
    }

    private List<ShxDelegateAdapter.Adapter> setData(ListBean data){

        List<ShxDelegateAdapter.Adapter> list = new ArrayList<>();

        for (ListBean.ItemData itemBean : data.list) {
            switch (itemBean.type) {
                case 1: {
                    //通栏布局，只会显示一个组件View
                    adapter1 = new List2Item01Adapter(mContext, itemBean);
                    list.add(adapter1);
                    break;
                }

                case 2: {
                    //Grid布局， 支持横向的colspan
                    adapter2 = new List2Item02Adapter(mContext, itemBean);
                    list.add(adapter2);
                    break;
                }

                case 5: {
                    // stikcy布局， 可以配置吸顶或者吸底
                    adapter5 = new List2Item05Adapter(mContext, itemBean);
                    list.add(adapter5);
                    break;
                }

                case 6: {
                    //瀑布流布局，可配置间隔高度/宽度
                    if(adapter6==null){
                        adapter6 = new List2Item06Adapter(mContext, itemBean);
                        list.add(adapter6);
                    }else {
                        adapter6.addData(itemBean);
                        adapter6.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
        return list;
    }
}
