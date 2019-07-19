package com.shx.base.adapter.demo.list2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.vLayout.VlayoutItemAdapter;
import com.shx.base.R;
import com.shx.base.adapter.base.BaseVlayoutViewHolder;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.vLayout.VlayoutItemAdapter;

public class List2Item01Adapter extends VlayoutItemAdapter<List2Item01Adapter.ItemV01> {

    ListBean.ItemData itemBean;

    public List2Item01Adapter(Context context,ListBean.ItemData itemBean) {
        setContext(context);
        this.itemBean = itemBean;
    }

    @Override
    public ItemV01 doOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemV01(LayoutInflater.from(this.getContext()).inflate(R.layout.shx_item_text, parent, false));
    }

    @Override
    public void doOnBindViewHolder(ItemV01 holder, int position) {
        if (holder != null) {
            holder.text.setText(itemBean.type + "*" + itemBean.name);
        }
    }

    @Override
    public int doGetItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
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
