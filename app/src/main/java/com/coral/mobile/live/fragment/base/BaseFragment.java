package com.coral.mobile.live.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by xss on 2017/4/7.
 */

public abstract class BaseFragment extends Fragment {

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, contentView);

        beforeInitView();
        initExtra();
        initView();
        initEvent();
        afterView();

        return contentView;
    }

    public abstract int getLayoutId();

    public void beforeInitView() {}

    public void initExtra() {}

    public void initView() {}

    public void initEvent() {}

    public void afterView() {}
}
