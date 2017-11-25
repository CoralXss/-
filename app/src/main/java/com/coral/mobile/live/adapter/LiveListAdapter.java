package com.coral.mobile.live.adapter;

import com.coral.mobile.live.R;
import com.coral.mobile.live.adapter.base.BaseRecyclerAdapter;
import com.coral.mobile.live.adapter.base.BaseViewHolder;
import com.coral.mobile.live.entity.LiveEntity;

/**
 * Created by xss on 2017/5/31.
 */

public class LiveListAdapter extends BaseRecyclerAdapter<LiveEntity, BaseViewHolder> {

    public LiveListAdapter() {
        super(R.layout.adapter_item_live_list, null);
    }

    @Override
    public void bindViewHolder(BaseViewHolder holder, int position, LiveEntity item) {
        holder.setText(R.id.tv_status, item.getStatusString(item.status));
        holder.setText(R.id.tv_property, item.roomName);
    }
}
