package com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * <p>
 * Created by ZENG Yuhao on 16/7/4. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class MagnifierView extends View {
    public static final String TAG = "Magnifier";

    private Bitmap mBitmap;
    private Paint mBitmapPaint, mCenterPaint, mSidelinePaint;
    private BitmapShader mBitmapShader;
    private ShapeDrawable mBackgroundDrawable;
    private int mBackgroundColor;

    /**
     * backup of radius, when animation is running on radius, this value is used to restore initial radius
     */
    private float pxRadiusBackup;
    private float mScaleRate = 1.5f;
    private Matrix mScaleMatrix;
    private Matrix mCanvasMatrix;
    private int mSidelineWidth = 3;

    /**
     * absolute values of center(X,Y) in pixels
     */
    private float absCenterX, absCenterY;

    /**
     * absolute value of radius in pixels
     */
    private float absRadius;

    private OnAppearDisappearListener mOnAppearDisappearListener;
    private AnimatorSet mAppearDisappearAnim;
    private boolean isVisibleCenterEnabled = true;
    private int mAnimationDuration = 150;

    public MagnifierView(Context context) {
        super(context);
        init(context);
    }

    public MagnifierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MagnifierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MagnifierView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mBitmapPaint = new Paint();
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        mScaleMatrix = new Matrix();
        mCanvasMatrix = new Matrix();

        mSidelinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSidelinePaint.setStyle(Paint.Style.STROKE);
        mSidelinePaint.setStrokeWidth(mSidelineWidth);
        mSidelinePaint.setColor(getResources().getColor(android.R.color.darker_gray));

        mBackgroundColor = getResources().getColor(android.R.color.transparent);
        mBackgroundDrawable = new ShapeDrawable();
        mBackgroundDrawable.setShape(new OvalShape());
        setDefaultBackgroundColor(mBackgroundColor);
    }

    public void setDefaultBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        applyBackgroundColor(color);
    }

    protected void applyBackgroundColor(@ColorInt int color) {
        mBackgroundDrawable.getPaint().setColor(color);
        setBackground(mBackgroundDrawable);
    }

    public void setSidelineWidth(int width) {
        mSidelineWidth = width;
        postInvalidateOnAnimation();
    }

    public int getSidelineWidth() {
        return mSidelineWidth;
    }

    public void setAbsRadius(float R) {
        absRadius = R;
        postInvalidateOnAnimation();
    }

    public float getAbsRadius() {
        return absRadius;
    }

    public void setScaleRate(float scale) {
        mScaleRate = scale;
        update();
    }

    public float getScaleRate() {
        return mScaleRate;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public int getAnimationDuration() {
        return mAnimationDuration;
    }

    public void bindBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "-->bindBitmap: Skipped, bitmap cannot be null.");
            return;
        }
        if (bitmap != mBitmap) {
            mBitmap = bitmap;
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setShader(mBitmapShader);
            Log.i(TAG, "-->bindBitmap");
        }
        postInvalidateOnAnimation();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(width, height);
            setLayoutParams(lp);
        } else {
            lp.width = width;
            lp.height = height;
        }
        requestLayout();
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(lp);
        } else {
            lp.width = width;
        }
        requestLayout();
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
            setLayoutParams(lp);
        } else {
            lp.height = height;
        }
        requestLayout();
    }

    public void enableVisibleCenter() {
        isVisibleCenterEnabled = true;
    }

    public void disableVisibleCenter() {
        isVisibleCenterEnabled = false;
    }

    public void setOnAppearDisappearListener(OnAppearDisappearListener listener) {
        mOnAppearDisappearListener = listener;
    }

    public void removeOnAppearDisappearListener() {
        mOnAppearDisappearListener = null;
    }

