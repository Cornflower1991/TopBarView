package com.yexiuliang.library;

/**
 * Description: TopBar观察者
 *
 * @author yexiuliang on 2018/7/9
 */
public interface TopBarObserver {
    /**
     * 有变化的条目,全部为-1
     *
     * @param index 下标数,从0开始
     */
    void onChanged(int index);

    /**
     * 需要重绘的条目,全部为-1
     *
     * @param index 下标数,从0开始
     */
    void onInvalidated(int index);
}
