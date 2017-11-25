package com.coral.mobile.live;

import android.os.Handler;

import com.coral.mobile.live.activity.MainActivity;
import com.coral.mobile.live.activity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.toHere(SplashActivity.this);
                finish();
            }
        }, 1000);
    }
}
