package com.shx.base.adapter.demo.list1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shx.base.adapter.base.BaseCommonAdapter;
import com.shx.base.adapter.base.BaseViewHolder;
import com.shx.base.R;
import com.shx.base.adapter.base.ShxItem;
import com.shx.base.adapter.bean.ListBean;
import com.shx.base.adapter.viewHolder.Item01ViewHolder;
import com.shx.base.adapter.viewHolder.Item02ViewHolder;
import com.shx.base.adapter.viewHolder.ItemOverViewHolder;
import com.shx.base.interfaces.IRecyclerAdapterLisenter;
import com.shx.base.utils.CollectionUtils;

import java.util.List;

public class List1Adapter<T> extends BaseCommonAdapter<T> implements IRecyclerAdapterLisenter<T> {

    public static final int ITEM_01 = 0;
    public static final int ITEM_02 = 1;
    public static final int ITEM_OVER = 2;

    public List1Adapter(List<T> listParam, T obj) {
        super(listParam, obj);
        doInit(this);
    }

    @Override
    public BaseViewHolder generalHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_01:
                return new Item01ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shx_item_text, parent, false));
            case ITEM_02:
                return new Item02ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shx_item_text, parent, false));
            case ITEM_OVER:
                return new ItemOverViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shx_item_over, parent, false));
        }
        return null;
    }

    @Override
    public void bindHolder(RecyclerView.ViewHolder holder, ShxItem item, int position) {
        switch (getItemViewType(position)) {
            case ITEM_01: {
                Item01ViewHolder viewHolder = (Item01ViewHolder) holder;
                viewHolder.setPosition(position);
                viewHolder.setItem(item);
                break;
            }
            case ITEM_02: {
                Item02ViewHolder viewHolder = (Item02ViewHolder) holder;
                viewHolder.setPosition(position);
                viewHolder.setItem(item);
                break;
            }
            case ITEM_OVER: {
                ItemOverViewHolder viewHolder = (ItemOverViewHolder) holder;
                viewHolder.setPosition(position);
                viewHolder.setItem(item);
                break;
            }
        }
    }

    @Override
    public boolean buildItems(List<T> listObject, T object, boolean refresh) {
        ListBean bean = (ListBean) object;
        if (bean != null) {

            if (!CollectionUtils.isEmpty(bean.list)) {
                for (ListBean.ItemData s : bean.list) {
                    if (s.type == 1) {
                        buildItem((T) s, ITEM_01);
                    } else if (s.type == 2) {
                        buildItem((T) s, ITEM_02);
                    }
                }
            }

            if (bean.recordPage == bean.currentPage) {
                buildItem(null, ITEM_OVER);
            }

        }
        return false;
    }

    public void buildItem(T obj, int type) {
        ShxItem item = new ShxItem();
        item.setObj(obj);
        item.setViewType(type);
        mItems.add(item);
    }
}
