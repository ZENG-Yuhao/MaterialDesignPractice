package com.esigelec.zengyuhao.materialdesignpractice.Core.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    public ArrayList<Locator> convertToLocators(View[] views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.length);
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    public ArrayList<Locator> convertToLocators(ArrayList<View> views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.size());
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    public ArrayList<Locator> convertToLocators(Locator[] locators) {
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

        mAttachedView = attachedView;
        mRootView = attachedView.getRootView();
        mLocators = locators;
        attach((ViewGroup) mRootView, mLocators);

        if (!useCustomBitmap) {
            Drawable drawable = ((ImageView) attachedView).getDrawable();
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        mMagnifier = new MagnifierView(context);
        mMagnifier.setSize(mMagnifierWidth, mMagnifierHeight);
        mMagnifier.setElevation(16);
        mMagnifier.disappearFast();
        ((ViewGroup) mRootView).addView(mMagnifier);

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
         * gravity of position of pivot in {@link #locatorView}, with this attribute, the position of locator can be
         * easily set without specific pivot values.
         */
        private int mPivotGravity;

        /**
         * relative values of pivot(X,Y), these values are available only when
         * {@link #mPivotGravity}={@link #GRAVITY_USER_CUSTOM}. <br>
         * [0, 1] --- pivot is inside the locatorView <br>
         * (1, infinite+) --- pivot is outside the locatorView <br>
         * -1 --- invalid value
         * <p/>
         * <b>A pivot is a reference point that represents the gravity center of this locator, in most case, this is
         * also the point that indicates the real location where this locator is.</b>
         */
        private float pivotX = -1, pivotY = -1;

        /**
         * pivot(X, Y) in pixels, these values are available only when {@link #mPivotGravity}={@link #GRAVITY_USER_CUSTOM}.
         */
        private float pxPivotX = -1, pxPivotY = -1;

        private View locatorView;

        /**
         * view attached to this locator, this view will show a location marker
         *
         * @param locatorView
         */
        public Locator(View locatorView) {
            this(locatorView, GRAVITY_CENTER);
        }

        /**
         * @param locatorView view attached to this locator, this view will show a location marker
         * @param gravity     gravity of position of pivot in {@link #locatorView}, with this attribute,  the
         *                    position of locator can be easily set without specific pivot values.
         */
        public Locator(View locatorView, int gravity) {
            this.locatorView = locatorView;
            // GRAVITY_USER_CUSTOM setup without indicate pivot is not allowed, it will be set the pivot gravity to GRAVITY_CENTER
            if (gravity == GRAVITY_USER_CUSTOM)
                this.mPivotGravity = GRAVITY_CENTER;
            else
                this.mPivotGravity = gravity;
        }

        /**
         * @param locatorView            view attached to this locator, this view will show a location marker
         * @param pivotX                 pivotX
         * @param pivotY                 pivotY
         * @param attachToRelativeValues whether the indicated pivot should be attached as relative values
         */
        public Locator(View locatorView, float pivotX, float pivotY, boolean attachToRelativeValues) {
            this.locatorView = locatorView;
            if (attachToRelativeValues) {
                this.pivotX = pivotX;
                this.pivotY = pivotY;
            } else {
                this.pxPivotX = pivotX;
                this.pxPivotY = pivotY;
            }
            mPivotGravity = GRAVITY_USER_CUSTOM;
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
                } else {
                    this.pxPivotX = pivotX;
                    this.pxPivotY = pivotY;
                }
            }
        }

        public void attachTo(ViewGroup parent) {
            removeParent();
            parent.addView(locatorView);
        }

        public void removeParent() {
            GPSLocatorHelper.removeParent(locatorView);
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
            locatorView.setX(x);
        }

        public float getX() {
            return locatorView.getX();
        }

        public void setY(float y) {
            locatorView.setY(y);
        }

        public float getY() {
            return locatorView.getY();
        }
    }
}
