package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by ZENG Yuhao on 30/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class MagnifierImageView extends ImageView {
    private Magnifier mMagnifier;
    private Bitmap mBitmap;

    public MagnifierImageView(Context context) {
        super(context);
        init(context);
    }

    public MagnifierImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MagnifierImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mMagnifier != null)
                    mMagnifier.show();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private class Magnifier extends ImageView {

        public Magnifier(Context context) {
            super(context);
        }

        public Magnifier(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Magnifier(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        public void show() {

        }

        public void hide() {

        }
    }
}