//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        Log.i("MAGNIFIER ONLAYOUT", "....");
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mAppearDisappearAnim != null && mAppearDisappearAnim.isRunning()) {
            mAppearDisappearAnim.cancel();
        }
        absRadius = Math.min(w, h) / 2;
        pxRadiusBackup = absRadius;

        //translate canvas origin point to the middle of this view
        mCanvasMatrix.setTranslate(w / 2, h / 2);
        absCenterX = w / 2;
        absCenterY = h / 2;
    }


    public void updateCenterByFractionVals(double fractCentX, double fractCentY) {
        //if (fractCentX < 0 || fractCentX > 1 || fractCentY < 0 || fractCentY > 1) return false;
        if (fractCentX < 0) fractCentX = 0;
        else if (fractCentX > 1) fractCentX = 1;

        if (fractCentY < 0) fractCentY = 0;
        else if (fractCentY > 1) fractCentY = 1;
        float absX = (float) (mBitmap.getWidth() * fractCentX);
        float absY = (float) (mBitmap.getHeight() * fractCentY);
        updateCenterByAbsoluteVals(absX, absY);
    }

    public void updateCenterByAbsoluteVals(float absX, float absY) {
        absCenterX = absX;
        absCenterY = absY;
        update();
    }

    protected void update() {
        if (mBitmap == null) return;
        mScaleMatrix.setTranslate(-absCenterX, -absCenterY);
        mScaleMatrix.postScale(mScaleRate, mScaleRate);
        mBitmapPaint.getShader().setLocalMatrix(mScaleMatrix);
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) return;

        // translate to middle of the view
        canvas.concat(mCanvasMatrix);

        // draw bitmap in the circle
        canvas.drawCircle(0, 0, absRadius, mBitmapPaint);

        // draw center point
        if (isVisibleCenterEnabled)
            canvas.drawCircle(0, 0, 5, mCenterPaint);

        //draw side line
        canvas.drawCircle(0, 0, absRadius - mSidelineWidth / 2, mSidelinePaint);
    }

    /**
     * appear without animations
     */
    public void appearFast() {
        setAlpha(1);
        setAbsRadius(pxRadiusBackup);
        bringToFront();
    }

    /**
     * disappear without animations
     */
    public void disappearFast() {
        setAlpha(0);
        setAbsRadius(0);
    }

    protected void prepareAppearDisappearAnim(Animator animR, Animator animAlpha) {
        mAppearDisappearAnim = new AnimatorSet();
        mAppearDisappearAnim.setDuration(mAnimationDuration);
        //mAppearDisappearAnim.setInterpolator(new DecelerateInterpolator());
        mAppearDisappearAnim.playTogether(animR, animAlpha);
    }

    /**
     * appear with animations
     */
    public void appear() {
        // only one animation is allowed at the same moment.
        if (mAppearDisappearAnim != null && mAppearDisappearAnim.isRunning()) {
            mAppearDisappearAnim.cancel();
        }
        bringToFront();
        Animator animR = ObjectAnimator.ofFloat(this, "absRadius", getAbsRadius(), pxRadiusBackup);
        Animator animAlpha = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 1);
        prepareAppearDisappearAnim(animR, animAlpha);
        mAppearDisappearAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                //applyBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onBeforeAppear();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //applyBackgroundColor(mBackgroundColor);
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onAppeared();
                }
            }
        });
        mAppearDisappearAnim.setInterpolator(new DecelerateInterpolator());
        mAppearDisappearAnim.start();
    }

    /**
     * disappear with animations
     */
    public void disappear() {
        // only one animation is allowed at the same moment.
        if (mAppearDisappearAnim != null && mAppearDisappearAnim.isRunning()) {
            mAppearDisappearAnim.cancel();
        }
        Animator animR = ObjectAnimator.ofFloat(this, "absRadius", getAbsRadius(), 0);
        Animator animAlpha = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 0);
        prepareAppearDisappearAnim(animR, animAlpha);
        mAppearDisappearAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //applyBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onBeforeDisappear();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //applyBackgroundColor(mBackgroundColor);
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onDisappeared();
                }
            }
        });
        mAppearDisappearAnim.setInterpolator(new AccelerateInterpolator());
        mAppearDisappearAnim.start();
    }

    public interface OnAppearDisappearListener {
        void onBeforeAppear();

        void onBeforeDisappear();

        void onAppeared();

        void onDisappeared();
    }

}

