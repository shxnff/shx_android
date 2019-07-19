package com.shx.base.vLayout;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.alibaba.android.vlayout.VirtualLayoutManager;

public class ShxVlayoutAdapter {

    public RecyclerView mRecyclerView;
    public Context mContext;
    //管理工具
    public VirtualLayoutManager layoutManager;
    //适配器代理
    public ShxDelegateAdapter delegateAdapter;

    public ShxVlayoutAdapter(Context mContext,RecyclerView mRecyclerView){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        init();
    }

    public void init(){
        /**
         * 签订管理协议
         */
        layoutManager = new VirtualLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        /**
         * 设置适配器
         * true or false 表示不同类型的adpter，item是否会复用
         */
        delegateAdapter = new ShxDelegateAdapter(layoutManager, false);
        mRecyclerView.setAdapter(delegateAdapter);

    }

}
