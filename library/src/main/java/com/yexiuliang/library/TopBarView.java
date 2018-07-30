package com.yexiuliang.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Description: 通用标题栏
 *
 * @author yexiuliang on 2018/7/9
 */
public class TopBarView extends RelativeLayout {

    /**
     * 左边视图顺序值
     */
    public static final int LEFT_VIEW_INDEX = 0;
    /**
     * 中间视图顺序值
     */
    public static final int CENTER_VIEW_INDEX = 1;
    /**
     * 右边视图顺序值
     */
    public static final int RIGHT_VIEW_INDEX = 2;

    private final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 观察者
     */
    private final TopBarViewDataObserver mObserver = new TopBarViewDataObserver();

    /**
     * 分隔线视图
     */
    private View mBottomLine;
    /**
     * 底部阴影
     */
    private View mBottomShadow;
    /**
     * 容器
     */
    private RelativeLayout mContainer;
    /**
     * 左边的视图
     */
    private View mLeftView = null;
    /**
     * 中间的视图
     */
    private View mCenterView = null;
    /**
     * 右边的视图
     */
    private View mRightView = null;
    /**
     * 左边视图适配器点击缓存
     */
    private View.OnClickListener mOnLeftClickHolder = null;
    /**
     * 中间视图适配器点击缓存
     */
    private View.OnClickListener mOnCenterClickHolder = null;
    /**
     * 右边视图适配器点击缓存
     */
    private View.OnClickListener mOnRightClickHolder = null;

    /**
     * 标题栏背景颜色
     */
    private int mTopBarColor;
    /**
     * 标题栏高度
     */
    private int mTopBarHeight;
    /**
     * 是否显示底部分割线
     */
    private boolean mShowBottomLine;
    /**
     * 分割线颜色
     */
    private int mBottomLineColor;
    /**
     * 底部阴影高度
     */
    private float mBottomShadowHeight;

    /**
     * 适配器
     */
    private BaseTopBarAdapter mAdapter = null;

    public TopBarView(Context context) {
        super(context);
        init(context, null);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBarView);

        mTopBarColor = typedArray.getColor(R.styleable.TopBarView_topBarView_titleBarColor,
                Color.parseColor("#ffffff"));
        mTopBarHeight = (int) typedArray.getDimension(R.styleable.TopBarView_topBarView_titleBarHeight, dip2px(44));
        mShowBottomLine = typedArray.getBoolean(R.styleable.TopBarView_topBarView_showBottomLine, true);
        mBottomLineColor = typedArray.getColor(R.styleable.TopBarView_topBarView_bottomLineColor,
                Color.parseColor("#dddddd"));
        mBottomShadowHeight = typedArray.getDimension(R.styleable.TopBarView_topBarView_bottomShadowHeight, 0);
        typedArray.recycle();

        ViewGroup.LayoutParams globalParams = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        setLayoutParams(globalParams);
        // 构建主视图
        mContainer = new RelativeLayout(context);
        mContainer.setId(View.generateViewId());
        mContainer.setBackgroundColor(mTopBarColor);
        LayoutParams mainParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // 计算主布局高度
        if (mShowBottomLine) {
            //减去底部边线
            mainParams.height = mTopBarHeight - Math.max(1, dip2px(0.4f));
        } else {
            mainParams.height = mTopBarHeight;
        }
        addView(mContainer, mainParams);

