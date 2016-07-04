package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * <p>
 * Created by ZENG Yuhao on 04/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class MagnifierImageView extends ImageView {
    public MagnifierImageView(Context context) {
        super(context);
    }

    public MagnifierImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagnifierImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MagnifierImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public static class Magnifier extends View {
        public static final String TAG = "Magnifier";

        private Bitmap mBitmap;
        private Paint mBitmapPaint, mCenterPaint;
        private BitmapShader mBitmapShader;
        private float absoluteCentX, absoluteCentY, absoluteR;
        private float mScale = 2f;
        private Matrix mScaleMatrix;
        private Matrix mCanvasMatrix;

        public Magnifier(Context context) {
            super(context);
            init(context);
        }

        public Magnifier(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public Magnifier(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        public Magnifier(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(context);
        }

        public void init(Context context) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
            mScaleMatrix = new Matrix();
            mCanvasMatrix = new Matrix();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            absoluteR = Math.min(w, h) / 2;

            //translate canvas origin point to the middle of this view
            mCanvasMatrix.setTranslate(w / 2, h / 2);
        }

        public void updateCenterByRelativeVals(double centX, double centY) {
            if (centX < 0) centX = 0;
            else if (centX > 1) centX = 1;

            if (centY < 0) centY = 0;
            else if (centY > 1) centY = 1;
            Log.i(TAG, "-->updateCenterByRelativeVals" + "  " + mBitmap.getWidth() + " " + mBitmap.getHeight());
            float absCentX = (float) (mBitmap.getWidth() * centX);
            float absCentY = (float) (mBitmap.getHeight() * centY);
            updateCenterByAbsoluteVals(absCentX, absCentY);
        }

        public void updateCenterByAbsoluteVals(float absCentX, float absCentY) {
            absoluteCentX = absCentX;
            absoluteCentY = absCentY;
            Log.i(TAG, "-->updateCenterByAbsoluteVals" + "  " + absoluteCentX + " " + absoluteCentY);
            update();
        }

        public void setBitmapScale(float scale) {
            mScale = scale;
            update();
        }

        public void update() {
            mScaleMatrix.setTranslate(-absoluteCentX, -absoluteCentY);
            mScaleMatrix.postScale(mScale, mScale);
            mBitmapPaint.getShader().setLocalMatrix(mScaleMatrix);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Log.i(TAG, "onDraw" + absoluteCentX + " ## " + absoluteCentY + " ## " + absoluteR);
            if (mBitmap == null) return;

            // translate to middle of the view
            canvas.concat(mCanvasMatrix);
            //canvas.translate(getWidth() / 2, getHeight() / 2);
            // draw
//            mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
//            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            mScaleMatrix.setTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);
//            mScaleMatrix.postScale(2f, 2f);
//            mBitmapShader.setLocalMatrix(mScaleMatrix);
//            mBitmapPaint.setShader(mBitmapShader);
            canvas.drawCircle(0, 0, absoluteR, mBitmapPaint);
            canvas.drawCircle(0, 0, 5, mCenterPaint);

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


    }
}
