package com.esigelec.zengyuhao.materialdesignpractice.AutoHideView;

import android.animation.Animator;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * <p>
 * Created by ZENG Yuhao on 02/08/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class AutoHideHelper {
    private View mTargetView, mParentView;
    private boolean isHidden;
    private Interpolator mShowInterpolator, mHideInterpolator;
    private Animator mShowAnimator, mHideAnimator;
    private Rect mInitPosition, mFinalPosition, mCurrentPosition;

    public AutoHideHelper(View targetView) {
        mTargetView = targetView;
        if (mTargetView.getParent() == null) throw new RuntimeException("Error: target view must be attached to a " +
                "parent");
        mParentView = (View) mTargetView.getParent();
        mTargetView.getLocalVisibleRect(mInitPosition);
        mShowInterpolator = new AccelerateDecelerateInterpolator();
        mHideInterpolator = new AccelerateDecelerateInterpolator();

    }

    public void calculateFinalPosition() {

    }

    public void hide() {
        if (isHidden) return;
        mTargetView.getLocalVisibleRect(mCurrentPosition);


    }

    public void show() {
        if (!isHidden) return;
    }

    public void setShowInterpolator(Interpolator interpolator) {
        mShowInterpolator = interpolator;
    }

    public void setHideInterpolator(Interpolator interpolator) {
        mHideInterpolator = interpolator;
    }

}
