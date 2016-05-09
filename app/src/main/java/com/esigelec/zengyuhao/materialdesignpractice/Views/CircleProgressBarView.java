package com.esigelec.zengyuhao.materialdesignpractice.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * Created by enzoz on 2016/4/22.
 */
public class CircleProgressBarView extends View {
    private int mBarWidth;
    private Color mBarColor;
    private BarStyle mBarStyle;
    private int mPercentage;

    private int centerX = -1, centerY = -1;
    private Paint paint, paint1, paint2, paintTxt;
    private float circleR = -1;
    private RectF rect;

    private enum BarStyle {
        STATIC,
        DYNAMIC
    }

    public CircleProgressBarView(Context context) {
        super(context);
        init();
    }

    public CircleProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView, 0, 0);
        try {
            mBarWidth = a.getInteger(R.styleable.CircleProgressBarView_barWidth, 0);
            mPercentage = a.getInteger(R.styleable.CircleProgressBarView_percentage, 0);
            Log.i("attr", "attr---->" + mBarWidth + " " + mPercentage);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(3);

        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setStrokeJoin(Paint.Join.ROUND);
        paint1.setStrokeWidth(3);

        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeWidth(3);

        paintTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTxt.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        paintTxt.setTextSize(70);
        paintTxt.setStrokeCap(Paint.Cap.ROUND);
        paintTxt.setStrokeJoin(Paint.Join.ROUND);
        paintTxt.setStrokeWidth(3);

    }

    public void updatePercentage(int percentage) {
        mPercentage = percentage;
        if (mPercentage > 100)
            mPercentage = 100;
        else if (mPercentage < 0)
            mPercentage = 0;
        invalidate();
    }

    public int getPercentage() {
        return mPercentage;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (centerX >= 0 && centerY >= 0 && circleR >= 0) {
            canvas.drawCircle(centerX, centerY, circleR, paint);

            canvas.drawArc(rect, -90, 0.01f * mPercentage * 360, true, paint1);
            canvas.drawCircle(centerX, centerY, circleR * 0.8f, paint2);
            canvas.drawText(String.valueOf(mPercentage) + "%", centerX - 50, centerY + 10, paintTxt);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            circleR = ((float) Math.min(getMeasuredWidth(), getMeasuredHeight())) / 2;
            centerX = getMeasuredWidth() / 2;
            centerY = getMeasuredWidth() / 2;
            rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            Log.i("onLayout", "onLayout-->" + left + " " + top + " " + right + " " + bottom);
            Log.i("onLayout", "onLayout----->" + circleR + " " + centerX + " " + centerY);
        }
    }
}
