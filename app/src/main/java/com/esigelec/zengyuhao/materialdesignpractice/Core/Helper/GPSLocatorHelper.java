package com.esigelec.zengyuhao.materialdesignpractice.Core.Helper;

import android.graphics.Bitmap;
import android.view.View;

import java.lang.reflect.Array;
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
        // pivot
        public float pivotX, pivotY;
        public View locatorView;

        public Locator() {

        }
    }
}
