package com.shx.base.vLayout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.Cantor;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.support.v7.widget.RecyclerView.NO_ID;

public class ShxDelegateAdapter extends VirtualLayoutAdapter<RecyclerView.ViewHolder> {

    @Nullable
    private AtomicInteger mIndexGen;

    private int mIndex = 0;

    private final boolean mHasConsistItemType;

    private SparseArray<ShxDelegateAdapter.Adapter> mItemTypeAry = new SparseArray<>();

    @NonNull
    private final List<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> mAdapters = new ArrayList<>();

    private int mTotal = 0;

    private final SparseArray<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> mIndexAry = new SparseArray<>();

    private long[] cantorReverse = new long[2];

    /**
     * Delegate Adapter merge multi sub adapters, default is thread-unsafe
     *
     * @param layoutManager layoutManager
     */
    public ShxDelegateAdapter(VirtualLayoutManager layoutManager) {
        this(layoutManager, false, false);
    }

    /**
     * @param layoutManager      layoutManager
     * @param hasConsistItemType whether sub adapters itemTypes are consistent
     */
    public ShxDelegateAdapter(VirtualLayoutManager layoutManager, boolean hasConsistItemType) {
        this(layoutManager, hasConsistItemType, false);
    }

    /**
     * @param layoutManager      layoutManager
     * @param hasConsistItemType whether sub adapters itemTypes are consistent
     * @param threadSafe         tell whether your adapter is thread-safe or not
     */
    ShxDelegateAdapter(VirtualLayoutManager layoutManager, boolean hasConsistItemType, boolean threadSafe) {
        super(layoutManager);
        if (threadSafe) {
            mIndexGen = new AtomicInteger(0);
        }

        mHasConsistItemType = hasConsistItemType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHasConsistItemType) {
            ShxDelegateAdapter.Adapter adapter = mItemTypeAry.get(viewType);
            if (adapter != null) {
                return adapter.onCreateViewHolder(parent, viewType);
            }

            return null;
        }


        // reverse Cantor Function
        Cantor.reverseCantor(viewType, cantorReverse);

        int index = (int)cantorReverse[1];
        int subItemType = (int)cantorReverse[0];

        ShxDelegateAdapter.Adapter adapter = findAdapterByIndex(index);
        if (adapter == null) {
            return null;
        }

        return adapter.onCreateViewHolder(parent, subItemType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = findAdapterByPosition(position);
        if (pair == null) {
            return;
        }

        pair.second.onBindViewHolder(holder, position - pair.first.mStartPosition);
        pair.second.onBindViewHolderWithOffset(holder, position - pair.first.mStartPosition, position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = findAdapterByPosition(position);
        if (pair == null) {
            return;
        }
        pair.second.onBindViewHolder(holder, position - pair.first.mStartPosition, payloads);
        pair.second.onBindViewHolderWithOffset(holder, position - pair.first.mStartPosition, position, payloads);

    }

    @Override
    public int getItemCount() {
        return mTotal;
    }

    /**
     * Big integer of itemType returned by delegated adapter may lead to failed
     *
     * @param position item position
     * @return integer represent item view type
     */
    @Override
    public int getItemViewType(int position) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> p = findAdapterByPosition(position);
        if (p == null) {
            return RecyclerView.INVALID_TYPE;
        }

        int subItemType = p.second.getItemViewType(position - p.first.mStartPosition);

        if (subItemType < 0) {
            // negative integer, invalid, just return
            return subItemType;
        }

        if (mHasConsistItemType) {
            mItemTypeAry.put(subItemType, p.second);
            return subItemType;
        }


        int index = p.first.mIndex;

        return (int) Cantor.getCantor(subItemType, index);
    }


    @Override
    public long getItemId(int position) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> p = findAdapterByPosition(position);

        if (p == null) {
            return NO_ID;
        }

        long itemId = p.second.getItemId(position - p.first.mStartPosition);

        if (itemId < 0) {
            return NO_ID;
        }

