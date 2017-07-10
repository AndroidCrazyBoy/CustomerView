package com.baolx.customerview.customer_switch;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.baolx.customerview.R;

/**
 * Created by BaoLongxiang on 2016/7/13.
 * 自定义开关控件
 */
public class SwitchView extends View {

    private static final String TAG = "SwitchView";
    private Bitmap switchBitmap;
    private Bitmap slidBitmap;
    private Paint mPaint;
    /** 滑块的走边距 */
    private float slidLeft;
    /** 滑块的最大滑动边距 */
    private float maxSlid;
    /** 是否开启 */
    private boolean isOpen;
    /** 可视为滑动的最小距离 */
    private int mTouchSlop = 1;
    /** 是否滑动了 */
    private boolean isSlid;
    /** 记录开始按下的坐标 */
    private int startX;

    /** 默认情况下高度 */
    private int defaultHeight;
    /** 默认情况下宽度 */
    private int defaultWidth;


    public SwitchView(Context context) {
        super(context);
        initView(context);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        switchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        //计算得出最大可滑动的距离
        maxSlid = switchBitmap.getWidth() - slidBitmap.getWidth();
        defaultHeight = switchBitmap.getHeight();
        defaultWidth = switchBitmap.getWidth();

        mPaint = new Paint();
    }

    /**
     * 返回开关打开的状态
     * @return
     */
    public boolean isOpen(){
        return isOpen;
    }


    /**
     * 设置开关状态
     * @param sw
     */
    public void setSwitchState(boolean sw){
        isOpen = sw;
        doSwitch();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, defaultHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, heightSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpec, defaultHeight);
        } else {
//            setMeasuredDimension(widthSpec, heightMeasureSpec);
            setMeasuredDimension(defaultWidth, defaultHeight);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        if(slidLeft > maxSlid){
            slidLeft = maxSlid;
        } else if(slidLeft < 0){
            slidLeft = 0;
        }
        canvas.drawBitmap(switchBitmap, 0, 0, mPaint);
        canvas.drawBitmap(slidBitmap, slidLeft, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "---->ACTION_DOWN");
                startX = (int) event.getX();
                isSlid = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "---->ACTION_MOVE");
                int offsetX = (int) (event.getX() - startX);
                Log.i(TAG, "---->offsetX = "+offsetX);
                if(Math.abs(offsetX) < mTouchSlop && !isSlid){
                    isSlid = false;
                } else {
                    isSlid = true;
                    slidLeft += offsetX;
                    startX = (int) event.getX();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "---->ACTION_UP isSlid = "+isSlid);
                Log.i(TAG, "---->ACTION_UP slidLeft = "+slidLeft);
                if(isSlid) {
                    if (slidLeft >= (maxSlid / 2)) {
                        isOpen = false;
                    } else {
                        isOpen = true;
                    }
                }
                changeSwitch();
                break;
        }

        return true;
    }

    private void changeSwitch() {
        isOpen = !isOpen;
        doSwitch();
    }

    private void doSwitch() {
        if(isOpen){
            slidLeft = maxSlid;
        } else {
            slidLeft = 0;
        }
        invalidate();
    }
}
