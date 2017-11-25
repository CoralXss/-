package com.coral.mobile.live.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xss on 2017/5/31.
 */

public class LiveEntity implements Serializable {

    /**直播间ID*/
    public long roomId;
    /**直播间名称*/
    public String roomName;
    /**频道ID*/
    public int channelId;
    /**频道名称*/
    public String channelName;
    /**栏目ID*/
    public int columnId;
    /**栏目名称*/
    public String columnName;
    /**直播流ID*/
    public long streamId;
    /**流key*/
    public String streamKey;
    /**直播状态：0-待直播,  1-直播中, 2-直播结束, 3-禁播*/
    public int status;
    /**推送流url*/
    public String publishUrl;

    /** 播放流 url列表*/
    public List<LiveTypeEntity> playUrls;

    /**回放地址*/
    public String playBackUrl;
    /**播放次数*/
    public int playCount;
    /**im相关会话标识*/
    public String imConversationId;
    /**预约时间*/
    public String subscribeTime;
    /**创建时间*/
    public String createTime;
    /**更新时间*/
    public String updateTime;

    public static class LiveTypeEntity implements Serializable {
        /**	播放流url*/
        public String url;
        /** 播放流类型：1-RTMP；2-HDL；3-HLS；4-Snapshot*/
        public int urlType;
    }

    public String getStatusString(int status) {
        switch (status) {
            case 0:
                return "待直播";
            case 1:
                return "直播中";
            case 2:
                return "直播结束";
            case 3:
                return "禁播";
            default:
                return "待直播";
        }
    }

}
