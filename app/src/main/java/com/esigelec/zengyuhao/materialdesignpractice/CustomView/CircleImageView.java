package com.esigelec.zengyuhao.materialdesignpractice.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * An custom ImageView that draws drawables/bitmaps in circle. Unlike most of custom views of this kind, this view
 * modifies onDraw() according to original implementation of ImageView to draw circle rather than overriding it
 * completely, thus, almost all features of ImageView are kept.
 * <p>
 * Created by ZENG Yuhao on 01/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class CircleImageView extends ImageView {
    private static final String TAG = "CircleImageView";
    private Paint mPaint;
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private Drawable mDrawable, mDrawableCompared;
    private int mDrawableWidth, mDrawableHeight;
    private Matrix mDrawMatrix;
    private int mPaddingTop, mPaddingLeft, mPaddingRight, mPaddingBottom;
    private int mLeft, mTop, mRight, mBottom;
    private boolean mCropToPadding;
    private int mScrollX, mScrollY;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setColor(Color.TRANSPARENT);
//        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
//        mPaint.setXfermode(mode);

        initShape();
    }

    protected void initShape() {
        OvalShape shape_circle = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(shape_circle);
        shapeDrawable.getPaint().setColor(getResources().getColor(android.R.color.white));
        this.setBackground(shapeDrawable);
    }

    protected void setup() {
        mLeft = getLeft();
        mTop = getTop();
        mRight = getRight();
        mBottom = getBottom();
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }


    protected void onPreDraw() {
        mDrawable = getDrawable();
        mDrawableWidth = mDrawable.getIntrinsicWidth();
        mDrawableHeight = mDrawable.getIntrinsicHeight();
        mDrawMatrix = getImageMatrix();

        // variables below are hidden in source code, so these attachment are added by conjecture, they do work fine,
        // but they may not reliable.
        mScrollX = getScrollX();
        mScrollY = getScrollY();
        mCropToPadding = getCropToPadding();
        /*
          #variables below are got in setup()#
          mLeft = getLeft();
          mTop = getTop();
          mRight = getRight();
          mBottom = getBottom();
          mPaddingLeft = getPaddingLeft();
          mPaddingTop = getPaddingTop();
          mPaddingRight = getPaddingRight();
          mPaddingBottom = getPaddingBottom();
        */

    }

    /**
     * Most parts of this method are directly copied from {@link ImageView#onDraw(Canvas)} for keeping as many as
     * possible features of ImageView.  because there are many variables are defined as private in ImageView, so
     * {@link #onPreDraw()} is added to make those variables being attached.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        onPreDraw();
        if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return;     // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && mPaddingTop == 0 && mPaddingLeft == 0) {
            //mDrawable.draw(canvas);  //-- replaced by startDrawing()
            startDrawing(canvas);
        } else {
            int saveCount = canvas.getSaveCount();
            canvas.save();

            if (mCropToPadding) {
                Log.i(TAG, "onDraw: " + "mCropToPadding=" + mCropToPadding);
                final int scrollX = mScrollX;
                final int scrollY = mScrollY;
                canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop, scrollX + mRight - mLeft -
                        mPaddingRight, scrollY + mBottom - mTop - mPaddingBottom);
            }
            // Panning from origin of coordinate system to new origin
            canvas.translate(mPaddingLeft, mPaddingTop);

            if (mDrawMatrix != null) {
                canvas.concat(mDrawMatrix);
            }
            //mDrawable.draw(canvas);  //-- replaced by startDrawing()
            startDrawing(canvas);
            canvas.restoreToCount(saveCount);
        }

    }

    protected void startDrawing(Canvas canvas) {
        Log.i(TAG, "onDraw");
        if (mDrawable != mDrawableCompared) {
            Log.i(TAG, "onDraw: " + "mDrawable!=mDrawableCompared");
            Log.i(TAG, "onDraw: " + getWidth() + " " + getHeight() + " " + mDrawableWidth + " " + mDrawableHeight);
            mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
            //mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(mBitmapShader);
            // update reference
            mDrawableCompared = mDrawable;
        }

        // Inside ImageView, the difference between view's size and canvas/drawable's size was already calculated and
        // relative adaptive changes are reflected in mDrawMatrix, so we don't have to care about paddings, pannings
        // etc. the only thing we have to do is simply taking intrinsic width and height, and using them to calculate
        // center of the circle, that's why we use mDrawableWidth & mDrawableHeight rather than getWidth() &
        // getHeight().
        canvas.drawCircle(mDrawableWidth / 2, mDrawableHeight / 2, Math.min(mDrawableWidth / 2, mDrawableHeight / 2),
                mPaint);
    }

    public void onDrawTemp(Canvas canvas) {
        //canvas.clipPath(mCirclePath);
        if (mDrawable == null || getDrawable() != mDrawable) {
            Log.i(TAG, "onDraw: " + getWidth() + " " + getHeight());
            mDrawable = getDrawable();
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
            //treatBitmap(mBitmap);
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mPaint.setShader(mBitmapShader);

        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 4, mPaint);
//
//        if (mDrawable == null || getDrawable() != mDrawable) {
//            Log.i(TAG, "onDraw: " + getWidth() + " " + getHeight());
//
//            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
//            mBitmap = treatBitmap(mBitmap);
//            BitmapDrawable bd = new BitmapDrawable(getResources(), mBitmap);
//            setImageDrawable(bd);
//            mDrawable = bd;
//        }
    }

    public static Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(target);

        canvas.drawCircle(min / 2, min / 2, min / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public static Bitmap treatBitmap(Bitmap bitmap) {
        Bitmap bm = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        double centerX = bitmap.getWidth() / 2;
        double centerY = bitmap.getHeight() / 2;
        double radius = Math.min(centerX, centerY) * 0.8;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                if (isOverDistance(centerX, centerY, i, j, radius))
                    bm.setPixel(i, j, Color.TRANSPARENT);
            }
        }
        return bm;
    }

    public static boolean isOverDistance(double x, double y, double m, double n, double r) {
        double dx = Math.abs(x - m);
        if (dx > r) return true;
        double dy = Math.abs(y - n);
        if (dy > r) return true;

        if (dx + dy <= r) return false;

        double distance = dx * dx + dy * dy;
        if (distance > r * r) return true;

        return false;
    }
}
