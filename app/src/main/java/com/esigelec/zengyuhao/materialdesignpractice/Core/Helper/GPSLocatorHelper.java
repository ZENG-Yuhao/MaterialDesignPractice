package com.esigelec.zengyuhao.materialdesignpractice.Core.Helper;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.MagnifierView;

import java.util.ArrayList;

/**
 * <p>
 * Created by ZENG Yuhao on 05/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class GPSLocatorHelper {
    private static final String TAG = "GPSLocatorHelper";

    private View mAttachedView;
    /**
     * root view of the attached view.
     */
    private View mRootView;
    private ArrayList<Locator> mLocators;

    private Bitmap mBitmap;
    private boolean useCustomBitmap = false;

    //private Context mContext;
    private MagnifierView mMagnifier;
    private int mMagnifierWidth = 600, mMagnifierHeight = 600;

    private ActionsCoordinator mActionsCoordinator;


    public GPSLocatorHelper(Context context, View attachedView, View... locatorViews) {
        this(context, attachedView, null, locatorViews);
    }

    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, View... locatorViews) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, ArrayList<View> locatorViews) {
        this(context, attachedView, null, locatorViews);
    }

    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, ArrayList<View> locatorViews) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, Locator... locators) {
        this(context, attachedView, null, locators);
    }


    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, Locator... locators) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locators));
    }

    public static ArrayList<Locator> convertToLocators(View[] views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.length);
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    public static ArrayList<Locator> convertToLocators(ArrayList<View> views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.size());
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    public static ArrayList<Locator> convertToLocators(Locator[] locators) {
        if (locators == null || locators.length <= 0) return null;

        ArrayList<Locator> list = new ArrayList<>(locators.length);
        for (Locator locator : locators) {
            list.add(locator);
        }
        return list;
    }

    /**
     * Root Initialization
     *
     * @param attachedView attached view.
     * @param locators     locator list.
     */
    public void init(Context context, View attachedView, ArrayList<Locator> locators) {
        if (attachedView == null)
            throw new IllegalArgumentException("The attachedView cannot be null.");

        if (!attachedView.isAttachedToWindow())
            throw new RuntimeException("The attachedView should be attached to window");

        if (useCustomBitmap && mBitmap == null)
            throw new RuntimeException("Attached bitmap cannot be null");

        if (!useCustomBitmap && !(attachedView instanceof ImageView))
            throw new RuntimeException("If attachedView is not an ImageView, you should bind a Bitmap in " +
                    "constructor arguments.");

        // view and locators
        mAttachedView = attachedView;
        mRootView = attachedView.getRootView();
        mLocators = locators;
        attach((ViewGroup) mRootView, mLocators);

        // bitmap
        if (!useCustomBitmap) {
            Drawable drawable = ((ImageView) attachedView).getDrawable();
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        //  magnifier
        mMagnifier = new MagnifierView(context);
        mMagnifier.setSize(mMagnifierWidth, mMagnifierHeight);
        mMagnifier.setElevation(16);
        mMagnifier.bindBitmap(mBitmap);
        mMagnifier.disappearFast();
        ((ViewGroup) mRootView).addView(mMagnifier);

        // coordinator
        mActionsCoordinator = new ActionsCoordinator(this);
    }


    private static void attach(ViewGroup parent, ArrayList<Locator> locators) {
        for (Locator locator : locators) {
            locator.attachTo(parent);
        }
    }

    public Locator getLocatorAt(int position) {
        return null;
    }

    public void removeLocatorAt(int position) {

    }

    public void setPivotGravityForAllLocators(int gravity) {

    }

    public static void removeParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(view);
            }
        }
    }

    public static class ActionsCoordinator {
        private GPSLocatorHelper mGPSLocatorHelper;
        private Locator mFocusLocator;
        private int mIndexOfFocusLocator;

        public ActionsCoordinator(GPSLocatorHelper helper) {
            mGPSLocatorHelper = helper;
        }

        void focusOn(int index) {
            mIndexOfFocusLocator = index;
            mFocusLocator = mGPSLocatorHelper.mLocators.get(index);
            focusOn(mFocusLocator, mIndexOfFocusLocator);

        }

        void focusOn(Locator locator) {
            mFocusLocator = locator;
            mIndexOfFocusLocator = mGPSLocatorHelper.mLocators.indexOf(locator);
            focusOn(mFocusLocator, mIndexOfFocusLocator);
        }

        private void focusOn(Locator locator, int indexOfFocus) {
            locator.hide();
            locatorsDisappear();
            mGPSLocatorHelper.mMagnifier.appear();
            onFocused(locator, indexOfFocus);
        }

        void locatorsDisappear() {
            int index = 0;
            for (Locator locator : mGPSLocatorHelper.mLocators) {
                if (locator != mFocusLocator) {
                    onLocatorDisappear(locator, index);
                }
                index++;
            }
            onAllLocatorsDisappeared();
        }


        protected void onLocatorDisappear(Locator locator, int position) {
            locator.hide();
        }

        protected void onAllLocatorsDisappeared() {

        }

        protected void onFocused(Locator focusLocator, int indexOfFocus) {

        }

        void clearFocus() {
            mFocusLocator.show();
            mGPSLocatorHelper.mMagnifier.disappear();
            locatorsAppear();
            onFocusCleared(mFocusLocator, mIndexOfFocusLocator);
        }

        void locatorsAppear() {
            int index = 0;
            for (Locator locator : mGPSLocatorHelper.mLocators) {
                if (locator != mFocusLocator) {
                    onLocatorAppear(locator, index);
                }
                index++;
            }
            onAllLocatorsAppeared();
        }

        protected void onLocatorAppear(Locator locator, int position) {
            locator.show();
        }

        protected void onAllLocatorsAppeared() {

        }

        protected void onFocusCleared(Locator oldFocusLocator, int oldIndexOfFocus) {

        }


        void positionLocator(float rawX, float rawY) {
            float[] pivot = Locator.calculatePivot(mFocusLocator);
            if (pivot == null) return;
            float offsetX = -pivot[0];
            float offsetY = -pivot[1];
            mFocusLocator.contentView.setX(rawX + offsetX);
            mFocusLocator.contentView.setY(rawY + offsetY);
        }

        void moveMagnifier(float rawX, float rawY, float centX, float centY) {
            MagnifierView magnifier = mGPSLocatorHelper.mMagnifier;
            float offsetX = -magnifier.getWidth() / 2;
            float offsetY = -magnifier.getHeight();
            magnifier.setX(rawX + offsetX);
            magnifier.setY(rawY + offsetY);
            magnifier.updateCenterByRelativeVals(centX, centY);
        }


    }


    public static class Locator {
        /**
         * pivot position mPivotGravity
         */
        public static final int GRAVITY_LEFT_TOP = 0;
        public static final int GRAVITY_LEFT_CENTER = 1;
        public static final int GRAVITY_LEFT_BOTTOM = 2;
        public static final int GRAVITY_RIGHT_TOP = 3;
        public static final int GRAVITY_RIGHT_CENTER = 4;
        public static final int GRAVITY_RIGHT_BOTTOM = 5;
        public static final int GRAVITY_CENTER_TOP = 6;
        public static final int GRAVITY_CENTER = 7;
        public static final int GRAVITY_CENTER_BOTTOM = 8;
        public static final int GRAVITY_USER_CUSTOM = 9;

        /**
         * gravity of position of pivot in {@link #contentView}, with this attribute, the position of locator can be
         * easily set without specific pivot values.
         */
        protected int mPivotGravity;

        /**
         * relative values of pivot(X,Y), these values are available only when
         * {@link #mPivotGravity}={@link #GRAVITY_USER_CUSTOM}. <br>
         * [0, 1] --- pivot is inside the contentView <br>
         * (1, infinite+) --- pivot is outside the contentView <br>
         * (-infinite, 0) --- invalid value
         * <p/>
         * <b>A pivot is a reference point that represents the gravity center of this locator, in most case, this is
         * also the point that indicates the real location where this locator is.</b>
         */
        protected float pivotX = -1, pivotY = -1;

        /**
         * pivot(X, Y) in pixels, these values are available only when {@link #mPivotGravity}={@link #GRAVITY_USER_CUSTOM}.
         */
        protected float pxPivotX = -1, pxPivotY = -1;

        protected View contentView;

        /**
         * view attached to this locator, this view will show a location marker
         *
         * @param contentView
         */
        public Locator(View contentView) {
            this(contentView, GRAVITY_CENTER);
        }

        /**
         * @param contentView view attached to this locator, this view will show a location marker
         * @param gravity     gravity of position of pivot in {@link #contentView}, with this attribute,  the
         *                    position of locator can be easily set without specific pivot values.
         */
        public Locator(View contentView, int gravity) {
            this.contentView = contentView;
            // GRAVITY_USER_CUSTOM setup without indicate pivot is not allowed, it will be set the pivot gravity to GRAVITY_CENTER
            if (gravity == GRAVITY_USER_CUSTOM)
                this.mPivotGravity = GRAVITY_CENTER;
            else
                this.mPivotGravity = gravity;
        }

        /**
         * @param contentView            view attached to this locator, this view will show a location marker
         * @param pivotX                 pivotX
         * @param pivotY                 pivotY
         * @param attachToRelativeValues whether the indicated pivot should be attached as relative values
         */
        public Locator(View contentView, float pivotX, float pivotY, boolean attachToRelativeValues) {
            this.contentView = contentView;
            setPivotGravity(GRAVITY_USER_CUSTOM, pivotX, pivotY, attachToRelativeValues);
        }

        public void setPivotGravity(int gravity) {
            // GRAVITY_USER_CUSTOM setup without indicate pivot is not allowed
            if (gravity == GRAVITY_USER_CUSTOM) return;
            mPivotGravity = gravity;
        }

        public void setPivotGravity(int gravity, float pivotX, float pivotY, boolean attachToRelativeValues) {
            mPivotGravity = gravity;

            // pivotX, pivotY are valid only when gravity is set to GRAVITY_USER_CUSTOM
            if (gravity == GRAVITY_USER_CUSTOM) {
                if (attachToRelativeValues) {
                    this.pivotX = pivotX;
                    this.pivotY = pivotY;

                    // invalid the other pair of values
                    this.pxPivotX = -1;
                    this.pxPivotY = -1;
                } else {
                    this.pxPivotX = pivotX;
                    this.pxPivotY = pivotY;

                    // invalid the other pair of values
                    this.pivotX = -1;
                    this.pivotX = -1;
                }
            }
        }

        public void attachTo(ViewGroup parent) {
            removeParent();
            parent.addView(contentView);
        }

        public void removeParent() {
            GPSLocatorHelper.removeParent(contentView);
        }

        public void addAnimation(Animator animator) {
            animator.setTarget(contentView);
        }

        public void clearAnimation() {
            contentView.clearAnimation();
        }

        public void show() {
            contentView.setVisibility(View.VISIBLE);
        }

        public void hide() {
            contentView.setVisibility(View.INVISIBLE);
        }

        public int getPivotGravity() {
            return mPivotGravity;
        }

        public float getPivotX() {
            return pivotX;
        }

        public float getPivotY() {
            return pivotY;
        }

        public float getPxPivotX() {
            return pxPivotX;
        }

        public float getPxPivotY() {
            return pxPivotY;
        }

        public void setX(float x) {
            contentView.setX(x);
        }

        public float getX() {
            return contentView.getX();
        }

        public void setY(float y) {
            contentView.setY(y);
        }

        public float getY() {
            return contentView.getY();
        }

        public static float[] calculatePivot(Locator locator) {
            if (locator == null || locator.contentView == null) return null;
            ViewGroup.LayoutParams lp = locator.contentView.getLayoutParams();
            if (lp == null) return null;

            int width = lp.width;
            int height = lp.height;

            float[] pivot = new float[2];
            switch (locator.mPivotGravity) {
                case GRAVITY_LEFT_TOP:
                    break;
                case GRAVITY_LEFT_CENTER:
                    break;
                case GRAVITY_LEFT_BOTTOM:
                    break;
                case GRAVITY_RIGHT_TOP:
                    break;
                case GRAVITY_RIGHT_CENTER:
                    break;
                case GRAVITY_RIGHT_BOTTOM:
                    break;
                case GRAVITY_CENTER_TOP:
                    break;
                case GRAVITY_CENTER:
                    break;
                case GRAVITY_CENTER_BOTTOM:
                    break;
                case GRAVITY_USER_CUSTOM:
                    if (locator.getPxPivotX() != -1 && locator.getPxPivotY() != -1) {
                        pivot[0] = locator.getPxPivotX();
                        pivot[1] = locator.getPxPivotX();
                    } else if (locator.getPivotX() != -1 && locator.getPivotY() != -1) {
                        pivot[0] = locator.getPivotX() * locator.contentView.getWidth();
                        pivot[0] = locator.getPivotY() * locator.contentView.getHeight();
                    } else {
                        pivot = null;
                    }
                    break;
                default:
                    pivot = null;
            }
            return pivot;
        }
    }
}
