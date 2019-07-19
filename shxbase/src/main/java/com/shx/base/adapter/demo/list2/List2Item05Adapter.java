package com.shx.base.adapter.demo.list2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.R;
import com.shx.base.adapter.base.BaseVlayoutViewHolder;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.vLayout.VlayoutItemAdapter;

public class List2Item05Adapter extends VlayoutItemAdapter<List2Item05Adapter.ItemV01> {

    ListBean.ItemData itemBean;

    public List2Item05Adapter(Context context, ListBean.ItemData itemBean) {
        setContext(context);
        this.itemBean = itemBean;
    }

    @Override
    public ItemV01 doOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemV01(LayoutInflater.from(this.getContext()).inflate(R.layout.shx_item_text2, parent, false));
    }

    @Override
    public void doOnBindViewHolder(ItemV01 holder, int position) {
        if (holder != null) {
            holder.text.setText(itemBean.name);
        }
    }

    @Override
    public int doGetItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StickyLayoutHelper helper = new StickyLayoutHelper(true);
        helper.setBgColor(getContext().getResources().getColor(R.color.cred));
        return helper;
    }

    static class ItemV01 extends BaseVlayoutViewHolder {
        public TextView text;

        public ItemV01(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
