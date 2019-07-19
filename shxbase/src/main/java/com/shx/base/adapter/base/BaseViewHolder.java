package com.shx.base.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    protected View mParentView;
    protected int mPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mParentView = itemView;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getItemPosition() {
        return mPosition;
    }

    public abstract void setItem(ShxItem item);

}
