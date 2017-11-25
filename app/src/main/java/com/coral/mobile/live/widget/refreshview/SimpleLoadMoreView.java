package com.coral.mobile.live.widget.refreshview;

import com.coral.mobile.live.R;

/**
 * Created by xss on 2017/6/1.
 */

public class SimpleLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.adapter_load_more_view;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    public int getLoadFailedViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    public int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
