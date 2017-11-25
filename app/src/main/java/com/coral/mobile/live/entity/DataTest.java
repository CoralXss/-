package com.coral.mobile.live.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2017/5/31.
 */

public class DataTest {

    public static List<LiveEntity> getLiveList(int pageIndex, int pageSize) {
        List<LiveEntity> list = new ArrayList<>();

        for (int i = 0; i < (pageIndex == 3 ? 3 : pageSize); i++) {
            int index = pageIndex * pageSize + i + 1;

            LiveEntity entity = new LiveEntity();
            entity.channelId = index;
            entity.roomName = "碧桂园森林城市-" + index;
            entity.status = i % 3;
            list.add(entity);
        }
        Log.e("list", pageIndex + ", " + list.size());

        return list;
    }
}
