package com.shx.base.vLayout;

import android.content.Context;
import android.view.ViewGroup;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.shx.base.adapter.base.BaseVlayoutViewHolder;

public abstract class VlayoutItemAdapter  <T extends BaseVlayoutViewHolder> extends ShxDelegateAdapter.Adapter<T>  {

    /**
     * 上下文环境
     * 提供get方法来获取
     */
    private Context mContext;

    /**
     * 必要的布局管理属性
     * 被框架管理，所以写在抽象类里面
     */
    private LayoutHelper mLayoutHelper;
    /**
     * 内部的adapter建议只用一个type
     */
    private Object viewType;

    public VlayoutItemAdapter() {

    }

    public VlayoutItemAdapter setViewType(Object viewType) {
        this.viewType = viewType;
        return this;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext =  mContext;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.mLayoutHelper;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return doOnCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        doOnBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return doGetItemCount();
    }

    public abstract T doOnCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void doOnBindViewHolder(T holder, int position);
    public abstract int doGetItemCount();
}