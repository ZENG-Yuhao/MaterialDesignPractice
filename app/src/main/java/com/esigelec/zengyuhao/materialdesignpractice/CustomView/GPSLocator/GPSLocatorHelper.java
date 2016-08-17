package com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Size;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * <p>
 * Created by ZENG Yuhao on 05/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class GPSLocatorHelper implements Observer {
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
    private int mMagnifierWidth = 700, mMagnifierHeight = 700;

    private ActionCoordinator mActionCoordinator;
    private OnLocatorPositionListener mOnLocatorPositionListener;

    public GPSLocatorHelper(Context context, View attachedView, View... locatorViews) {
        useCustomBitmap = false;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, View... locatorViews) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, ArrayList<View> locatorViews) {
        useCustomBitmap = false;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, ArrayList<View> locatorViews) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locatorViews));
    }

    public GPSLocatorHelper(Context context, View attachedView, Locator... locators) {
        useCustomBitmap = false;

        init(context, attachedView, convertToLocators(locators));
    }

    public GPSLocatorHelper(Context context, View attachedView, Bitmap bitmap, Locator... locators) {
        mBitmap = bitmap;
        useCustomBitmap = true;
        init(context, attachedView, convertToLocators(locators));
    }

    /**
     * Generate an ArrayList of locators by an array of views.
     *
     * @param views array of the views that will be held by the locators
     * @return ArrayList of locators
     */
    public static ArrayList<Locator> convertToLocators(View[] views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.length);
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    /**
     * Generate an ArrayList of locators by an ArrayList of views.
     *
     * @param views ArrayList of the views that will be held by the locators
     * @return ArrayList of locators
     */
    public static ArrayList<Locator> convertToLocators(ArrayList<View> views) {
        if (views == null) return null;

        ArrayList<Locator> locators = new ArrayList<>(views.size());
        for (View view : views) {
            Locator locator = new Locator(view, Locator.GRAVITY_CENTER_BOTTOM);
            locators.add(locator);
        }
        return locators;
    }

    /**
     * Generate an ArrayList of locators by an array of locators.
     *
     * @param locators array of locators
     * @return ArrayList of locators
     */
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

