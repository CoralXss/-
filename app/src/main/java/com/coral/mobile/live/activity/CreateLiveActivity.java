package com.coral.mobile.live.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.coral.mobile.live.R;
import com.coral.mobile.live.activity.base.BaseActivity;
import com.coral.mobile.live.utils.ViewUtils;

import butterknife.BindView;

public class CreateLiveActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_close)
    protected ImageView iv_close;
    @BindView(R.id.btn_create)
    protected Button btn_create;

    public static void toHere(Context context) {
        Intent intent = new Intent(context, CreateLiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_live;
    }

    @Override
    public void initView() {
        super.initView();

        ViewUtils.setBackgroundDrawable(btn_create, 5, 1, "#13B8C5", "#13B8C5");
    }

    @Override
    public void initEvent() {
        super.initEvent();
        iv_close.setOnClickListener(this);
        btn_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_create:

                break;
            default:
                break;
        }
    }
}
