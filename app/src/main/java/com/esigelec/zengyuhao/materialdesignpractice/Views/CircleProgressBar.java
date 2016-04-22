package com.esigelec.zengyuhao.materialdesignpractice.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * Created by enzoz on 2016/4/22.
 */
public class CircleProgressBar extends View {
    private int mBarWidth;
    private Color mBarColor;
    private BarStyle mBarStyle;
    private int mPercentage;

    private enum BarStyle {
        STATIC,
        DYNAMIC
    }

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);

        try {
            mBarWidth = a.getInteger(R.styleable.CircleProgressBar_barWidth, 0);
            mPercentage = a.getInteger(R.styleable.CircleProgressBar_percentage, 0);
        } finally {
            a.recycle();
        }
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