//        if (!attachedView.isAttachedToWindow())
//            throw new RuntimeException("The attachedView should be attached to window");

        if (useCustomBitmap && mBitmap == null)
            throw new RuntimeException("If you want use another bitmap rather than the one in ImageView, then  " +
                    "the attached bitmap cannot be null.");

        if (!useCustomBitmap && !(attachedView instanceof ImageView))
            throw new RuntimeException("If attachedView is not an ImageView, then you should bind a Bitmap in " +
                    "constructor arguments.");

        // view and locators
        initLocators(attachedView, locators);

        //  magnifier
        initMagnifier(context, attachedView);

        // coordinator
        initCoordinator();
    }


    /**
     * Initialization of locators.
     * <p>
     * <b>IMPORTANT:</b>
     * <br>
     * All locators will be attached to the root view of the window where the attachedView is, in order to have
     * global coordinates (rawX, rawY) and to prevent being shaded from other views.
     *
     * @param attachedView view attached in which all locators should locate
     * @param locators     ArrayList of locators
     */
    private void initLocators(View attachedView, ArrayList<Locator> locators) {
        mAttachedView = attachedView;
        mRootView = attachedView.getRootView();
        mLocators = locators;
        attach((ViewGroup) mRootView, mLocators);
    }


    private void initMagnifier(Context context, View attachedView) {
        if (!useCustomBitmap) {
            Drawable drawable = ((ImageView) attachedView).getDrawable();
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        mMagnifier = new MagnifierView(context);
        mMagnifier.setSize(mMagnifierWidth, mMagnifierHeight);
        //mMagnifier.setElevation(32);
        mMagnifier.setScaleRate(2f);
        mMagnifier.bindBitmap(mBitmap);
        mMagnifier.disappearImmediately();
        ((ViewGroup) mRootView).addView(mMagnifier);

    }

    private void initCoordinator() {
        mActionCoordinator = new ActionCoordinator(this);
    }

    /**
     * Get locator at specified index.
     *
     * @param index index in the ArrayList of locators
     * @return locator found if index does not out of index, otherwise return null
     */
    public Locator getLocatorAt(int index) {
        if (mLocators != null && index >= 0 && index < mLocators.size())
            return mLocators.get(index);
        else
            return null;
    }

    public void setPivotGravityForAllLocators(int gravity) {

    }

    /**
     * Set your custom coordinator.
     * @param coordinator ActionCoordinator that extends {@link ActionCoordinator}
     */
    public void setActionsCoordinator(ActionCoordinator coordinator) {
        mActionCoordinator = coordinator;
    }

    /**
     * Add listener that receive callbacks and information when any locator has changed its position.
     * @param listener {@link OnLocatorPositionListener}
     */
    public void setOnLocatorPositionListener(OnLocatorPositionListener listener) {
        mOnLocatorPositionListener = listener;
    }

    public void setFocusLocator(Locator locator) {
        if (mActionCoordinator == null) return;
        mActionCoordinator.focusOn(locator);
    }

    public void setFocusLocator(int index) {
        if (mActionCoordinator == null) return;
        mActionCoordinator.focusOn(index);
    }

    public void clearFocus() {
        if (mActionCoordinator == null || !mActionCoordinator.hasFocus()) return;
        mActionCoordinator.clearFocus();
    }

    public void positionLocator(float rawX, float rawY) {
        if (mActionCoordinator == null || !mActionCoordinator.hasFocus()) return;
        mActionCoordinator.positionLocator(rawX, rawY);
    }


    public void moveMagnifier(float rawX, float rawY, float centX, float centY) {
        if (mActionCoordinator == null || !mActionCoordinator.hasFocus()) return;
        mActionCoordinator.moveMagnifier(rawX, rawY, centX, centY);
    }

    /**
     * This method is called when registered locator's location has changed.
     *
     * @param observable Locator whose location has changed.
     * @param data       array of 4 elements that follow the order: relativeX, relativeY, relativeX in fraction,
     *                   relativeY in
     *                   fraction
     */
    @Override
    public void update(Observable observable, Object data) {
        if (mOnLocatorPositionListener == null) return;
        if (observable instanceof Locator) {
            float[] rawXY = (float[]) data;
            float[] location = getLocationRelatedTo(mAttachedView, rawXY[0], rawXY[1], true);
            Locator locator = (Locator) observable;
            int index = mLocators.indexOf(locator);
            mOnLocatorPositionListener.onPositionChanged(locator, index, location[0], location[1], location[2],
                    location[3]);
        }
    }

    /**
     * Attach a group of locators to a specified ViewGroup parent.
     *
     * @param parent   parent view
     * @param locators array of locators
     */
    private void attach(ViewGroup parent, ArrayList<Locator> locators) {
        for (Locator locator : locators) {
            locator.attachTo(parent);
            locator.addObserver(this);
        }
    }

    /**
     * Get location that relates to the specified view.
     *
     * @param view         reference view
     * @param rawX         x-coordinate on screen
     * @param rawY         y-coordinate on screen
     * @param withPaddings true if reference view's paddings should be taken into account.
     * @return array of 4 elements that follow the order: relativeX, relativeY, relativeX in fraction, relativeY in
     * fraction
     */
    public static float[] getLocationRelatedTo(View view, float rawX, float rawY, boolean withPaddings) {
        float[] location = new float[4];

        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);

        int paddingLeft = 0, paddingTop = 0, paddingRight = 0, paddingBottom = 0;
        if (withPaddings) {
            paddingLeft = view.getPaddingLeft();
            paddingTop = view.getPaddingTop();
            paddingRight = view.getPaddingRight();
            paddingBottom = view.getPaddingBottom();
        }

        // relative X in pixel
        location[0] = rawX - (viewLocation[0] + paddingLeft);
        // relative Y in pixel
        location[1] = rawY - (viewLocation[1] + paddingTop);

        int actualWidth = view.getWidth() - paddingLeft - paddingRight;
        int actualHeight = view.getHeight() - paddingTop - paddingBottom;
        // relative X in fraction
        location[2] = location[0] / actualWidth;
        // relative Y in fraction
        location[3] = location[1] / actualHeight;
        return location;
    }


    /**
     * Class that handles and coordinates actions for each locator and also for the magnifier.
     * <p>
     * This class has just finished minimum necessary implementation. Create class that extends
     * {@link ActionCoordinator} and override these methods below to define more powerful actions in your way:
     * <li>1. {@link ActionCoordinator#onLocatorDisappear(Locator, int, boolean)}</li>
     * <li>2. {@link ActionCoordinator#onPostLocatorsDisappear()}</li>
     * <li>3. {@link ActionCoordinator#onLocatorAppear(Locator, int, boolean)}</li>
     * <li>4. {@link ActionCoordinator#onPostLocatorsAppear()}</li>
     */
    public static class ActionCoordinator {
        private GPSLocatorHelper mGPSLocatorHelper;
        private Locator mFocusLocator;
        private int mIndexOfFocusLocator;

        public ActionCoordinator(GPSLocatorHelper helper) {
            mGPSLocatorHelper = helper;
        }

        void focusOn(int index) {
            if (hasFocus()) return;
            focusOn(mGPSLocatorHelper.mLocators.get(index), index);

        }

        void focusOn(Locator locator) {
            if (hasFocus()) return;
            focusOn(locator, mGPSLocatorHelper.mLocators.indexOf(locator));
        }

        private void focusOn(Locator locator, int index) {
            // update focus
            mIndexOfFocusLocator = index;
            mFocusLocator = locator;
            mFocusLocator.contentView.bringToFront();

            locatorsDisappear();
            mGPSLocatorHelper.mMagnifier.appear();
        }

        void locatorsDisappear() {
            onLocatorDisappear(mFocusLocator, mIndexOfFocusLocator, true);

            int index = 0;
            for (Locator locator : mGPSLocatorHelper.mLocators) {
                if (locator != mFocusLocator) {
                    onLocatorDisappear(locator, index, false);
                }
                index++;
            }
            onPostLocatorsDisappear();
        }

        /**
         * You can override this method to handle disappearing actions such as animations for each locator.
         *
         * @param locator locator on disappearing
         * @param index   index of the locator in the list of locators
         * @param isFocus true if the locator is focus locator
         */
        protected void onLocatorDisappear(Locator locator, int index, boolean isFocus) {
            if (isFocus)
                locator.hide();
        }

        /**
         * Overriding this method is a good chance to intervene the end of processing queue of
         * {@link #onLocatorDisappear(Locator, int, boolean)}.
         * <p>
         * One use case is that you have bound animations for each locator, and you want those animations to be
         * started at the same time. Just put animations into {@link android.animation.AnimatorSet} and start the
         * AnimatorSet here.
         */
        protected void onPostLocatorsDisappear() {
        }

        void clearFocus() {
            mGPSLocatorHelper.mMagnifier.disappear();
            locatorsAppear();
            mFocusLocator = null;
        }

        void locatorsAppear() {
            onLocatorAppear(mFocusLocator, mIndexOfFocusLocator, true);

            int index = 0;
            for (Locator locator : mGPSLocatorHelper.mLocators) {
                if (locator != mFocusLocator) {
                    onLocatorAppear(locator, index, false);
                }
                index++;
            }
            onPostLocatorsAppear();
        }

        /**
         * You can override this method to handle appearing actions such as animations for each locator.
         *
         * @param locator locator on appearing
         * @param index   index of the locator in the list of locators
         * @param isFocus true if the locator is focus locator
         */
        protected void onLocatorAppear(Locator locator, int index, boolean isFocus) {
            if (isFocus)
                locator.show();
        }

        /**
         * Overriding this method is a good chance to intervene the end of processing queue of
         * {@link #onLocatorAppear(Locator, int, boolean)}
         * <p>
         * One use case is that you have bound animations for each locator, and you want those animations to be
         * started at the same time. Just put animations into {@link android.animation.AnimatorSet} and start the
         * AnimatorSet here.
         */
        protected void onPostLocatorsAppear() {
        }

        /**
         * Move current focus locator.
         */
        void positionLocator(float rawX, float rawY) {
            positionLocator(mFocusLocator, rawX, rawY);
        }

        /**
         * Move locator so that locator's pivot coincide with the position specified by rawX and rawY.
         * this method who controls locator's position to ensure that locator's pivot does not exceed bounds of
         * {@link GPSLocatorHelper#mAttachedView}.
         *
         * @param locator locator to be positioned
         * @param rawX    raw data of x-coordinate that locator's pivot should be moved to.
         * @param rawY    raw data of y-coordinate that locator's pivot should be moved to.
         */
        void positionLocator(Locator locator, float rawX, float rawY) {
            Locator.calculatePivot(locator);
            float offsetX = -locator.getPxPivotX();
            float offsetY = -locator.getPxPivotY();

            float[] location = {rawX, rawY};
            correctLocation(mGPSLocatorHelper.mAttachedView, location);

            locator.setXY(location[0] + offsetX, location[1] + offsetY);
        }

        // // TODO: 16/7/17 make offsetX, offsetY configurable.
        void moveMagnifier(float rawX, float rawY, float centX, float centY) {
            MagnifierView magnifier = mGPSLocatorHelper.mMagnifier;
            float offsetX = -magnifier.getWidth() / 2;
            float offsetY = -magnifier.getHeight();

            float[] location = {rawX, rawY};
            correctLocation(mGPSLocatorHelper.mAttachedView, location);
            magnifier.setX(location[0] + offsetX);
            magnifier.setY(location[1] + offsetY);
            magnifier.updateCenterByFractionVals(centX, centY);
        }

        public boolean hasFocus() {
            return (mFocusLocator != null);
        }

        public boolean isFoucus(Locator locator) {
            return (locator == mFocusLocator);
        }

        /**
         * Check whether the specified location is out of borders of the specified view, if yes, correct the location
         * to ensure that it locates in the view. Paddings are taken into account by default.
         *
         * @param view     reference view
         * @param location location to be checked, an array of two integers in which to hold the coordinates, rawX, rawY
         * @return true if location exceeds borders of the view and the location has been corrected.
         */
        static boolean correctLocation(View view, @Size(2) float[] location) {
            return correctLocation(view, location, true);
        }

        /**
         * Check whether the specified location is out of borders of the specified view, if yes, correct the location
         * to ensure that it locates in the view. You can decided whether paddings are taken into account.
         *
         * @param view         reference view
         * @param location     location to be checked, an array of two integers in which to hold the coordinates,
         *                     rawX, rawY
         * @param withPaddings set this argument to true if you want paddings are taken into account when doing the
         *                     correction.
         * @return true if location exceeds borders of the view and the location has been corrected.
         */
        static boolean correctLocation(View view, @Size(2) float[] location, boolean withPaddings) {
            boolean isExceededX = false;
            boolean isExceededY = false;
            int[] viewLocation = new int[2];
            view.getLocationOnScreen(viewLocation);

            int offsetLeft = 0, offsetTop = 0, offsetRight = 0, offsetBottom = 0;
            if (withPaddings) {
                offsetLeft = view.getPaddingLeft();
                offsetTop = view.getPaddingTop();
                offsetRight = -view.getPaddingRight();
                offsetBottom = -view.getPaddingBottom();
            }

            int leftBorder = viewLocation[0] + offsetLeft;
            int topBorder = viewLocation[1] + offsetTop;
            int rightBorder = viewLocation[0] + view.getWidth() + offsetRight;
            int bottomBorder = viewLocation[1] + view.getHeight() + offsetBottom;

            if (location[0] < leftBorder) location[0] = leftBorder;
            else if (location[0] > rightBorder) location[0] = rightBorder;
            else isExceededX = true;

            if (location[1] < topBorder) location[1] = topBorder;
            else if (location[1] > bottomBorder) location[1] = bottomBorder;
            else isExceededY = true;

            return (isExceededX || isExceededY);
        }
    } // END OF: ActionCoordinator


    public static class Locator extends Observable {
        /* pivot position mPivotGravity */
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
         * Fraction values of pivot(X,Y).
         * <p/>
         * <b>
         * A pivot is a reference point that represents the gravity center of this locator, in most case, this is
         * also the point that indicates the real location where this locator points to.
         * </b>
         * <p/>
         * [0, 1] --- pivot is inside the contentView <br>
         * (1, infinite+) --- pivot is outside the contentView <br>
         * (-infinite, 0) --- invalid value
         */
        protected float pivotX = -1, pivotY = -1;

        /**
         * pivot(X, Y) in pixels
         */
        protected float pxPivotX = -1, pxPivotY = -1;

        /**
         * content view held by the locator
         */
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
            // GRAVITY_USER_CUSTOM setup without indicate pivot is not allowed, it will be set the pivot gravity to
            // GRAVITY_CENTER
            if (gravity == GRAVITY_USER_CUSTOM)
                this.mPivotGravity = GRAVITY_CENTER;
            else
                this.mPivotGravity = gravity;
        }

        /**
         * @param contentView            view attached to this locator, this view will show a location marker
         * @param pivotX                 pivotX
         * @param pivotY                 pivotY
         * @param attachToFractionValues whether the specified pivot should be attached as fraction values
         */
        public Locator(View contentView, float pivotX, float pivotY, boolean attachToFractionValues) {
            this.contentView = contentView;
            setPivotGravity(GRAVITY_USER_CUSTOM, pivotX, pivotY, attachToFractionValues);
        }

        /**
         * Set gravity of pivot of locator except {@link #GRAVITY_USER_CUSTOM}
         *
         * @param gravity gravity of pivot
         */
        public void setPivotGravity(int gravity) {
            // GRAVITY_USER_CUSTOM setup without indicate pivot is not allowed
            if (gravity == GRAVITY_USER_CUSTOM) return;
            mPivotGravity = gravity;
        }

        /**
         * Set gravity of pivot of locator, if gravity!={@link #GRAVITY_USER_CUSTOM}, the rest arguments would not be
         * used.
         *
         * @param gravity                gravity of pivot
         * @param pivX                   x-coordinate of pivot
         * @param pivY                   y-coordinate of pivot
         * @param attachToFractionValues true if arguments pivX and pivY represent fraction values
         */
        public void setPivotGravity(int gravity, float pivX, float pivY, boolean attachToFractionValues) {
            mPivotGravity = gravity;

            // pivX, pivY are valid only when gravity is set to GRAVITY_USER_CUSTOM
            if (gravity != GRAVITY_USER_CUSTOM) return;

            if (attachToFractionValues) {
                this.pivotX = pivX;
                this.pivotY = pivY;

                // invalid the other pair of values
                this.pxPivotX = -1;
                this.pxPivotY = -1;
            } else {
                this.pxPivotX = pivX;
                this.pxPivotY = pivY;

                // invalid the other pair of values
                this.pivotX = -1;
                this.pivotX = -1;
            }
        }

        /**
         * Attach {@link #contentView} of this locator to a {@link ViewGroup} parent.
         *
         * @param parent target view to which this locator should be attached
         */
        public void attachTo(ViewGroup parent) {
            removeParent(contentView);
            parent.addView(contentView);
        }

        /**
         * Move locator so that locator's pivot coincide with the position specified (rawX, rawY).
         *
         * @param rawX raw data of x-coordinate that locator's pivot should be moved to.
         * @param rawY raw data of y-coordinate that locator's pivot should be moved to.
         */
        public void positionTo(float rawX, float rawY) {
            float[] pivot = Locator.calculatePivot(this);
            if (pivot == null) return;

            float offsetX = -pivot[0];
            float offsetY = -pivot[1];
            setXY(rawX + offsetX, rawY + offsetY);
        }

        public void bindAnimation(Animator animator) {
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

        public void setXY(float x, float y) {
            contentView.setX(x);
            contentView.setY(y);
            setChanged();
            notifyObservers(new float[]{x + pxPivotX, y + pxPivotY});
//            Log.i("positionTo", "--->left:" + contentView.getLeft() + " top:" + contentView.getTop() + " right:" +
//                    contentView.getRight() + " bottom:" + contentView.getBottom() + " width:" + contentView.getWidth
//                    () + " " + "height:" + contentView.getHeight());
        }

        private void setX(float x) {
            contentView.setX(x);
        }

        public float getX() {
            return contentView.getX();
        }

        private void setY(float y) {
            contentView.setY(y);
        }

        public float getY() {
            return contentView.getY();
        }

        /**
         * Calculate and update pivot (in pixels) of a locator according to its PivotGravity
         *
         * @param locator
         * @return
         */
        public static float[] calculatePivot(Locator locator) {
            if (locator == null || locator.contentView == null) return null;
            ViewGroup.LayoutParams lp = locator.contentView.getLayoutParams();
            if (lp == null) return null;

//            int width = lp.width;
//            int height = lp.height;

            int width = locator.contentView.getWidth();
            int height = locator.contentView.getHeight();
            float[] pivot = new float[2];
            switch (locator.mPivotGravity) {
                case GRAVITY_LEFT_TOP:
                    pivot[0] = 0;
                    pivot[1] = 0;
                    break;
                case GRAVITY_LEFT_CENTER:
                    pivot[0] = 0;
                    pivot[1] = 0.5f * height;
                    break;
                case GRAVITY_LEFT_BOTTOM:
                    pivot[0] = 0;
                    pivot[1] = height;
                    break;
                case GRAVITY_RIGHT_TOP:
                    pivot[0] = width;
                    pivot[1] = 0;
                    break;
                case GRAVITY_RIGHT_CENTER:
                    pivot[0] = width;
                    pivot[1] = 0.5f * height;
                    break;
                case GRAVITY_RIGHT_BOTTOM:
                    pivot[0] = width;
                    pivot[1] = height;
                    break;
                case GRAVITY_CENTER_TOP:
                    pivot[0] = 0.5f * width;
                    pivot[1] = 0;
                    break;
                case GRAVITY_CENTER:
                    pivot[0] = 0.5f * width;
                    pivot[1] = 0.5f * height;
                    break;
                case GRAVITY_CENTER_BOTTOM:
                    pivot[0] = 0.5f * width;
                    pivot[1] = height;
                    break;
                case GRAVITY_USER_CUSTOM:
                    if (locator.getPxPivotX() != -1 && locator.getPxPivotY() != -1) {
                        pivot[0] = locator.getPxPivotX();
                        pivot[1] = locator.getPxPivotX();
                    } else if (locator.getPivotX() != -1 && locator.getPivotY() != -1) {
                        pivot[0] = locator.getPivotX() * width;
                        pivot[0] = locator.getPivotY() * height;
                    } else {
                        pivot = null;
                    }
                    break;
                default:
                    pivot = null;
            }
            if (pivot != null) {
                locator.pxPivotX = pivot[0];
                locator.pxPivotY = pivot[1];
            }
            return pivot;
        }

        /**
         * Remove parent view of a view for that it can be attached to another view.
         *
         * @param view operation view
         */
        static void removeParent(View view) {
            if (view != null) {
                ViewParent parent = view.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(view);
                }
            }
        }
    } // END OF: Locator

    public interface OnLocatorPositionListener {

        /**
         * @param locator the locator whose position has been changed.
         * @param index   index of the locator in {@link #mLocators}
         * @param pxPosX  screen's x-coordinate of pivot in pixels (not the same with pivotX).
         * @param pxPosY  screen's y-coordinate of pivot in pixels (not the same with pivotX).
         * @param posX    fraction value of screen's x-coordinate of pivot (not the same with pivotX).
         * @param posY    fraction value of screen's y-coordinate of pivot (not the same with pivotX).
         */
        void onPositionChanged(Locator locator, int index, float pxPosX, float pxPosY, float posX, float posY);

    }
}
