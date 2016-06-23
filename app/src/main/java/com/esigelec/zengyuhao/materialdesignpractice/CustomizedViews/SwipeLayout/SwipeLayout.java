package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SwipeLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.ViewWrapper;

/**
 * Created by ZENG Yuhao on 23/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class SwipeLayout extends FrameLayout {
    private static final String TAG = "SwipeLayout";

    // MODE
    private static final int MODE_STICKY = 0;
    private static final int MODE_NON_STICKY = 1;

    // DIRECTION FLAG
    private static final int GOING_LEFT = 0;
    private static final int GOING_RIGHT = 1;
    private static final int STAYING = 2;
    /**
     * Threshold percentage in relation to width of this layout. When user release the swiping, this value is used to
     * judge whether the top layer should continue swipe to target position (swiping-offset >= threshold) or it should
     * swipe back to its initial position (swiping-offset < threshold).
     */
    private double mTriggerThreshold = 0.05;
    private int mTriggerThresholdPixels;
    private double maxLeftOffset = 0.30;
    private int maxLeftOffsetPixels;

    /* touch event*/
    private int X0, Y0;
    private int lastX, lastY;

    private View mTopLayer, mBottomLayer;
    private int mWidth, mHeight;
    private int mode = MODE_STICKY;
    private ValueAnimator mAnimator;
    private OpenAndCloseListener mListener;
    private int flag = STAYING;  // direction flag


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
        Log.i(TAG, "------>init");
        // register listener to get width and height once layout finished.
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null)
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mWidth = getWidth();
                    mHeight = getHeight();
                    mTriggerThresholdPixels = (int) (mTriggerThreshold * mWidth);
                    maxLeftOffsetPixels = (int) (maxLeftOffset * mWidth);

                    // unregister the listener when first time width and height has been got.
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
        Log.i(TAG, "------>onFinishInflate");
        if (getChildCount() < 2)
            throw new IllegalStateException("To make SwipeLayout work properly, there must be 2 layers.");
        initLayers();
        super.onFinishInflate();
    }

    private void initLayers() {
        Log.i(TAG, "------>initLayers");
        mTopLayer = getChildAt(0);
        mBottomLayer = getChildAt(1);

        // clear layout params of the layers to make sure they fill all the space.
        FrameLayout.LayoutParams paramsTop = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mTopLayer.setLayoutParams(paramsTop);

        FrameLayout.LayoutParams paramsBottom = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomLayer.setLayoutParams(paramsBottom);

        // reset elevation to make sure top layer is on the top of bottom layer
        mTopLayer.setElevation(3);
        mBottomLayer.setElevation(2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currX = (int) event.getRawX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "------>ACTION_DOWN");
                stopAnimation();
                X0 = currX;
                lastX = currX;
                return true;

            case MotionEvent.ACTION_MOVE:
                int dtX = currX - lastX;
                flag = (dtX < 0) ? GOING_LEFT : GOING_RIGHT;

                // getX() should always be in the section [-maxLeftOffsetPixels, 0]
                if (mTopLayer.getX() + dtX < -maxLeftOffsetPixels)
                    mTopLayer.setX(-maxLeftOffsetPixels);
                else if (mTopLayer.getX() + dtX > 0)
                    mTopLayer.setX(0);
                else
                    mTopLayer.setX(mTopLayer.getX() + dtX);

                // record x, y
                lastX = currX;
                return true;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "------>ACTION_UP");
                if (mode == MODE_STICKY) {
                    int leftBound = -(maxLeftOffsetPixels - mTriggerThresholdPixels);
                    int rightBound = -mTriggerThresholdPixels;

                    if (mTopLayer.getX() <= leftBound && mTopLayer.getX() >= -maxLeftOffsetPixels)
                        open();
                    else if (mTopLayer.getX() <= 0 && mTopLayer.getX() >= rightBound)
                        close();
                    else {
                        if (flag == GOING_LEFT)
                            open();
                        else if (flag == GOING_RIGHT)
                            close();
                    }
                }
                return true;

        }
        return super.onTouchEvent(event);
    }

    public void open() {
        Log.i(TAG, "------>open");
        startAnimation(-maxLeftOffsetPixels, "open");
    }

    public void close() {
        Log.i(TAG, "------>close");
        startAnimation(0, "close");
    }

    private void startAnimation(int targetPosition, final String sender) {
        mAnimator = ValueAnimator.ofInt((int) mTopLayer.getX(), targetPosition);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(150);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTopLayer.setX((int) animation.getAnimatedValue());
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener == null) return;
                if (sender.equals("open"))
                    mListener.onOpened();
                else if (sender.equals("close"))
                    mListener.onClosed();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAnimator.start();
    }

    private void stopAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void setOpenAndCloseListener(OpenAndCloseListener listener) {
        if (listener != null)
            mListener = listener;
    }

    public interface OpenAndCloseListener {
        void onOpened();

        void onClosed();
    }
}
