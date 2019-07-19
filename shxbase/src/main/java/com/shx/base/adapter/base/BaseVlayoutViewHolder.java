package com.shx.base.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseVlayoutViewHolder extends RecyclerView.ViewHolder {
    protected View mParentView;
    protected int mPosition;

    public BaseVlayoutViewHolder(View itemView) {
        super(itemView);
        mParentView = itemView;
    }

}
