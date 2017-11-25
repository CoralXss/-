package com.coral.mobile.live.widget;

import android.view.View;

/**
 * Created by xss on 2017/6/5.
 * desc: prevent two operations, such as showing dialog twice from sequent double click
 */

public abstract class OnClickDoubleListener implements View.OnClickListener {

    private static final long TIME_SPAN_MIN = 400l;
    private long preClickTime = 0;

    private View targetView = null;

    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();

        long timeSpan = time - preClickTime;

        if (timeSpan > TIME_SPAN_MIN || targetView != v) {
            onClickDouble(v);
        }
        preClickTime = time;
        targetView = v;
    }

    public abstract void onClickDouble(View v);

}
