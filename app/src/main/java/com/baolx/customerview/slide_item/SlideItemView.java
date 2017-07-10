package com.baolx.customerview.slide_item;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.baolx.customerview.R;

/**
 * Created by BaoLongxiang on 2016/7/14.
 */
public class SlideItemView extends FrameLayout {

    /**
     * 默认的 回弹时间
     */
    private static final int DEFAULT_TIME = 300;
    /**
     * 滑动item 的内容
     */
    private View slidItemContent;

    /**
     * 滑动item的滑动块
     */
    private View slidItemMenu;


    /**
     * 弹性滑动
     */
    private Scroller mScroller;

    private int downX;

    private int mStartX, mStartY;

    private int scrollOffset;

    private int itemMenuWidth, itemMenuHeight;

    private int itemContentWidth, itemContentHeight;

    /**
     * 监听slideView的开关状态
     */
    private ISlideItemCallBack changeStateListener;

    public void setChangeStateListener(ISlideItemCallBack changeStateListener) {
        this.changeStateListener = changeStateListener;
    }

    public SlideItemView(Context context) {
        super(context);
        initView(context);
    }

    public SlideItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SlideItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        slidItemContent = getChildAt(0);
        slidItemMenu = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        itemMenuWidth = slidItemMenu.getWidth();
        itemMenuHeight = slidItemMenu.getHeight();
        itemContentWidth = getMeasuredWidth();
        itemContentHeight = getMeasuredHeight();

        slidItemContent.layout(0, 0, itemContentWidth, itemContentHeight);
        slidItemMenu.layout(itemContentWidth, 0, itemContentWidth + itemMenuWidth, itemMenuHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.i("TAG", "---------onTouchEvent ACTION_DOWN");
                downX = mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("TAG", "---------onTouchEvent ACTION_MOVE");
                int endX = (int) event.getX();
                int endY = (int) event.getY();

                int xx = endX - downX;
                if(Math.abs(xx) > 5){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                int offset = endX - mStartX;
                int toScroll = getScrollX() - offset;
//                Log.i("TAG", "---------toScroll="+toScroll);
//                Log.i("TAG", "---------offset="+offset);
//                Log.i("TAG", "---------getScrollX()="+getScrollX());
//                Log.i("TAG", "---------slidItemMenu.getWidth()="+slidItemMenu.getWidth());
//                Log.i("TAG", "---------offset="+offset);
//                scrollBy(offset, 0);
                if(toScroll < 0){
                    toScroll = 0;
                } else if(toScroll > itemMenuWidth){
                    toScroll = itemMenuWidth;
                }
                scrollTo(toScroll, getScrollY());

                mStartX = endX;
                mStartY = endY;

                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "---------onTouchEvent ACTION_UP");
                scrollOffset = getScrollX();
                if(scrollOffset >= (itemMenuWidth / 2.0)){
                    openSlid();
                } else {
                    closeSlid();
                }
                break;
        }
        return true;
    }


    /**
     * 事件拦截 返回true 代表拦截当前事件, onTouchEvent()方法将会被调用.
     * 返回false 事件将会传递给子view
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        boolean isIntercept = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(changeStateListener != null){
                    changeStateListener.onTouch(this);
                }
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - mStartX;
                int dy = endY - mStartY;
                if(Math.abs(dx) > Math.abs(dy) && endX > 8){
                    isIntercept = true;
                }
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return isIntercept;
    }

    public void openSlid() {
        if(changeStateListener != null){
            changeStateListener.slidOpen(this);
        }
        int dx = itemMenuWidth - scrollOffset;
        Log.i("TAG", "---------openSlid_dx="+dx);
        mScroller.startScroll(getScrollX(), getScrollY(), dx, 0, DEFAULT_TIME);
        invalidate();
    }

    private void closeSlid() {
        if(changeStateListener != null){
            changeStateListener.slidClose(this);
        }
        int dx = 0 - scrollOffset;
        Log.i("TAG", "---------closeSlid_dx="+dx);
        mScroller.startScroll(getScrollX(), getScrollY(), dx, 0, DEFAULT_TIME);
        invalidate();
    }


    /**
     * 防止出现关闭不上的效果出现
     */
    public void closeSlid2(){
        if(changeStateListener != null){
            changeStateListener.slidClose(this);
        }
        if(getScrollX() == 0){
            return;
        }
        int dx = 0 - itemMenuWidth;
        mScroller.startScroll(getScrollX(), getScrollY(), dx, 0, DEFAULT_TIME);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public interface ISlideItemCallBack{
        void slidOpen(SlideItemView itemView);
        void slidClose(SlideItemView itemView);
        void onTouch(SlideItemView itemView);
    }
}
