package com.baolx.customerview.bottombar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by 10312 on 2017/6/15.
 */

public class BottomBar extends LinearLayout {
    private View bottomBar;

    private View bottomContent;

    private Scroller mScroller;

    /** 控制栏的可视范围 */
    private Rect barRect;

    private int downX, downY;
    private int scrollOffset;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
    }

    private void initView() {
        int count = getChildCount();
        if(count != 2){
        }
        bottomBar = getChildAt(0);
        bottomContent =getChildAt(1);
        barRect = new Rect();
        mScroller = new Scroller(getContext());
        bottomBar.getGlobalVisibleRect(barRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("", "--------->x="+event.getX() + ", y="+event.getY());
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endY = (int) event.getY();
                int dy = (int) (endY - downY);
                int toScroll = getScrollY() - dy;
                if(toScroll < 0){
                    toScroll = 0;
                } else if(toScroll > bottomContent.getMeasuredHeight()){
                    toScroll = bottomContent.getMeasuredHeight();
                }
                scrollTo(0, toScroll);
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                scrollOffset = getScrollY();
                if(scrollOffset > bottomContent.getMeasuredHeight() / 2){
                    showNavigation();
                } else {
                    closeNavigation();
                }
                break;
        }

        return true;
    }

    private void showNavigation(){
        int dy = bottomContent.getMeasuredHeight() - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
    }

    private void closeNavigation(){
        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bottomBar.layout(0, getMeasuredHeight() - bottomBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        bottomContent.layout(0, getMeasuredHeight(), getMeasuredWidth(), bottomBar.getBottom() + bottomContent.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
