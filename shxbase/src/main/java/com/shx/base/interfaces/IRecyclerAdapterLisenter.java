package com.shx.base.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.shx.base.adapter.base.BaseViewHolder;
import com.shx.base.adapter.base.ShxItem;

import java.util.List;

public interface IRecyclerAdapterLisenter<T> {

    BaseViewHolder generalHolder(ViewGroup parent, int viewType);

    void bindHolder(RecyclerView.ViewHolder holder, ShxItem item, int position);

    boolean buildItems(List<T> listObject, T object, boolean refresh);
}
