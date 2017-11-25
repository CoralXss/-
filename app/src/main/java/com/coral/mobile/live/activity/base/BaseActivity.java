package com.coral.mobile.live.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by xss on 2017/3/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        // 绑定 Activity
        ButterKnife.bind(this);

        initExtra();
        beforeInitView();
        initView();
        initEvent();
        afterView();
    }

    public abstract int getLayoutId();

    public void beforeInitView() {}

    public void initExtra() {}

    public void initView() {}

    public void initEvent() {}

    public void afterView() {}

}
