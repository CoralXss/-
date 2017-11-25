package com.coral.mobile.live.widget.refreshview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.coral.mobile.live.adapter.base.BaseRecyclerAdapter;

/**
 * Created by xss on 2017/6/2.
 */

public class RefreshRecyclerView extends RecyclerView {
    private static final String TAG = RefreshRecyclerView.class.getSimpleName();

    private int mLastVisibleItem;

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setRecyclerScrollListener();
    }

    /**
     * 设置滑动监听，显示加载更多 (这里可以自定义一个 RecyclerView ，添加滑动监听事件，然后将加载更多接口暴露出去)
     */
    private void setRecyclerScrollListener() {
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                final BaseRecyclerAdapter listAdapter = (BaseRecyclerAdapter) recyclerView.getAdapter();

//                Log.e(TAG, "scroll " + newState + ", " + mLastVisibleItem + ", " + recyclerView.getLayoutManager().getItemCount());

                // 加载更多策略一：监听列表滑动到最后一个可见的item  策略二：当 adapter.getItemCount() > pageSize，就显示加载更多
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == recyclerView.getLayoutManager().getItemCount()
                        && listAdapter != null && !listAdapter.isLoadMoreEnd() && !listAdapter.isLoading()) {
                    // 加载条件：还有更多数据 + 当前不是正在加载状态
                    listAdapter.setLoadMoreEnabled(true);
                    listAdapter.loadMoreBegin();

                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    mLastVisibleItem = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof GridLayoutManager) {
                    mLastVisibleItem = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    mLastVisibleItem = findMax(into);
                }
            }
        });
    }

    /**
     * 获取数组中的最大值
     *
     * @param lastPositions 需要找到最大值的数组
     * @return 数组中的最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
