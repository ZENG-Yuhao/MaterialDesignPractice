package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * <p>
 * A layout supporting swiping-to-left effect, there are two modes STICKY and NON-STICKY for user. With STICKY
 * mode, layout will swipe automatically to left or to right, with NON-STICKY mode, layout can stop at  any
 * intermediate position between the most left position and the most right position.
 * <p>
 * For now this layout can only be initialized in XML file and only support horizontal swipe-to-left effect, it will
 * take two first children as its top layer and bottom layer.
 * <p>
 * Attention: This layout does not take charge of actions of each view, it handles only the swiping effect. Since
 * view are inflated by XML file, so all elements can be retrieved by their Ids, all actions can be defined at other
 * place (in Activity for example).
 * <p>
 * Created by ZENG Yuhao on 23/06/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class SwipeLayout extends FrameLayout {
    private static final String TAG = "SwipeLayout";

    // STATE
    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;
    public static final int STATE_TRANSIT = 2;
    public static final int STATE_DRAGGING = 3;

    // MODE
    public static final int MODE_STICKY = 0;
    public static final int MODE_NON_STICKY = 1;

    // DIRECTION FLAG
    private static final int GOING_LEFT = 0;
    private static final int GOING_RIGHT = 1;
    private static final int STAYING = 2;
    /**
     * Threshold percentage in relation to width of this layout. With STICKY mode when user release the swiping, this
     * value is used to judge whether the top layer should continue swipe to target position (swiping-offset >=
     * threshold) or it should swipe back to its initial position (swiping-offset < threshold).
     */
    private double mTriggerThreshold = 0.02;
    private int mTriggerThresholdPixels; // mTriggerThresholdPixels = mTriggerThreshold * width;
    /**
     * Max offset of left bound in percentage. In this layout, position of layer is controlled by {@link View#setX(float)}
     * when layer reached the left boundary, getX() = -maxLeftOffsetPixels, when layer reached the right boundary,
     * getX() = 0;
     */
    private double maxLeftOffset = 0.30;
    private int maxLeftOffsetPixels; // maxLeftOffsetPixels = maxLeftOffset * width;

    /* touch event*/
    private int lastX, lastY;

    private View mTopLayer, mBottomLayer;
    private int mWidth;
    private int mode = MODE_STICKY;
    private ValueAnimator mAnimator;
    private OpenAndCloseListener mListener;
    private int flag = STAYING;  // direction flag
    private int mState = STATE_CLOSED;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMaxLeftOffset(double fraction) {
        if (fraction > 0 && fraction <= 1) {
            maxLeftOffset = fraction;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    public double getMaxLeftOffset() {
        return maxLeftOffset;
    }

    public void setTriggerThreshold(double fraction) {
        if (fraction > 0 && fraction <= 1) {
            mTriggerThreshold = fraction;
        }
    }

    public double getTriggerThreshold() {
        return mTriggerThreshold;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public int getState() {
        return mState;
    }

    /**
     * Initialization before layout is inflated.
     */
    private void init() {
        Log.i(TAG, "------>init");
        // register listener to get width and height once layout finished.
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null)
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mWidth = getWidth();
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

        mTopLayer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onLayerTouch(v, event);
            }
        });
    }

    /**
     * <p/>
     * On <b>NON-STICKY</b> mode, there are only min boundary and max boundary check.
     * <p/>
     * On <b>STICK</b> mode, if top layer's getX() locates in <b>(-maxLeftOffsetPixels, leftThreshold)</b> or in
     * <b>(rightThreshold, 0)</b>, top layer will swipe back to the closest position, otherwise next swiping action
     * will take account of {@link #flag} state.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    private boolean onLayerTouch(View v, MotionEvent event) {
        int currX = (int) event.getRawX();
        int currY = (int) event.getRawY();

        ViewConfiguration vConfig = ViewConfiguration.get(v.getContext());
        /*
            "Touch slop" refers to the distance in pixels a user's touch can wander before the gesture is interpreted
             as scrolling. Touch slop is typically used to prevent accidental scrolling when the user is performing
             some other touch operation, such as touching on-screen elements.
         */
        int mTouchSlop = vConfig.getScaledTouchSlop();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "------>ACTION_DOWN");
                stopAnimation();
                lastX = currX;
                lastY = currY;
                return true;

            case MotionEvent.ACTION_MOVE:
                mState = STATE_DRAGGING;
                int dtX = currX - lastX;
                int dtY = currY - lastY;


                // if (Math.abs(dtX) <= mTouchSlop) return false;

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
                lastY = currY;
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "------>ACTION_UP");
                if (mode == MODE_STICKY) {
                    int leftThreshold = -(maxLeftOffsetPixels - mTriggerThresholdPixels);
                    int rightThreshold = -mTriggerThresholdPixels;

                    if (mTopLayer.getX() <= leftThreshold && mTopLayer.getX() >= -maxLeftOffsetPixels)
                        open();
                    else if (mTopLayer.getX() <= 0 && mTopLayer.getX() >= rightThreshold)
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
        return false;
    }

    /**
     * Top layer swipe to the most left side, as if it was opened.
     */
    public void open() {
        Log.i(TAG, "------>open");
        startAnimation(-maxLeftOffsetPixels, "open");
    }

    /**
     * Top layer swipe to the most right side, as if it was closed.
     */
    public void close() {
        Log.i(TAG, "------>close");
        startAnimation(0, "close");
    }

    private void startAnimation(int targetPosition, final String sender) {
        mAnimator = ValueAnimator.ofInt((int) mTopLayer.getX(), targetPosition);
        mAnimator.setInterpolator(new DecelerateInterpolator());
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
                mState = STATE_TRANSIT;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener == null) return;
                if (sender.equals("open")) {
                    mState = STATE_OPENED;
                    mListener.onOpened();
                } else if (sender.equals("close")) {
                    mState = STATE_CLOSED;
                    mListener.onClosed();

                }
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

    /**
     * If there is a new action coming in, stop the ancient animation.
     */
    private void stopAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void setOpenAndCloseListener(OpenAndCloseListener listener) {
        if (listener != null)
            mListener = listener;
    }

    /**
     * Listener to listener open and close state.
     */
    public interface OpenAndCloseListener {
        void onOpened();

        void onClosed();
    }
}
