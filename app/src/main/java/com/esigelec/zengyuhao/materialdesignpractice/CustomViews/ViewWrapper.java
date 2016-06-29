package com.esigelec.zengyuhao.materialdesignpractice.CustomViews;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ZENG Yuhao on 22/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class ViewWrapper {
    public View targetView;

    public ViewWrapper(View target) {
        this.targetView = target;
    }

    public int getWidth() {
        return targetView.getLayoutParams().width;
    }

    public void setWidth(int width) {
        targetView.getLayoutParams().width = width;

            /*
             * When a view is added, there are some methods will be invoke in its lifecycle:
             * -- parent calls addView() --
             * onAttachedToWindow()
             * measure()
             * onMeasure()
             * layout()
             * onLayout()
             * dispatchDraw()
             * draw()
             * onDraw()
             * { invalidate() or requestLayout() }
             *
             * The difference between these two methods is invalidate() recalls dispatchDraw()
             * and requestLayout() restarts from measure()
             *
             */
        targetView.requestLayout();
    }

    public void setMargin(int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(targetView.getLayoutParams());
        params.setMargins(l, t, r, b);

    }

    public void setBottomMargin(int bottomMargin) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(targetView.getLayoutParams());
        params.bottomMargin = bottomMargin;
    }

    public int getBottomMargin() {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(targetView.getLayoutParams());
        return params.bottomMargin;
    }

    public void setRightMargin(int rightMargin) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(targetView.getLayoutParams());
        params.rightMargin = rightMargin;
    }

    public int getRightMargin() {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(targetView.getLayoutParams());
        return params.rightMargin;
    }


}
