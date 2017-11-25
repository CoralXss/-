package com.coral.mobile.live.adapter.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coral.mobile.live.widget.refreshview.LoadMoreView;
import com.coral.mobile.live.widget.refreshview.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by xss on 2017/5/31.
 */

public abstract class BaseRecyclerAdapter<T, K extends BaseViewHolder> extends RecyclerView.Adapter<K> {
    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();

    protected List<T> mDatas;
    protected int mLayoutId;

    protected Context mContext;
    protected LayoutInflater mInflater;

    /** 加载更多View */
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    /** 是否有加载更多的功能 */
    private boolean mLoadMoreEnable;
    /** 是否正在加载 */
    private boolean mIsLoading;

    private OnLoadMoreListener mOnLoadMoreListener;

    /** 头部\底部view */
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;

    /** 是否有头部view */
    private boolean mHasHeaderView;
    private boolean mHasFooterView;

    /** 标记不同type的view */
    public static final int HEADER_VIEW = 0x00000111;  // 273
    public static final int LOADING_VIEW = 0x00000222; // 546
    public static final int FOOTER_VIEW = 0x00000333;  // 819

    public BaseRecyclerAdapter(int layoutResId) {
        this(layoutResId, null);
    }

    public BaseRecyclerAdapter(List<T> datas) {
        this(0, datas);
    }

    public BaseRecyclerAdapter(int layoutResId, List<T> datas) {
        this.mDatas = (datas == null) ? new ArrayList<T>() : datas;
        this.mLayoutId = layoutResId;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public void setData(List<T> datas) {
        this.mDatas = (datas == null) ? new ArrayList<T>() : datas;
        notifyDataSetChanged();
    }

    public void addData(T data) {
        mDatas.add(data);
        notifyItemInserted(mDatas.size() + getHeaderViewCount());
    }

    public void addData(int position, T data) {
        mDatas.add(position, data);
        notifyItemInserted(position + getHeaderViewCount());
    }

    public void addData(List<T> datas) {
        mDatas.addAll(datas);
        // 刷新 从添加数据位置开始的 datas.size() 个 item
        notifyItemRangeInserted(mDatas.size() - datas.size() + getHeaderViewCount(), datas.size());
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    // 如果是 headerView 或者 footerView 或 loadView 就占满一行
                    return (viewType == HEADER_VIEW || viewType == FOOTER_VIEW || viewType == LOADING_VIEW) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;

        this.mContext = parent.getContext();
        if (mInflater == null) {
            this.mInflater = LayoutInflater.from(mContext);
        }

        Log.e(TAG, viewType + "");

        switch (viewType) {  // 头部 底部 内容
            case HEADER_VIEW:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case FOOTER_VIEW:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            case LOADING_VIEW: // 加载更多
                baseViewHolder = createLoadMoreBaseViewHolder(parent);
                break;
            default:  // 内容
                baseViewHolder = createContentBaseViewHolder(parent, mLayoutId);
                break;
        }

        return baseViewHolder;
    }

    /**
     * 创建非主内容 ViewHolder
     * @param view
     * @return
     */
    protected K createBaseViewHolder(View view) {
        return (K) new BaseViewHolder(view);
    }

    /**
     * 创建主内容 ViewHolder
     * @param parent
     * @param layoutResId
     * @return
     */
    protected K createContentBaseViewHolder(ViewGroup parent, int layoutResId) {
        View contentView = mInflater.inflate(layoutResId, parent, false);
        return createBaseViewHolder(contentView);
    }

    /**
     * 创建加载更多 ViewHolder
     * @param parent
     * @return
     */
    protected K createLoadMoreBaseViewHolder(ViewGroup parent) {
        View loadView = mInflater.inflate(mLoadMoreView.getLayoutId(), parent, false);
        return createBaseViewHolder(loadView);
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:
                bindViewHolder(holder, position, mDatas.get(holder.getLayoutPosition() - getHeaderViewCount()));
                break;
            case LOADING_VIEW:
                mLoadMoreView.bindView(holder);
                break;
            case HEADER_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                bindViewHolder(holder, position, mDatas.get(holder.getLayoutPosition() - getHeaderViewCount()));
                break;
        }
    }

    /**
     * 子类需要实现的方法
     * @param holder
     * @param position
     * @param item
     */
    public abstract void bindViewHolder(K holder, int position, T item);

