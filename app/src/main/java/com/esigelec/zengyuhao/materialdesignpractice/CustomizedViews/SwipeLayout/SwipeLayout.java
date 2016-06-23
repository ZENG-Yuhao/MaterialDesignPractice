package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SwipeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.MutableBoolean;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.ViewWrapper;

/**
 * Created by ZENG Yuhao on 23/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class SwipeLayout extends FrameLayout {
    private View mTopLayerView, mBottomLayerView;
    private ViewWrapper mTopLayer, mBottomLayer;

    private int mWidth, mHeight;
    /**
     * Threshold percentage in relation to width of this layout. When user release the swiping, this value is used to
     * judge whether the top layer should continue swipe to target position (swiping-offset >= threshold) or it should
     * swipe back to its initial position (swiping-offset < threshold).
     */
    private float mTriggerThreshold;

    private int mTriggerThresholdPixels;

    private float maxLeftOffset;

    private int maxLeftOffsetPixels;

    public SwipeLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // register listener to get width and height once layout finished.
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null)
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mWidth = getWidth();
                    mHeight = getHeight();

                    ViewTreeObserver observer0 = getViewTreeObserver();
                    if (observer0 != null) {
                        observer0.removeOnGlobalLayoutListener(this);
                    }
                }
            });
    }

    // when xml layout has been load, all initialisation start form here.
    @Override
    protected void onFinishInflate() {
        if (getChildCount() < 2)
            throw new IllegalStateException("To make SwipeLayout work properly, there must be 2 layers.");

        initLayers();
        super.onFinishInflate();
    }

    private void initLayers() {
        mTopLayerView = getChildAt(0);
        mBottomLayerView = getChildAt(1);

        mTopLayer = new ViewWrapper(mTopLayerView);
        mBottomLayer = new ViewWrapper(mBottomLayerView);

        // clear layout params of the layers to make sure they fill all the space.
        FrameLayout.LayoutParams paramsTop = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mTopLayerView.setLayoutParams(paramsTop);

        FrameLayout.LayoutParams paramsBottom = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomLayerView.setLayoutParams(paramsBottom);

        // reset elevation to make sure top layer is on the top of bottom layer
        mTopLayerView.setElevation(3);
        mBottomLayerView.setElevation(2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }

        return super.onTouchEvent(event);
    }
}