        if (mShowBottomLine) {
            //显示分隔线
            mBottomLine = new View(context);
            mBottomLine.setBackgroundColor(mBottomLineColor);
            LayoutParams bottomLineParams = new LayoutParams(MATCH_PARENT, Math.max(1, dip2px(0.4f)));
            bottomLineParams.addRule(RelativeLayout.BELOW, mContainer.getId());
            addView(mBottomLine, bottomLineParams);
        } else if (mBottomShadowHeight != 0) {
            mBottomShadow = new View(context);
            mBottomShadow.setBackgroundResource(R.drawable.topbar_shadow);
            LayoutParams bottomShadowParams = new LayoutParams(MATCH_PARENT, dip2px(mBottomShadowHeight));
            bottomShadowParams.addRule(RelativeLayout.BELOW, mContainer.getId());
            addView(mBottomShadow, bottomShadowParams);
        }
    }

    private int dip2px(float dip) {
        if (getContext() == null || getResources() == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    /**
     * 初始化主视图
     */
    private void initContentViews() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取adapter
     */
    @Nullable
    public BaseTopBarAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseTopBarAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterObserver(mObserver);
        }
        if (mContainer != null) {
            mContainer.removeAllViewsInLayout();
        }
        mAdapter = adapter;
        if (adapter != null) {
            adapter.registerObserver(mObserver);
            mAdapter = adapter;
            initContentViews();
        }
    }

    private LayoutParams getChildParams(int index) {
        LayoutParams relParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        switch (index) {
            case LEFT_VIEW_INDEX:
                relParams.addRule(ALIGN_PARENT_LEFT);
                relParams.addRule(CENTER_VERTICAL);
                break;
            case CENTER_VIEW_INDEX:
                relParams.addRule(CENTER_IN_PARENT);
                break;
            case RIGHT_VIEW_INDEX:
                relParams.addRule(ALIGN_PARENT_RIGHT);
                relParams.addRule(CENTER_VERTICAL);
                break;

            default:
                break;
        }
        return relParams;
    }

    public void setOnLeftClickListener(View.OnClickListener listener) {
        this.mOnLeftClickHolder = listener;
        //防止设置点击事件顺序的问题
        if (mLeftView != null) {
            mLeftView.setOnClickListener(listener);
        }
    }

    public void setOnCenterClickListener(View.OnClickListener listener) {
        this.mOnCenterClickHolder = listener;
        //防止设置点击事件顺序的问题
        if (mCenterView != null) {
            mCenterView.setOnClickListener(listener);
        }
    }

    public void setOnRightClickListener(View.OnClickListener listener) {
        this.mOnRightClickHolder = listener;
        //防止设置点击事件顺序的问题
        if (mRightView != null) {
            mRightView.setOnClickListener(listener);
        }
    }

    private class TopBarViewDataObserver implements TopBarObserver {
        public TopBarViewDataObserver() {
        }

        @Override
        public void onChanged(int index) {
            if (mAdapter == null) { return; }
            if (mContainer.getChildCount() > 3) {
                mContainer.removeAllViewsInLayout();
                mLeftView = null;
                mCenterView = null;
                mRightView = null;
                index = -1;
            }
            View leftTemp = null;
            View centerTemp = null;
            View rightTemp = null;
            switch (index) {
                case LEFT_VIEW_INDEX: {
                    leftTemp = mAdapter.getLeftView(mContainer);
                    break;
                }
                case CENTER_VIEW_INDEX: {
                    centerTemp = mAdapter.getCenterView(mContainer);
                    break;
                }
                case RIGHT_VIEW_INDEX: {
                    rightTemp = mAdapter.getRightView(mContainer);
                    break;
                }
                default: {
                    leftTemp = mAdapter.getLeftView(mContainer);
                    centerTemp = mAdapter.getCenterView(mContainer);
                    rightTemp = mAdapter.getRightView(mContainer);
                    break;
                }
            }

            if (mLeftView != null && leftTemp != null) {
                mContainer.removeView(mLeftView);
                mLeftView = null;
            }

            if (leftTemp != null) {
                mLeftView = leftTemp;
                if (mLeftView.getId() == View.NO_ID) {
                    mLeftView.setId(View.generateViewId());
                }
                mContainer.addView(mLeftView, getChildParams(LEFT_VIEW_INDEX));
            }

            if (mRightView != null && rightTemp != null) {
                mContainer.removeView(mRightView);
                mRightView = null;
            }

            if (rightTemp != null) {
                mRightView = rightTemp;
                if (mRightView.getId() == View.NO_ID) {
                    mRightView.setId(View.generateViewId());
                }
                mContainer.addView(mRightView, getChildParams(RIGHT_VIEW_INDEX));
            }

            if (mCenterView != null && centerTemp != null) {
                mContainer.removeView(mCenterView);
                mCenterView = null;
            }

            if (centerTemp != null) {
                mCenterView = centerTemp;
                if (mCenterView.getId() == View.NO_ID) {
                    mCenterView.setId(View.generateViewId());
                }
                mContainer.addView(mCenterView, getChildParams(CENTER_VIEW_INDEX));
            }
            coverClickListener();
        }

        @Override
        public void onInvalidated(int index) {
            switch (index) {
                case LEFT_VIEW_INDEX: {
                    if (mLeftView != null) {
                        mLeftView.invalidate();
                    }
                    break;
                }
                case CENTER_VIEW_INDEX: {
                    if (mCenterView != null) {
                        mCenterView.invalidate();
                    }
                    break;
                }
                case RIGHT_VIEW_INDEX: {
                    if (mRightView != null) {
                        mRightView.invalidate();
                    }
                    break;
                }
                default: {
                    if (mLeftView != null) {
                        mLeftView.invalidate();
                    }
                    if (mCenterView != null) {
                        mCenterView.invalidate();
                    }
                    if (mRightView != null) {
                        mRightView.invalidate();
                    }
                    break;
                }
            }
            TopBarView.this.invalidate();
        }

        private void coverClickListener() {
            if (mOnLeftClickHolder != null && mLeftView != null) {
                mLeftView.setOnClickListener(mOnLeftClickHolder);
            }
            if (mOnCenterClickHolder != null && mRightView != null) {
                mRightView.setOnClickListener(mOnCenterClickHolder);
            }
            if (mOnRightClickHolder != null && mCenterView != null) {
                mCenterView.setOnClickListener(mOnRightClickHolder);
            }
        }
    }
}
