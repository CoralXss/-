package com.coral.mobile.live.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.coral.mobile.live.R;
import com.coral.mobile.live.activity.base.BaseActivity;
import com.coral.mobile.live.widget.OnClickDoubleListener;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_start_play)
    protected ImageView iv_start_play;

    public static void toHere(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        iv_start_play.setOnClickListener(onClickDoubleListener);
    }

    OnClickDoubleListener onClickDoubleListener = new OnClickDoubleListener() {

        @Override
        public void onClickDouble(View v) {
            CreateLiveActivity.toHere(MainActivity.this);
        }
    };
}
