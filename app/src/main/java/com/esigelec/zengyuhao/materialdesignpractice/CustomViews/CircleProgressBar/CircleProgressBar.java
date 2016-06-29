package com.esigelec.zengyuhao.materialdesignpractice.CustomViews.CircleProgressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 *
 * Created by Enzo(ZENG Yuhao) on 16/5/10.
 */
public class CircleProgressBar extends RelativeLayout {
    private int mBarWidth = 0;
    private int mPercentage = 0;

    private CircleProgressBarView mView;

    private TextView mTextView;
    private int mBarTextSize;

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
        try {
            mBarWidth = a.getInteger(R.styleable.CircleProgressBar_barWidth, 0);
            mPercentage = a.getInteger(R.styleable.CircleProgressBar_percentage, 0);
            mBarTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_barTextSize, 12);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        // init CircleProgessBarView
        mView = new CircleProgressBarView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mView.setLayoutParams(lp);
        mView.updatePercentage(mPercentage);

        // init TextView
        mTextView = new TextView(getContext());
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_IN_PARENT);
        mTextView.setLayoutParams(lp);
        mTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        mTextView.setTextSize(mBarTextSize);

        //add views
        addView(mView);
        addView(mTextView);
    }

    public int getPercentage() {
        return mPercentage;
    }

    public void setPercentage(int percentage) {
        mPercentage = percentage;
        mView.updatePercentage(percentage);
        mTextView.setText(String.valueOf(mPercentage) + "%");
    }
}
