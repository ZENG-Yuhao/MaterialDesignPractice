package com.esigelec.zengyuhao.materialdesignpractice.Core.Helper;

import android.graphics.Bitmap;
import android.view.View;

import java.util.ArrayList;

/**
 * <p>
 * Created by ZENG Yuhao on 05/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class GPSLocatorHelper {
    private Bitmap mBitmap;

    public GPSLocatorHelper(View attachedView, View... locatorViews) {
        this(attachedView, null, locatorViews);
    }

    public GPSLocatorHelper(View attachedView, Bitmap bitmap, View... locatorViews) {
        mBitmap = bitmap;
        init(attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(View attachedView, ArrayList<View> locatorViewList) {
        this(attachedView, null, locatorViewList);
    }

    public GPSLocatorHelper(View attachedView, Bitmap bitmap, ArrayList<View> locatorViewList) {
        mBitmap = bitmap;
        init(attachedView, convertToLocators(locatorViewList));
    }

    public GPSLocatorHelper(View attachedView, Locator... locators) {
        this(attachedView, null, locators);
    }


    public GPSLocatorHelper(View attachedView, Bitmap bitmap, Locator... locators) {
        mBitmap = bitmap;
        init(attachedView, convertToLocators(locators));
    }

    /**
     * Root Initialization
     *
     * @param attachedView attached view.
     * @param locatorList  locator list.
     */
    public void init(View attachedView, ArrayList<Locator> locatorList) {

    }

    public ArrayList<Locator> convertToLocators(View[] views) {
        return null;
    }

    public ArrayList<Locator> convertToLocators(ArrayList<View> views) {
        return null;
    }

    public ArrayList<Locator> convertToLocators(Locator[] locators) {
        return null;
    }

    public static class Locator {
        /**
         * pivot position mPivotGravity
         */
        public static final int LEFT_TOP = 0;
        public static final int LEFT_CENTER = 1;
        public static final int LEFT_BOTTOM = 2;
        public static final int TOP_CENTER = 3;
        public static final int RIGHT_TOP = 4;
        public static final int RIGHT_CENTER = 5;
        public static final int RIGHT_BOTTOM = 6;
        public static final int CENTER = 7;
        public static final int USER_CUSTOM = 8;

        private int mPivotGravity = CENTER;
        /**
         * relative values of pivot(X,Y), these values are available only when
         * {@link #mPivotGravity}={@link #USER_CUSTOM}. <br>
         * [0, 1] --- pivot is inside the locatorView <br>
         * (1, infinite+) --- pivot is outside the locatorView <br>
         * -1 --- invalid value
         */
        private float pivotX = -1, pivotY = -1;

        /**
         * pivot(X, Y) in pixels, these values are available only when {@link #mPivotGravity}={@link #USER_CUSTOM}.
         */
        private int pxPivotX = -1, pxPivotY = -1;

        private View locatorView;

        public Locator(View locatorView, int gravity) {
            this.locatorView = locatorView;
            this.mPivotGravity = gravity;
        }

        public Locator(View locatorView, float pivotX, float pivotY) {
            this.locatorView = locatorView;
            this.pivotX = pivotX;
            this.pivotY = pivotY;
        }

        public Locator(View locatorView, int pxPivotX, int pxPivotY) {
            this.locatorView = locatorView;
            this.pxPivotX = pxPivotX;
            this.pxPivotY = pxPivotY;
        }

        public int getPivotGravity(){
            return mPivotGravity;
        }

        public float getPivotX(){
            return pivotX;
        }

        public float getPivotY(){
            return pivotY;
        }

        public int getPxPivotX(){
            return pxPivotX;
        }

        public int getPxPivotY(){
            return pxPivotY;
        }
    }
}
