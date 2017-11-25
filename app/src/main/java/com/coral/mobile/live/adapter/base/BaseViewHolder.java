package com.coral.mobile.live.adapter.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xss on 2017/5/31.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    // 存储一个 list item 布局中所有的控件，不必每次都去 findViewById
    private SparseArray<View> itemViews;
    public View convertView;

    public BaseViewHolder(View itemView) {
        super(itemView);

        convertView = itemView;
        itemViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = itemViews.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            itemViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId,@DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public BaseViewHolder setBackgroundRes(int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }


}
