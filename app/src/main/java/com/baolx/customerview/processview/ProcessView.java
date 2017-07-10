package com.baolx.customerview.processview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 10312 on 2017/6/12.
 */

public class ProcessView extends View {
    private static final int DEFAULT_HEIGHT_DP = 20;

    private Paint pgPaint;

    private Paint processPaint;

    private RectF pgRect;

    private Rect textRect;

    private int borderWidth;
    /** 圆角 */
    private float radius;

    private int mProcess;

    private float mMaxProcess;

    public ProcessView(Context context) {
        this(context, null);
    }

    public ProcessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initVew() {
        borderWidth = 5;
        radius = 50;
        mMaxProcess = 100;
        pgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pgPaint.setStyle(Paint.Style.STROKE);
        pgPaint.setStrokeWidth(borderWidth);
        processPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        processPaint.setStyle(Paint.Style.FILL);
        textRect = new Rect();
        pgRect = new RectF(borderWidth, borderWidth, getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode){
            case MeasureSpec.AT_MOST:
                height = dp2px(DEFAULT_HEIGHT_DP);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = heightSpecSize;
                break;
        }
        initVew();
        setMeasuredDimension(widthSpecSize, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        drawBackgroud(canvas);
        //绘制进度
        drawProcess(canvas);
        //绘制文本
        drawText(canvas);
        //绘制进度文本
        drawProcessText(canvas);
    }

    private void drawProcessText(Canvas canvas) {
        processPaint.setColor(Color.WHITE);
        String text = getProcessText();
        canvas.save();
        float tx = (getMeasuredWidth() - textRect.width()) / 2;
        float ty = (getMeasuredHeight() + textRect.height()) / 2;
        int right = (int) (mProcess / mMaxProcess * getMeasuredWidth());
        if(tx <= right){
            canvas.clipRect(textRect.left, textRect.top, right, textRect.right);
            canvas.drawText(text, tx, ty, processPaint);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        processPaint.setColor(Color.GREEN);
        String text = getProcessText();
        processPaint.getTextBounds(text, 0, text.length(), textRect);
        processPaint.setTextSize(50);
        float tx = (getMeasuredWidth() - textRect.width()) / 2;
        float ty = (getMeasuredHeight() + textRect.height()) / 2;
        canvas.drawText(text, tx, ty, processPaint);
    }

    private void drawProcess(Canvas canvas) {
        processPaint.setColor(Color.GREEN);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        int right = (int) (mProcess / mMaxProcess * getMeasuredWidth());
        canvas.clipRect(pgRect.left, pgRect.top, right, pgRect.bottom);
        canvas.drawRoundRect(pgRect, radius, radius, processPaint);
        canvas.restore();
    }

    private void drawBackgroud(Canvas canvas) {
        pgPaint.setColor(Color.GREEN);
        canvas.drawRoundRect(pgRect, radius, radius, pgPaint);
    }

    private String getProcessText(){
        return mProcess + "%";
    }

    public void setProcess(int process){
        this.mProcess = process;
        postInvalidate();
    }

    private int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
