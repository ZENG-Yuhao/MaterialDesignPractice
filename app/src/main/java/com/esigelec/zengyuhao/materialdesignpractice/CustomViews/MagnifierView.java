package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
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
    private float absoluteCentX, absoluteCentY, absoluteR;
    private float backupAbsoluteR;
    private float mScale = 1.5f;
    private Matrix mScaleMatrix;
    private Matrix mCanvasMatrix;
    private int mSidelineWidth = 3;
    private OnAppearDisappearListener mOnAppearDisappearListener;
    private AnimatorSet mAppearDisappearAnim;

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

    protected void init(Context context) {
        mBitmapPaint = new Paint();
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        mScaleMatrix = new Matrix();
        mCanvasMatrix = new Matrix();

        mSidelinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSidelinePaint.setStyle(Paint.Style.STROKE);
        mSidelinePaint.setStrokeWidth(mSidelineWidth);
        mSidelinePaint.setColor(getResources().getColor(android.R.color.darker_gray));

        initShape();
    }

    protected void initShape() {
        OvalShape shape_circle = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(shape_circle);
        shapeDrawable.getPaint().setColor(getResources().getColor(android.R.color.transparent));
        this.setBackground(shapeDrawable);
        setElevation(12);
    }

    public void setSidelineWidth(int width) {
        mSidelineWidth = width;
    }

    public int getSidelineWidth() {
        return mSidelineWidth;
    }

    public void setAbsoluteR(float R) {
        absoluteR = R;
        postInvalidateOnAnimation();

    }

    public float getAbsoluteR() {
        return absoluteR;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mAppearDisappearAnim != null && mAppearDisappearAnim.isRunning()) {
            mAppearDisappearAnim.cancel();
        }
        absoluteR = Math.min(w, h) / 2;
        backupAbsoluteR = absoluteR;

        //translate canvas origin point to the middle of this view
        mCanvasMatrix.setTranslate(w / 2, h / 2);

        absoluteCentX = w / 2;
        absoluteCentY = h / 2;
    }

    public void updateCenterByRelativeVals(double centX, double centY) {
        if (centX < 0) centX = 0;
        else if (centX > 1) centX = 1;

        if (centY < 0) centY = 0;
        else if (centY > 1) centY = 1;
        float absCentX = (float) (mBitmap.getWidth() * centX);
        float absCentY = (float) (mBitmap.getHeight() * centY);
        updateCenterByAbsoluteVals(absCentX, absCentY);
    }

    public void updateCenterByAbsoluteVals(float absCentX, float absCentY) {
        absoluteCentX = absCentX;
        absoluteCentY = absCentY;
        update();
    }

    public void setBitmapScale(float scale) {
        mScale = scale;
        update();
    }

    protected void update() {
        if (mBitmap == null) return;
        mScaleMatrix.setTranslate(-absoluteCentX, -absoluteCentY);
        mScaleMatrix.postScale(mScale, mScale);
        mBitmapPaint.getShader().setLocalMatrix(mScaleMatrix);
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) return;

        // translate to middle of the view
        canvas.concat(mCanvasMatrix);

        // draw bitmap in the circle
        canvas.drawCircle(0, 0, absoluteR, mBitmapPaint);

        // draw center point
        canvas.drawCircle(0, 0, 5, mCenterPaint);

        //draw side line
        canvas.drawCircle(0, 0, absoluteR - mSidelineWidth / 2, mSidelinePaint);
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
        invalidate();
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
        postInvalidateOnAnimation();
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

    /**
     * appear without animations
     */
    public void appearFast() {
        setAlpha(1);
        setAbsoluteR(backupAbsoluteR);
    }

    /**
     * disappear without animations
     */
    public void disappearFast() {
        setAlpha(0);
        setAbsoluteR(0);
    }

    private void prepareAppearDisappearAnim(Animator animR, Animator animAlpha) {
        mAppearDisappearAnim = new AnimatorSet();
        mAppearDisappearAnim.setDuration(150);
        mAppearDisappearAnim.setInterpolator(new DecelerateInterpolator());
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
        Animator animR = ObjectAnimator.ofFloat(this, "absoluteR", getAbsoluteR(), backupAbsoluteR);
        Animator animAlpha = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 1);
        prepareAppearDisappearAnim(animR, animAlpha);
        mAppearDisappearAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onBeforeAppear();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onAppeared();
                }
            }
        });
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
        Animator animR = ObjectAnimator.ofFloat(this, "absoluteR", getAbsoluteR(), 0);
        Animator animAlpha = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 0);
        prepareAppearDisappearAnim(animR, animAlpha);
        mAppearDisappearAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onBeforeDisappear();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnAppearDisappearListener != null) {
                    mOnAppearDisappearListener.onDisappeared();
                }
            }
        });
        mAppearDisappearAnim.start();
    }

    public void setOnAppearDisappearListener(OnAppearDisappearListener listener) {
        mOnAppearDisappearListener = listener;
    }

    public interface OnAppearDisappearListener {
        void onBeforeAppear();

        void onBeforeDisappear();

        void onAppeared();

        void onDisappeared();
    }

}
