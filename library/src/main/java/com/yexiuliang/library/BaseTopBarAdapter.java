package com.yexiuliang.library;


import android.app.Activity;
import android.database.Observable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: 通用标题栏的Adapter
 *
 * @author yexiuliang on 2018/7/9
 */

public abstract class BaseTopBarAdapter extends Observable<TopBarObserver> {
    /**
     * 依附的Activity
     */
    protected Activity mActivity;

    public BaseTopBarAdapter(Activity activity) {
        mActivity = activity;
    }

    /**
     * 左视图
     */
    public abstract View getLeftView(ViewGroup parent);

    /**
     * 中间视图
     */
    public abstract View getCenterView(ViewGroup parent);

    /**
     * 右视图
     */
    public abstract View getRightView(ViewGroup parent);

    /**
     * 更新全部视图
     */
    public void notifyDataSetChanged() {
        for (TopBarObserver observer : mObservers) {
            observer.onChanged(-1);
        }
    }

    /**
     * 更新单个视图
     *
     * @param index 视图索引
     * @see TopBarView#LEFT_VIEW_INDEX
     * @see TopBarView#CENTER_VIEW_INDEX
     * @see TopBarView#RIGHT_VIEW_INDEX
     */
    public void notifyItemChanged(int index) {
        for (TopBarObserver observer : mObservers) {
            observer.onChanged(index);
        }
    }

    /**
     * 重绘全部视图
     */
    public void notifyDataSetInvalidate() {
        for (TopBarObserver observer : mObservers) {
            observer.onInvalidated(-1);
        }
    }

    /**
     * 重绘单个视图
     *
     * @param index 视图索引
     * @see TopBarView#LEFT_VIEW_INDEX
     * @see TopBarView#CENTER_VIEW_INDEX
     * @see TopBarView#RIGHT_VIEW_INDEX
     */
    public void notifyDataSetInvalidate(int index) {
        for (TopBarObserver observer : mObservers) {
            observer.onInvalidated(-index);
        }
    }

}
