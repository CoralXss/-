package com.coral.mobile.live.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.coral.mobile.live.R;
import com.coral.mobile.live.adapter.base.BaseRecyclerAdapter;
import com.coral.mobile.live.adapter.LiveListAdapter;
import com.coral.mobile.live.entity.DataTest;
import com.coral.mobile.live.entity.LiveEntity;
import com.coral.mobile.live.fragment.base.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2017/5/31.
 */

public class MainFragment extends BaseListFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public int getPageSize() {
        return 9;
    }

    @Override
    public BaseRecyclerAdapter constructAdapter() {
        return new LiveListAdapter();
    }

    @Override
    public void initView() {
        super.initView();

        setRecyclerLayoutManager(LAYOUT_MANAGER_GRID, 2);
        setHasHeaderView(true);
        setHasFooterView(false);
        setHasLoadMoreView(true);
    }

    private View headerView;
    private void addHeader() {
        if (headerView == null) {
            headerView = View.inflate(getActivity(), R.layout.adapter_header_live_list, null);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "点击头部", Toast.LENGTH_SHORT).show();
                }
            });
            addHeaderView(headerView);
        }
    }

    @Override
    public void toLoadData(final int pageIndex) {
        // showLoadingView();  根据请求结果显示不同的页面 - emptyView loadingView contentView failedView

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<LiveEntity> list = new ArrayList<>();
                if (pageIndex == 0) {
                    LiveEntity entity = new LiveEntity();
                    entity.channelId = 1;
                    entity.roomName = "碧桂园森林城市-深圳";
                    entity.status = 1;
                    list.add(entity);
                }

                list.addAll(DataTest.getLiveList(pageIndex, getPageSize()));

                if ((list == null || list.isEmpty()) && pageIndex == 0) {
                    showEmptyView();
                } else {
                    showContentView();
                    addHeader();
                    notifyDataSetChanged(list);
                }

                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
}
