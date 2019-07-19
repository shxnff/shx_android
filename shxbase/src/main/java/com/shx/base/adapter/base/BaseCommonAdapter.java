package com.shx.base.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.shx.base.interfaces.IRecyclerAdapterLisenter;
import com.shx.base.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommonAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<ShxItem> mItems = new ArrayList<ShxItem>();

    private List<T> mListParamOne;
    private T mObject;

    public IRecyclerAdapterLisenter<T> mCallback;

    public BaseCommonAdapter(List<T> listParam, T obj) {
        this.mListParamOne = listParam;
        this.mObject = obj;
    }

    public void doInit(IRecyclerAdapterLisenter<T> callback) {
        this.mCallback = callback;
        buildItems(mListParamOne, mObject, true);
    }

    @Override
    public int getItemCount() {
        return !CollectionUtils.isEmpty(mItems) ? mItems.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (CollectionUtils.isEmpty(mItems)) {
            return 0;
        }

        if (mItems.size() > position) {
            return mItems.get(position).getViewType();
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mCallback.generalHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShxItem item = mItems.get(position);
        mCallback.bindHolder(holder, item, position);
    }

    private void buildItems(List<T> listObject, T object, boolean refresh) {

        if (refresh) {
            mItems.clear();
        }

        this.mCallback.buildItems(listObject, object, refresh);
    }

    public void addData(List<T> listObject, T object, boolean refresh) {
        buildItems(listObject, object, refresh);
        notifyDataSetChanged();
    }

}