        int index = p.first.mIndex;
        /*
         * Now we have a pairing function problem, we use cantor pairing function for itemId.
         */
        return Cantor.getCantor(index, itemId);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        // do nothing
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        int position = holder.getPosition();
        if (position >= 0) {
            Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = findAdapterByPosition(position);
            if (pair != null) {
                pair.second.onViewRecycled(holder);
            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getPosition();
        if (position >= 0) {
            Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = findAdapterByPosition(position);
            if (pair != null) {
                pair.second.onViewAttachedToWindow(holder);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getPosition();
        if (position >= 0) {
            Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = findAdapterByPosition(position);
            if (pair != null) {
                pair.second.onViewDetachedFromWindow(holder);
            }
        }
    }


    /**
     * You can not set layoutHelpers to delegate adapter
     */
    @Deprecated
    @Override
    public void setLayoutHelpers(List<LayoutHelper> helpers) {
        throw new UnsupportedOperationException("ShxDelegateAdapter doesn't support setLayoutHelpers directly");
    }


    public void setAdapters(@Nullable List<ShxDelegateAdapter.Adapter> adapters) {
        clear();

        if (adapters == null) {
            adapters = Collections.emptyList();
        }

        List<LayoutHelper> helpers = new LinkedList<>();

        boolean hasStableIds = true;
        mTotal = 0;

        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair;
        for (ShxDelegateAdapter.Adapter adapter : adapters) {
            // every adapter has an unique index id
            ShxDelegateAdapter.AdapterDataObserver observer = new ShxDelegateAdapter.AdapterDataObserver(mTotal, mIndexGen == null ? mIndex++ : mIndexGen.incrementAndGet());
            adapter.registerAdapterDataObserver(observer);
            hasStableIds = hasStableIds && adapter.hasStableIds();
            LayoutHelper helper = adapter.onCreateLayoutHelper();

            helper.setItemCount(adapter.getItemCount());
            mTotal += helper.getItemCount();
            helpers.add(helper);
            pair = Pair.create(observer, adapter);
            mIndexAry.put(observer.mIndex, pair);
            mAdapters.add(pair);
        }

        if (!hasObservers()) {
            super.setHasStableIds(hasStableIds);
        }
        super.setLayoutHelpers(helpers);
    }

    /**
     * Add adapters in <code>position</code>
     *
     * @param position the index where adapters added
     * @param adapters adapters
     */
    public void addAdapters(int position, @Nullable List<ShxDelegateAdapter.Adapter> adapters) {
        if (adapters == null || adapters.size() == 0) {
            return;
        }
        if (position < 0) {
            position = 0;
        }

        if (position > mAdapters.size()) {
            position = mAdapters.size();
        }

        List<ShxDelegateAdapter.Adapter> newAdapter = new ArrayList<>();
        Iterator<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> itr = mAdapters.iterator();
        while (itr.hasNext()) {
            Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = itr.next();
            ShxDelegateAdapter.Adapter theOrigin = pair.second;
            newAdapter.add(theOrigin);
        }
        for (ShxDelegateAdapter.Adapter adapter : adapters) {
            newAdapter.add(position, adapter);
            position++;
        }
        setAdapters(newAdapter);
    }

    /**
     * Append adapters to the end
     *
     * @param adapters adapters will be appended
     */
    public void addAdapters(@Nullable List<ShxDelegateAdapter.Adapter> adapters) {
        addAdapters(mAdapters.size(), adapters);
    }

    public void addAdapter(int position, @Nullable ShxDelegateAdapter.Adapter adapter) {
        addAdapters(position, Collections.singletonList(adapter));
    }

    public void addAdapter(@Nullable ShxDelegateAdapter.Adapter adapter) {
        addAdapters(Collections.singletonList(adapter));
    }

    public void removeFirstAdapter() {
        if (mAdapters != null && !mAdapters.isEmpty()) {
            ShxDelegateAdapter.Adapter targetAdatper = mAdapters.get(0).second;
            removeAdapter(targetAdatper);
        }
    }

    public void removeLastAdapter() {
        if (mAdapters != null && !mAdapters.isEmpty()) {
            ShxDelegateAdapter.Adapter targetAdatper = mAdapters.get(mAdapters.size() - 1).second;
            removeAdapter(targetAdatper);
        }
    }

    public void removeAdapter(int adapterIndex) {
        if (adapterIndex >= 0 && adapterIndex < mAdapters.size()) {
            ShxDelegateAdapter.Adapter targetAdatper = mAdapters.get(adapterIndex).second;
            removeAdapter(targetAdatper);
        }
    }

    public void removeAdapter(@Nullable ShxDelegateAdapter.Adapter targetAdapter) {
        if (targetAdapter == null) {
            return;
        }
        removeAdapters(Collections.singletonList(targetAdapter));
    }

    public void removeAdapters(@Nullable List<ShxDelegateAdapter.Adapter> targetAdapters) {
        if (targetAdapters == null || targetAdapters.isEmpty()) {
            return;
        }
        List<LayoutHelper> helpers = new LinkedList<>(super.getLayoutHelpers());
        for (int i = 0, size = targetAdapters.size(); i < size; i++) {
            ShxDelegateAdapter.Adapter one = targetAdapters.get(i);
            Iterator<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> itr = mAdapters.iterator();
            while (itr.hasNext()) {
                Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = itr.next();
                ShxDelegateAdapter.Adapter theOther = pair.second;
                if (theOther.equals(one)) {
                    theOther.unregisterAdapterDataObserver(pair.first);
                    final int position = findAdapterPositionByIndex(pair.first.mIndex);
                    if (position >= 0 && position < helpers.size()) {
                        helpers.remove(position);
                    }
                    itr.remove();
                    break;
                }
            }
        }

        List<ShxDelegateAdapter.Adapter> newAdapter = new ArrayList<>();
        Iterator<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> itr = mAdapters.iterator();
        while (itr.hasNext()) {
            newAdapter.add(itr.next().second);
        }
        setAdapters(newAdapter);
    }

    public void clear() {
        mTotal = 0;
        mIndex = 0;
        if (mIndexGen != null) {
            mIndexGen.set(0);
        }
        mLayoutManager.setLayoutHelpers(null);

        for (Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> p : mAdapters) {
            p.second.unregisterAdapterDataObserver(p.first);
        }


        mItemTypeAry.clear();
        mAdapters.clear();
        mIndexAry.clear();
    }

    public int getAdaptersCount() {
        return mAdapters == null ? 0 : mAdapters.size();
    }

    /**
     * @param absoultePosition
     * @return the relative position in sub adapter by the absoulte position in DelegaterAdapter. Return -1 if no sub adapter founded.
     */
    public int findOffsetPosition(int absoultePosition) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> p = findAdapterByPosition(absoultePosition);
        if (p == null) {
            return -1;
        }
        int subAdapterPosition = absoultePosition - p.first.mStartPosition;
        return subAdapterPosition;
    }

    @Nullable
    public Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> findAdapterByPosition(int position) {
        final int count = mAdapters.size();
        if (count == 0) {
            return null;
        }

        int s = 0, e = count - 1, m;
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> rs = null;

        // binary search range
        while (s <= e) {
            m = (s + e) / 2;
            rs = mAdapters.get(m);
            int endPosition = rs.first.mStartPosition + rs.second.getItemCount() - 1;

            if (rs.first.mStartPosition > position) {
                e = m - 1;
            } else if (endPosition < position) {
                s = m + 1;
            } else if (rs.first.mStartPosition <= position && endPosition >= position) {
                break;
            }

            rs = null;
        }

        return rs;
    }


    public int findAdapterPositionByIndex(int index) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> rs = mIndexAry.get(index);
        return rs == null ? -1 : mAdapters.indexOf(rs);
    }

    public ShxDelegateAdapter.Adapter findAdapterByIndex(int index) {
        Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> rs = mIndexAry.get(index);
        return rs.second;
    }

    protected class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        int mStartPosition;

        int mIndex = -1;

        public AdapterDataObserver(int startPosition, int index) {
            this.mStartPosition = startPosition;
            this.mIndex = index;
        }

        public void updateStartPositionAndIndex(int startPosition, int index) {
            this.mStartPosition = startPosition;
            this.mIndex = index;
        }

        public int getStartPosition() {
            return mStartPosition;
        }

        public int getIndex() {
            return mIndex;
        }

        private boolean updateLayoutHelper() {
            if (mIndex < 0) {
                return false;
            }

            final int idx = findAdapterPositionByIndex(mIndex);
            if (idx < 0) {
                return false;
            }

            Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> p = mAdapters.get(idx);
            List<LayoutHelper> helpers = new LinkedList<>(getLayoutHelpers());
            LayoutHelper helper = helpers.get(idx);

            if (helper.getItemCount() != p.second.getItemCount()) {
                // if itemCount changed;
                helper.setItemCount(p.second.getItemCount());

                mTotal = mStartPosition + p.second.getItemCount();

                for (int i = idx + 1; i < mAdapters.size(); i++) {
                    Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter> pair = mAdapters.get(i);
                    // update startPosition for adapters in following
                    pair.first.mStartPosition = mTotal;
                    mTotal += pair.second.getItemCount();
                }

                // set helpers to refresh range
                ShxDelegateAdapter.super.setLayoutHelpers(helpers);
            }
            return true;
        }

        @Override
        public void onChanged() {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyItemRangeRemoved(mStartPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyItemRangeInserted(mStartPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyItemMoved(mStartPosition + fromPosition, mStartPosition + toPosition);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyItemRangeChanged(mStartPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (!updateLayoutHelper()) {
                return;
            }
            notifyItemRangeChanged(mStartPosition + positionStart, itemCount, payload);
        }
    }

    /**
     * return an adapter that only contains one item, and using SimpleLayoutHelper
     *
     * @param view the only view, no binding is required
     * @return adapter
     */
    public static ShxDelegateAdapter.Adapter<? extends RecyclerView.ViewHolder> simpleAdapter(@NonNull View view) {
        return new ShxDelegateAdapter.SimpleViewAdapter(view);
    }

    /**
     * Return an adapter that only contains on item and using given layoutHelper
     *
     * @param view         the only view, no binding is required
     * @param layoutHelper layoutHelper that adapter used
     * @return adapter
     */
    public static ShxDelegateAdapter.Adapter<? extends RecyclerView.ViewHolder> simpleAdapter(@NonNull View view, @NonNull LayoutHelper layoutHelper) {
        return new ShxDelegateAdapter.SimpleViewAdapter(view, layoutHelper);
    }


    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View view) {
            super(view);
        }
    }

    static class SimpleViewAdapter extends ShxDelegateAdapter.Adapter<RecyclerView.ViewHolder> {

        private View mView;

        private LayoutHelper mLayoutHelper;

        public SimpleViewAdapter(@NonNull View view, @NonNull LayoutHelper layoutHelper) {
            this.mView = view;
            this.mLayoutHelper = layoutHelper;
        }

        public SimpleViewAdapter(@NonNull View view) {
            this(view, new SingleLayoutHelper());
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShxDelegateAdapter.SimpleViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }


    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
        public abstract LayoutHelper onCreateLayoutHelper();

        protected void onBindViewHolderWithOffset(VH holder, int position, int offsetTotal) {

        }

        protected void onBindViewHolderWithOffset(VH holder, int position, int offsetTotal, List<Object> payloads) {
            onBindViewHolderWithOffset(holder, position, offsetTotal);
        }
    }

    public List<Pair<ShxDelegateAdapter.AdapterDataObserver, ShxDelegateAdapter.Adapter>> getmAdapters(){
        return mAdapters;
    }

}