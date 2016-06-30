package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by ZENG Yuhao on 30/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class MagnifierImageView extends ImageView {
    private int mWidth, mHeight;

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

    public MagnifierImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null)
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                }
            });
    }
}
