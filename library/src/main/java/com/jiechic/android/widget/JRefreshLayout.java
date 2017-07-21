package com.jiechic.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jiechic on 2017/7/21.
 */

public class JRefreshLayout extends ViewGroup {

    private final int mTouchSlop;

    // status enum
    public final static byte STATUS_INIT = 1;
    public final static byte STATUS_PREPARE = 2;
    public final static byte STATUS_LOADING = 3;
    public final static byte STATUS_COMPLETE = 4;

    private byte mStatus = STATUS_INIT;


    private JRefreshHandler mJRefreshHandler;

    private View mContent;
    private View mHeaderView;

    //control status
    private boolean isTouch;

    public JRefreshLayout(Context context) {
        this(context, null);
    }

    public JRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        init(context, attrs, defStyleAttr);

    }

    public JRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    public void setJRefreshHandler(JRefreshHandler jRefreshHandler) {
        this.mJRefreshHandler = jRefreshHandler;
    }

    public void setJRefreshHandler(JRefreshDefaultHanlder jRefreshHandler) {
        this.mJRefreshHandler = jRefreshHandler;
    }

    public JRefreshHandler getJRefreshHandler() {
        return mJRefreshHandler;
    }

    @Override
    protected void onAttachedToWindow() {
        final int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("JRefreshLayout can only contains 2 children");
        } else if (childCount == 2) {
//            if (mHeaderId != 0 && mHeaderView == null) {
//                mHeaderView = findViewById(mHeaderId);
//            }
//            if (mContainerId != 0 && mContent == null) {
//                mContent = findViewById(mContainerId);
//            }

            // not specify header or content
            if (mContent == null || mHeaderView == null) {
                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof JRefreshUIHandler) {
                    mHeaderView = child1;
                    mContent = child2;
                } else if (child2 instanceof JRefreshUIHandler) {
                    mHeaderView = child2;
                    mContent = child1;
                } else {
                    // both are not specified
                    if (mContent == null && mHeaderView == null) {
                        mHeaderView = child1;
                        mContent = child2;
                    }
                    // only one is specified
                    else {
                        if (mHeaderView == null) {
                            mHeaderView = mContent == child1 ? child2 : child1;
                        } else {
                            mContent = mHeaderView == child1 ? child2 : child1;
                        }
                    }
                }
            }
        } else if (childCount == 1) {
            mContent = getChildAt(0);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in JRefreshLayout is empty. Do you forget to specify its id in xml layout file?");
            mContent = errorView;
            addView(mContent);
        }
        if (mHeaderView != null) {
            mHeaderView.bringToFront();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    //是否需要拦截刷新操作
    volatile boolean isPulling = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mJRefreshHandler.checkCanDoRefresh(this, mContent, mHeaderView)) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            //当第一个手指落下
            case MotionEvent.ACTION_DOWN:
                isPulling = false;
                break;
            //当后面的手指落下
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            //当其中一个手指移动
            case MotionEvent.ACTION_MOVE:
                if (mJRefreshHandler != null && mJRefreshHandler.checkCanDoRefresh(this, mContent, mHeaderView)) {
                    final float x = ev.getX(ev.getPointerCount() - 1);
                    final float y = ev.getY(ev.getPointerCount() - 1);
                    final float absX = Math.abs(x);
                    final float absY = Math.abs(y);
                    if (absY > absX && absY > mTouchSlop) {
                        isPulling = true;
                        return true;
                    }
                }
                break;
            //当其中一个手指收起
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (isPulling) {
                    return isPulling;
                }
            case MotionEvent.ACTION_CANCEL:
                break;
            default:

        }

        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (isPulling) {
                    isPulling = false;

                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isPulling = false;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