    @Override
    public int getItemViewType(int position) {

        int headerCount = getHeaderViewCount();
        // 若添加了头部，则第一个item为headerView
        if (position < headerCount) {
            return HEADER_VIEW;
        } else {
            int count = position - headerCount;
            int itemCount = mDatas.size();
            // 如果当前item所在位置 < 内容item个数，则为列表itemView
            if (count < itemCount) {
                return getContentItemViewType(position);
            } else {
                count = count - itemCount;
                if (count < getFooterViewCount()) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    /**
     * 获取主内容的 item view 布局（当内容有不同布局时，可重写此方法）
     * @param position
     * @return
     */
    public int getContentItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        // 返回item view个数
        return getHeaderViewCount() + mDatas.size() + getFooterViewCount() + getLoadMoreViewCount();
    }

    public int getLoadMoreViewCount() {
        if (!mLoadMoreEnable || mDatas.isEmpty()) { // || mLoadMoreView.isLoadMoreEndGone()   !mLoadMoreEnable ||
            return 0;
        }
        return 1;
    }

    /**
     * 获取头部View个数，添加返回1，但是可以在 mHeaderLayout线性布局中动态添加子View
     * @return
     */
    public int getHeaderViewCount() {
        return (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) ? 0 : 1;
    }

    /**
     * 获取底部View个数，添加返回1，但是可以在 mFooterLayout线性布局中动态添加子View
     * @return
     */
    public int getFooterViewCount() {
        return (mFooterLayout == null || mFooterLayout.getChildCount() == 0) ? 0 : 1;
    }

    // 加载更多的 View Count 还需计算在内 ......

    public T getItem(int position) {
        if (position != -1) {
            return mDatas.get(position);
        }
        return null;
    }

    public void addHeaderView(View headerView) {
        addHeaderView(headerView, LinearLayout.VERTICAL);
    }

    /**
     * 添加headerView (对外调用方法)
     * @param headerView
     * @param orientation
     */
    public void addHeaderView(View headerView, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(headerView.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        mHeaderLayout.addView(headerView);
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                // 刷新列表
                notifyItemInserted(position);
            }
        }
    }

    /**
     * 移除 HeaderView 中的子View
     * @param headerView
     */
    public void removeHeaderView(View headerView) {
        if (getHeaderViewCount() == 0) {
            return;
        }
        mHeaderLayout.removeView(headerView);
        // 当 headerLayout 中没有 子View 时，就刷新列表
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                // 刷新列表
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * 获取 headerView 所在位置
     * @return
     */
    public int getHeaderViewPosition() {
        return mHasHeaderView ? 0 : -1;
    }

    public void addFooterView(View footerView) {
        addFooterView(footerView, LinearLayout.VERTICAL);
    }

    /**
     * 添加footerView (对外调用方法)
     * @param footerView
     * @param orientation
     */
    public void addFooterView(View footerView, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footerView.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        mFooterLayout.addView(footerView);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                // 刷新列表
                notifyItemInserted(position);
            }
        }
    }

    /**
     * 移除 FooterView 中的子View
     * @param footerView
     */
    public void removeFooterView(View footerView) {
        if (getFooterViewCount() == 0) {
            return;
        }
        mFooterLayout.removeView(footerView);
        // 当 mFooterLayout 中没有 子View 时，就刷新列表
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                // 刷新列表
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * 设置是否添加 HeaderView
     * @param hasHeaderView
     */
    public void setHasHeaderView(boolean hasHeaderView) {
        mHasHeaderView = hasHeaderView;
    }

    /**
     * 设置是否添加 FooterView
     * @param hasFooterView
     */
    public void setHasFooterView(boolean hasFooterView) {
        mHasFooterView = hasFooterView;
    }

    /**
     * 获取 footerView 所在位置
     * @return
     */
    public int getFooterViewPosition() {
        return mHasFooterView ? getHeaderViewCount() + mDatas.size() : -1;
    }

    /**
     * 设置是否需要加载更多
     * @param isEnable
     */
    public void setLoadMoreEnabled(boolean isEnable) {
        mLoadMoreEnable = isEnable;
    }

    public boolean getLoadMoreEnabled() {
        return mLoadMoreEnable;
    }

    /**
     * 当前是否正在加载
     * @return
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * 设置是否还能加载更多数据
     * @param visibility
     */
    public void setLoadMoreEndGone(boolean visibility) {
        mLoadMoreView.setLoadMoreEndGone(visibility);
    }

    public boolean isLoadMoreEnd() {
        return mLoadMoreView.isLoadMoreEndGone();
    }

    /**
     * 加载更多-正在加载
     */
    public void loadMoreBegin() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mIsLoading = true;
        // 加载开始
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        notifyItemChanged(getItemCount() - getLoadMoreViewCount());
    }

    /**
     * 加载更多-完成
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mIsLoading = false;
        // 加载完成，隐藏加载更多布局
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getItemCount() - getLoadMoreViewCount());
    }

    /**
     * 加载更多-所有数据加载完成
     */
    public void loadMoreEnd() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mIsLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        mLoadMoreView.setLoadMoreEndGone(true);
        notifyItemChanged(getItemCount() - getLoadMoreViewCount());
    }

    /**
     * 加载更多-失败
     */
    public void loadMoreFailed() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        // 加载失败
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getItemCount() - getLoadMoreViewCount());
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
