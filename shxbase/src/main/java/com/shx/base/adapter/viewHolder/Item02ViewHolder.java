package com.shx.base.adapter.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shx.base.adapter.base.BaseViewHolder;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.R;
import com.shx.base.adapter.base.BaseViewHolder;
import com.shx.base.adapter.base.ShxItem;
import com.shx.base.adapter.bean.ListBean;


public class Item02ViewHolder extends BaseViewHolder {

    TextView text;

    public Item02ViewHolder(View itemView) {
        super(itemView);
        mParentView = itemView;

        text = mParentView.findViewById(R.id.text);
    }

    @Override
    public void setItem(ShxItem item) {
        if (item == null) {
            return;
        }

        ListBean.ItemData data = (ListBean.ItemData) item.getObj();
        if (!TextUtils.isEmpty(data.name)) {
            text.setText(data.name);
        }

        text.setBackgroundResource(R.color.c999999);

    }

}
