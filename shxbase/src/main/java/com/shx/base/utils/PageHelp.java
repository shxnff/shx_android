package com.shx.base.utils;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shx.base.interfaces.RefreshState;

public class PageHelp {

    // 服务器数据的第几页
    private int currentPage = 1;
    // 一共多少页
    private int recordPage = 0;

    //刷新控件
    SmartRefreshLayout refreshLayout;
    RefreshState mRefreshState;
    RecyclerView mRecyclerView;
    //是否正在加载数据
    private boolean isLoadingData = false;

    public PageHelp(SmartRefreshLayout refreshLayout, RefreshState mRefreshState) {
        this.refreshLayout = refreshLayout;
        this.mRefreshState = mRefreshState;
        init();
    }

    public PageHelp(SmartRefreshLayout refreshLayout, RefreshState mRefreshState, RecyclerView mRecyclerView) {
        this.refreshLayout = refreshLayout;
        this.mRefreshState = mRefreshState;
        this.mRecyclerView = mRecyclerView;
        init();
    }

    private void init() {
        /**
         * 监听刷新与上拉加载事件
         */
        if (this.refreshLayout != null && mRefreshState != null) {
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                    clearPage();
                    mRefreshState.onRefresh(1);
                }

                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                    if (getNextPage() > recordPage) {
                        noMore();
                        finishLoadMore(100);
                        return;
                    }

                    mRefreshState.onLoadMore(getNextPage());
                }
            });
        }

        /**
         * 未到底部，静默加载
         */
//        if (mRecyclerView != null && mRefreshState != null) {
//            mRecyclerView.setOnScrollListener(new OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (refreshLayout != null && isHasNext() && !isLoadingData) {
//                        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
//
//                        //获取最后一个可见view的位置
//                        int lastVisibleItemPosition = 0;
//                        if (layoutManager instanceof LinearLayoutManager) {
//                            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                        } else if (layoutManager instanceof GridLayoutManager) {
//                            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
//                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                            int[] into = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
//                            lastVisibleItemPosition = findMax(into);
//                        }
//
//                        //获取所有的条目数
//                        int itemAll = layoutManager.getItemCount();
//                        //获取可视的条目数
//                        int canSee = layoutManager.getChildCount();
//
//                        if (itemAll > 0 && canSee > 0 && lastVisibleItemPosition > 0 && (itemAll - lastVisibleItemPosition) <= canSee) {
//                            System.out.println("/////onScrolled" + getNextPage());
//                            mRefreshState.onLoadMore(getNextPage());
//                        }
//                    }
//                }
//            });
//        }
    }

    //获取NEXT页面
    public int getNextPage() {
        return currentPage + 1;
    }

    //是否正在加载数据中
    public void setIsLoadingData(boolean isLoadingData) {
        this.isLoadingData = isLoadingData;
    }

    //把数据清空 恢复到开始时的状态
    public void clearPage() {
        this.currentPage = 1;
        this.recordPage = 0;
    }

    //判断是否有下一页数据
    public boolean isHasNext() {
        if (recordPage > 0 && currentPage < recordPage) {
            return true;
        }
        return false;
    }


    //设置当前页信息,总页数
    public void setCurrentPage(int currentPage, int recordPage) {
        this.currentPage = currentPage;
        this.recordPage = recordPage;
        if (isHasNext()) {
            //恢复上拉状态
            if (refreshLayout != null) {
                refreshLayout.setNoMoreData(false);
                refreshLayout.setEnableLoadMore(true);
            }
        } else {
            noMore();
        }
    }

    //设置之后，将不会再触发上拉加载事件
    private void noMore() {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.setEnableLoadMore(false);
        }
    }

    //结束刷新
    public void finishRefresh() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }

    public void finishRefresh(int time) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh(time);
        }
    }

    //结束加载
    public void finishLoadMore() {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore();
        }
    }

    public void finishLoadMore(int time) {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore(time);
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
