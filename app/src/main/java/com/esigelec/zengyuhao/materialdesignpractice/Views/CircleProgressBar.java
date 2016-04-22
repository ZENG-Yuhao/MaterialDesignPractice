package com.esigelec.zengyuhao.materialdesignpractice.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by enzoz on 2016/4/22.
 */
public class CircleProgressBar extends View {
    private int mBarWidth;
    private Color mBarColor;
    private BarStyle mBarStyle;

    private enum BarStyle {
        STATIC,
        DYNAMIC
    }

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
