package com.coral.mobile.live.fragment.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;

import com.coral.mobile.live.R;
import com.coral.mobile.live.adapter.base.BaseRecyclerAdapter;
import com.coral.mobile.live.widget.LoadingView;
import com.coral.mobile.live.widget.refreshview.RefreshRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xss on 2017/4/7.
 */

public abstract class BaseListFragment<T extends BaseRecyclerAdapter> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RefreshRecyclerView.OnLoadMoreListener {
    private static final String TAG = BaseListFragment.class.getSimpleName();

    // 布局管理器类别
    protected static final byte LAYOUT_MANAGER_LINEAR = 00;
    protected static final byte LAYOUT_MANAGER_GRID = 01;
    protected static final byte LAYOUT_MANAGER_STAGGERED = 10;

    @BindView(R.id.rv_recycler_list)
    protected RefreshRecyclerView rv_recycler_list;
    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout swipe_refresh_layout;

    @BindView(R.id.view_loading)
    protected LoadingView view_loading;
    @BindView(R.id.view_stub_no_data)
    protected ViewStub view_stub_no_data;
    @BindView(R.id.view_stub_load_failed)
    protected ViewStub view_stub_load_failed;

    /** pageIndex = 0 算作第一页，若从1开始，需要改动 */
    protected int pageIndex;
    protected BaseRecyclerAdapter listAdapter;

    protected boolean isFirstStartPage = true;

    public int getPageSize() {
        return 10;
    }

    @Override
    public void initView() {
        super.initView();

        setSwipeRefreshColors(getResources().getColor(R.color.color_circle_progress), getResources().getColor(R.color.color_white));
        setSwipeRefresh(true);
        setRecyclerLayoutManager(LAYOUT_MANAGER_LINEAR, 0);
        // 默认初始化 Adapter
        setRecyclerAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstStartPage) {
            showLoadingView();
            toLoadData(0);
            isFirstStartPage = false;
        }
    }

    private void setRecyclerAdapter() {
        if (listAdapter == null) {
            listAdapter = constructAdapter();
        }
    }

    /**
     * 设置加载框颜色
     * @param schemeColors
     * @param progressBgColor
     */
    private void setSwipeRefreshColors(int schemeColors, int progressBgColor) {
        if (swipe_refresh_layout != null) {
            // 设置旋转箭头的颜色
            swipe_refresh_layout.setColorSchemeColors(schemeColors);
            // 设置进度条背景色
            swipe_refresh_layout.setProgressBackgroundColorSchemeColor(progressBgColor);
        }
    }

    /**
     * 设置布局管理器
     * @param type
     * @param span
     */
    protected void setRecyclerLayoutManager(int type, int span) {
        switch (type) {
            case LAYOUT_MANAGER_GRID:
                rv_recycler_list.setLayoutManager(new GridLayoutManager(getActivity(), span));
                break;
            case LAYOUT_MANAGER_STAGGERED:
                rv_recycler_list.setLayoutManager(new StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL));
                break;
            case LAYOUT_MANAGER_LINEAR:
            default:
                rv_recycler_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
        }
    }

    /**
     * 设置是否允许下拉刷新
     * @param refresh
     */
    public void setSwipeRefresh(boolean refresh) {
        if (swipe_refresh_layout != null) {
            // 设置是显示还是隐藏刷新进度条
            swipe_refresh_layout.setEnabled(refresh);
            if (refresh) {
                swipe_refresh_layout.setOnRefreshListener(this);
            }
        }
    }

    /**
     * 设置是否允许下拉加载
     * @param hasLoadMoreView
     */
    public void setHasLoadMoreView(boolean hasLoadMoreView) {
        if (listAdapter != null) {
            listAdapter.setLoadMoreEnabled(hasLoadMoreView);
        }
    }

    /**
     * 设置是否添加 HeaderView
     * @param hasHeaderView
     */
    public void setHasHeaderView(boolean hasHeaderView) {
        if (listAdapter != null) {
            listAdapter.setHasHeaderView(hasHeaderView);
        }
    }

    /**
     * 设置是否添加 FooterView
     * @param hasFooterView
     */
    public void setHasFooterView(boolean hasFooterView) {
        if (listAdapter != null) {
            listAdapter.setHasFooterView(hasFooterView);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        toLoadData(pageIndex);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        toLoadData(pageIndex);
    }

    public abstract void toLoadData(int pageIndex);

    public abstract T constructAdapter();

    /**
     * 更新列表数据
     * @param datas not null or empty
     */
    public void notifyDataSetChanged(List datas) {
        swipe_refresh_layout.setRefreshing(false);
        setRecyclerAdapter();

        // 支持加载更多，才添加滑动监听 (不支持加载更多的情况 = 一次性加载所有数据，pageSize = MAX_INTEGER)
        if (listAdapter.getLoadMoreEnabled()) {
            rv_recycler_list.setOnLoadMoreListener(this);
        }

        if (pageIndex == 0) {
            // 下拉刷新
            listAdapter.setLoadMoreEndGone(false);
            listAdapter.setData(datas);
            // 设置完数据后，再给 recyclerView设置adapter才能使headerView占满一行
            rv_recycler_list.setAdapter(listAdapter);
        } else {
            if (listAdapter.getLoadMoreEnabled()) {
                // 加载更多
                listAdapter.addData(datas);
                if (datas.size() == getPageSize()) {
                    // 加载完成
                    listAdapter.loadMoreComplete();
                } else {
                    // 没有更多数据
                    listAdapter.loadMoreEnd();
                }
            }
        }
    }

    /**
     * 添加 HeaderView
     * @param headerView
     */
    protected void addHeaderView(View headerView) {
        if (listAdapter != null) {
            listAdapter.addHeaderView(headerView);
        }
    }

    /**
     * 添加 FooterView
     * @param headerView
     */
    protected void addFooterView(View headerView) {
        if (listAdapter != null) {
            listAdapter.addFooterView(headerView);
        }
    }

    public void showLoadingView() {
        view_loading.setVisibility(View.VISIBLE);
        swipe_refresh_layout.setVisibility(View.GONE);
        view_stub_no_data.setVisibility(View.GONE);
        view_stub_load_failed.setVisibility(View.GONE);
    }

    /**
     * 显示列表内容
     */
    public void showContentView() {
        view_loading.setVisibility(View.GONE);
        if (swipe_refresh_layout != null) {
            swipe_refresh_layout.setVisibility(View.VISIBLE);
        }
        if (view_stub_no_data != null) {
            view_stub_no_data.setVisibility(View.GONE);
        }
        if (view_stub_load_failed != null) {
            view_stub_load_failed.setVisibility(View.GONE);
        }
    }

    /**
     * 显示 EmptyView
     */
    public void showEmptyView() {
        view_loading.setVisibility(View.GONE);
        if (swipe_refresh_layout != null) {
            rv_recycler_list.setVisibility(View.GONE);
        }
        if (view_stub_no_data != null) {
            view_stub_no_data.setVisibility(View.VISIBLE);
        }
        if (view_stub_load_failed != null) {
            view_stub_load_failed.setVisibility(View.GONE);
        }
    }
}
