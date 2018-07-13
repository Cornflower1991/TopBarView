package com.yexiuliang.topbarview;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yexiuliang.library.BaseTopBarAdapter;

/**
 * Description: TODO
 *
 * @author yexiuliang on 2018/7/10
 */
public class BaseTopBarCenterTextAdapter extends BaseTopBarAdapter {

    protected String centerTextStr = "";
    private TextView mTextView;

    public BaseTopBarCenterTextAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getLeftView(ViewGroup parent) {
        return LayoutInflater.from(mActivity).inflate(R.layout.left, parent, false);
    }

    @Override
    public View getCenterView(ViewGroup parent) {
        mTextView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.center, parent, false);
        if (!TextUtils.isEmpty(centerTextStr)) {
            mTextView.setText(centerTextStr);
        } else {
            CharSequence title = mActivity.getTitle();
            centerTextStr = title == null ? "" : title.toString();
            if (!TextUtils.isEmpty(title)) {
                mTextView.setText(title);
            }
        }
        return mTextView;
    }

    @Override
    public View getRightView(ViewGroup parent) {
        return null;
    }

    public void setCenterTextStr(String centerTextStr) {
        this.centerTextStr = centerTextStr;
        mTextView.setText(centerTextStr);
//        notifyItemChanged(TopBarView.CENTER_VIEW_INDEX);
    }
}
