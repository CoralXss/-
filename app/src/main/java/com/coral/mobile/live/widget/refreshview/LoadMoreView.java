package com.coral.mobile.live.widget.refreshview;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.coral.mobile.live.adapter.base.BaseViewHolder;

/**
 * Created by xss on 2017/6/1.
 */

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    /**
     * 设置当前加载状态
     * @param status
     */
    public void setLoadMoreStatus(int status) {
        this.mLoadMoreStatus = status;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    /**
     * 设置加载更多可见性
     * @param gone
     */
    public void setLoadMoreEndGone(boolean gone) {
        this.mLoadMoreEndGone = gone;
    }

    public boolean isLoadMoreEndGone() {
        // 没有设置 加载更多
        if (getLayoutId() == 0) {
            return true;
        }

        return mLoadMoreEndGone;
    }

    /**
     * 不同加载状态，显示不同布局
     * @param holder
     */
    public void bindView(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                setLoadMoreViewVisibility(holder, true, false, false);
                break;
            case STATUS_END:
                setLoadMoreViewVisibility(holder, false, true, false);
                break;
            case STATUS_FAIL:
                setLoadMoreViewVisibility(holder, false, false, true);
                break;
            case STATUS_DEFAULT:
            default:
                setLoadMoreViewVisibility(holder, false, false, false);
                break;
        }
    }

    /**
     * 设置 加载更多 View 显示隐藏状态
     * @param holder
     * @param isLoading
     * @param loadEnd
     * @param loadFailed
     */
    private void setLoadMoreViewVisibility(BaseViewHolder holder, boolean isLoading, boolean loadEnd, boolean loadFailed) {
        holder.setVisible(getLoadingViewId(), isLoading);
        holder.setVisible(getLoadEndViewId(), loadEnd);
        holder.setVisible(getLoadFailedViewId(), loadFailed);
    }

    /**
     * 布局layout ID
     * @return
     */
    public abstract @LayoutRes int getLayoutId();

    /**
     * loading view id
     * @return
     */
    public abstract @IdRes int getLoadingViewId();

    /**
     * 加载失败 view id
     * @return
     */
    public abstract @IdRes int getLoadFailedViewId();

    /**
     * 加载完成 view id
     * @return
     */
    public abstract @IdRes int getLoadEndViewId();

}
