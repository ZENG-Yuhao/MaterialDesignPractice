package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SideToolBar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import com.esigelec.zengyuhao.materialdesignpractice.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 */
public class SideToolbar extends FrameLayout {
    /* layout orientation */
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private int mOrientation = HORIZONTAL;

    /* shadow */
    private ImageView mShadowView;
    private boolean isShadowLayoutChanged = true;

    /* Holder and adapter */
    private Adapter mAdapter;
    private ViewHolder[] mHolderList;
    private int mItemCount;  // number of items in mHolderList
    private int mWeightCount;
    private OnItemClickListener mOnItemClickListener;
    private int mCurrentPosition = 0;
    /**
     * If adapter has been newly set, must wipe all layout params when onMeasureViewHolder for secure to avoid margin
     * and padding problems.
     */
    private boolean isAdapterNewlySet = true;
    private boolean isItemLayoutChanged = true;

    /* item on focus & lost focus */
    private int mWeightFocus = 21000;
    private int mWeightNoFocus = 10000;

    /* Synchronizer */
    /**
     * Indicates that the pager is in an idle, settled state. The current page
     * is fully in view and no animation is in progress.
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * Indicates that the pager is currently being dragged by the user.
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * Indicates that the pager is in the process of settling to a final position.
     */
    public static final int SCROLL_STATE_SETTLING = 2;
    // zone state
    public static final int ZONE_LEFT = 0; // middle point of screen located at left side of current page
    public static final int ZONE_RIGHT = 1; // middle point of screen located at right side of current page
    // event state
    public static final int MODE_ITEM_CLICK = 0;
    public static final int MODE_TOUCH_SCROLL = 1;
    private FocusChangeSynchronizer mSynchronizer = new FocusChangeSynchronizer();

    /* ViewPagerScroller */
    public static final int pagerScrollDuration = 150;

    public SideToolbar(Context context) {
        super(context);
        init();
    }

    public SideToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SideToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        initShadow();
    }

    /**
     * Adding view manually for this layout is forbidden for not disturbing the layout. Using this method to add view
     * wouldn't have any effect.
     * <p/>
     * The same as other <b>addView(args..)</b>, for clarity of the codes, other addView methods weren't declared as
     * deprecated, but you still should not use them.
     *
     * @deprecated use {@link #setAdapter(Adapter)} to reset all child-view instead.
     */
    @Deprecated
    @Override
    public void addView(View child) {

    }


    /**
     * Removing view manually for this layout is forbidden for not disturbing the layout. Using this method to remove
     * view wouldn't have any effect.
     * <p/>
     * The same as other <b>removeView(args..)</b>, for clarity of the codes, other removeView methods weren't
     * declared as deprecated, but you still should not use them.
     *
     * @deprecated use {@link #setAdapter(Adapter)} to reset all child-view instead.
     */
    @Deprecated
    @Override
    public void removeAllViews() {

    }

    /**
     * Each item of this toolbar has two states: OnFocus and NoFocus, when an item was selected, its state is
     * OnFocus vice versa. The widths of two states are based on two weights defined here.
     *
     * @param weightFocus   weight for the selected item
     * @param weightNoFocus weight for non-selected items
     */
    public void setWeights(int weightFocus, int weightNoFocus) {
        this.mWeightFocus = weightFocus > 0 ? weightFocus : 1;
        this.mWeightNoFocus = weightNoFocus > 0 ? weightNoFocus : 1;
    }

    /**
     * After doing layout animations, there may be errors of widths, use this method can fix it.
     *
     * @param positionFocus To indicate which items was selected.
     */
    public void correctWeight(int positionFocus) {
        for (int i = 0; i < mItemCount; i++) {
            if (i == positionFocus)
                mHolderList[i].weight = mWeightFocus;
            else
                mHolderList[i].weight = mWeightNoFocus;
        }
    }


    protected void initShadow() {
        mShadowView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.side_toolbar_shadow, this, false);
        super.addView(mShadowView);
    }

    protected void measureShadow() {
        View initialView = mHolderList[0].itemView;
        mShadowView.setLeft(initialView.getLeft());
        mShadowView.setTop(initialView.getTop());
        mShadowView.setRight(initialView.getRight());
        mShadowView.setBottom(initialView.getBottom());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(initialView.getWidth(), initialView.getHeight());
        //lp.width = initialView.getWidth();
        //lp.height = initialView.getHeight();
        mShadowView.setLayoutParams(lp);
        Log.i("haha", "measureShadow----->" + initialView.getWidth() + " " + initialView.getHeight() + " " + lp.width + " " +
                "" + lp.height);
        Log.i("haha", "measureShadow----->" + mShadowView.getLeft() + " " + mShadowView.getTop() + " " + mShadowView
                .getRight() + " " + mShadowView.getBottom());
    }

    /**
     * To set an adapter for creating views of items, defining default actions, getting information etc.
     *
     * @param adapter the adapter to be set.
     */
    public void setAdapter(Adapter<? extends ViewHolder> adapter) {
        mAdapter = adapter;
        isAdapterNewlySet = true;

        if (adapter == null) {
            throw new IllegalArgumentException("adapter may not be null");
        }
        if (adapter.getItemCount() == 0) {
            throw new IllegalArgumentException("item count may not be 0");
        }

        /* init mHolderList through adapter */
        mItemCount = mAdapter.getItemCount();
        mHolderList = new ViewHolder[mItemCount];
        mWeightCount = (mItemCount - 1) * mWeightNoFocus + mWeightFocus;
        for (int i = 0; i < mItemCount; i++) {
            mHolderList[i] = mAdapter.createViewHolder(this);
            mHolderList[i].weight = (i == mCurrentPosition) ? mWeightFocus : mWeightNoFocus;
            mAdapter.bindViewHolder(mHolderList[i], i);
        }

        /* remove all children views when adapter has been changed */
        if (getChildCount() > 0)
            super.removeAllViews();
        initLayout();


    }

    /**
     * To be invoked when first this layout first loads.
     */
    // TODO: 2016/6/15 add OnGlobalLayoutListener to surveille incoming layout changes, and then adjust child-views size
    protected void initLayout() {

        // add views in mHolderList to the layout
        for (int i = 0; i < mItemCount; i++) {
            super.addView(mHolderList[i].itemView);
        }
        int height = getHeight();
        int width = getWidth();

        if (height != 0 && width != 0) {
            //initTabs(height, width);
            requestGlobalLayout();
            measureShadow();

        } else {
            // if height and width get 0 before the first load of the layout, register listener to obtain height and
            // width when onLayout() finished.
            final ViewTreeObserver observer = this.getViewTreeObserver();
            if (observer != null) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //initTabs(getHeight(), getWidth());
                        requestGlobalLayout();
                        measureShadow();

                        // once height and width got, unregister the listener
                        ViewTreeObserver observerLocal = getViewTreeObserver();
                        if (null != observerLocal) {
                            // removeOnGlobalLayoutListener() is only supported by SDK later than JELLY_BEAN
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                                observerLocal.removeGlobalOnLayoutListener(this);
                            else
                                observerLocal.removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }
    }

    /**
     * Measurement for each ViewHolder, make sure getWidth() and getHeight() are nonzero before invoking this method.
     * These measures must be done outside onMeasure() method and before requestLayout() or requestViewHolderLayout().
     */
    protected void onViewHoldersMeasure() {
        if (!isItemLayoutChanged) return;

        int width, height;
        ViewGroup.LayoutParams lp;
        for (int i = 0; i < mItemCount; i++) {
            if (mOrientation == HORIZONTAL) {
                height = getMeasuredHeight();
                width = integerize((double) getMeasuredWidth() * mHolderList[i].weight / mWeightCount);
            } else {
                height = integerize((double) getMeasuredHeight() * mHolderList[i].weight / mWeightCount);
                width = getMeasuredWidth();
            }

            if (isAdapterNewlySet) {
                lp = new FrameLayout.LayoutParams(width, height);
                mHolderList[i].itemView.setLayoutParams(lp);
                isAdapterNewlySet = false;
            } else {
                lp = mHolderList[i].itemView.getLayoutParams();
                lp.width = width;
                lp.height = height;
            }
            Log.i("haha", "onViewHoldersMeasure--->" + lp.width + " " + lp.height);
        }
    }

    /**
     * Method to maintain unified integralization
     *
     * @param value the value to be integerized.
     * @return the value being integerized.
     */
    private int integerize(double value) {
        return (int) Math.ceil(value);
    }

    protected void onViewHolderLayout() {
        if (!isItemLayoutChanged) return;

        ViewGroup.LayoutParams lp;
        int left = 0, top = 0;
        for (int i = 0; i < mItemCount; i++) {
            lp = mHolderList[i].itemView.getLayoutParams();
            if (mOrientation == HORIZONTAL) {
                mHolderList[i].itemView.layout(left, 0, left + lp.width, lp.height);
                left += lp.width;
            } else if (mOrientation == VERTICAL) {
                mHolderList[i].itemView.layout(0, top, lp.width, top + lp.height);
                top += lp.height;
            }
        }


    }

    protected void onShadowLayout() {
        if (!isShadowLayoutChanged) return;

        int left = mShadowView.getLeft();
        int top = mShadowView.getTop();
        int right = left + mShadowView.getWidth();
        int bottom = top + mShadowView.getHeight();
        Log.i("haha", "onShadowLayout----->" + left + " " + top + " " + right + " " + bottom);
        mShadowView.layout(left, top, right, bottom);
    }


    /**
     * Refresh layout changes, in some way, it's equivalent to requestLayout(), because onMeasure() method isn't
     * overridden, so an measure for items before requestLayout() is required. And this method is necessary after each
     * layout animation or layout change.
     */
    public void requestGlobalLayout() {
        onViewHoldersMeasure();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        onViewHolderLayout();
        onShadowLayout();
    }

    /**
     * Bind a listener to handle click event for each item.
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        if (listener == null) return;

        this.mOnItemClickListener = listener;
        // register listener for each holder
        for (int i = 0; i < mItemCount; i++) {
            registerOnItemClickListener(mHolderList[i], i);
        }
    }

    private void registerOnItemClickListener(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (position == mCurrentPosition) return;

                mSynchronizer.onClick(position);

                // call user customized method
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    public void setCurrentItem(int position) {
        mCurrentPosition = position;
        correctWeight(mCurrentPosition);
        requestGlobalLayout();
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void bindViewPager(ViewPager pager) {
        if (mSynchronizer != null)
            mSynchronizer.bindViewPager(pager);
    }

    public void unbindViewPager() {
        if (mSynchronizer != null)
            mSynchronizer.unbindViewPager();
    }

    public static abstract class ViewHolder {
        public View itemView;
        public int position = 0;
        public int weight = 1;

        public ViewHolder(View view) {
            if (view == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            itemView = view;
            // for that the holder can be re retrieved by the view
            itemView.setTag(this);
        }

        public final int getAdapterPosition() {
            return position;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {

        protected VH createViewHolder(ViewGroup parent) {
            VH holder = onCreateViewHolder(parent);
            return holder;
        }

        protected void bindViewHolder(VH holder, int position) {
            holder.position = position;
            onBindViewHolder(holder, position);
        }

        public abstract int getItemCount();

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(VH holder, int position);
    }


    public interface OnItemClickListener {
        /**
         * You should never invoke setCurrentItem() of the bound ViewPager in this callback method, because
         * {@link FocusChangeSynchronizer} will handle it for you. Otherwise, you may risk a conflict.
         *
         * @param view     the view was clicked.
         * @param position the position of the clicked view in this layout.
         */
        void onItemClick(View view, int position);
    }

    /**
     * Helper class for synchronizer the scrolling of the ViewPager bound and items of this toolbar.
     */
    private class FocusChangeSynchronizer implements ViewPager.OnPageChangeListener {

        public int mTriggerMode = MODE_TOUCH_SCROLL;

        public WeakReference<ViewPager> pagerWeakReference;
        public FocusChangedAnimator mItemAnimator;
        public Interpolator mInterpolator;


        public FocusChangeSynchronizer() {
            mInterpolator = new DecelerateInterpolator();
            mItemAnimator = new FocusChangedAnimator();
            mItemAnimator.setInterpolator(mInterpolator);
        }

        /**
         * @return true if there is already a pager bound to the synchronizer.
         */
        public boolean isPagerBound() {
            return (pagerWeakReference != null && pagerWeakReference.get() != null);
        }

        public void bindViewPager(ViewPager pager) {
            if (pager != null) {

                // if there is already one pager bound, unbind it.
                if (isPagerBound())
                    unbindViewPager();
                pagerWeakReference = new WeakReference<>(pager);
                // make sure they are at the same position
                pager.setCurrentItem(mCurrentPosition);
                pager.addOnPageChangeListener(this);
                bindScrollerToViewPager(pager);
            }
        }

        /**
         * Unbind the ViewPager that is currently bound.
         */
        public void unbindViewPager() {
            if (isPagerBound())
                pagerWeakReference.get().removeOnPageChangeListener(this);
        }

        /**
         * Redefine a new scroller to the bound ViewPager to have an unified interpolator and animation/scrolling
         * duration.
         *
         * @param pager
         */
        public void bindScrollerToViewPager(ViewPager pager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                ViewPagerScroller scroller = new ViewPagerScroller(pager.getContext(), mInterpolator);
                mScroller.set(pager, scroller);
            } catch (NoSuchFieldException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }

        public void onClick(int position) {
            mTriggerMode = MODE_ITEM_CLICK;
            onItemSelected(position);
        }

        public void onItemSelected(int position) {
            if (mItemAnimator.isRunning()) {
                mItemAnimator.cancel();
            }
            // Canceling animation must be executed before return
            if (mCurrentPosition == position) return;

            mItemAnimator.start(mCurrentPosition, position);
            if (isPagerBound())
                pagerWeakReference.get().setCurrentItem(position);
        }

        public void moveShadow(int position, float positionOffset) {
//            int targetPos = (position == mCurrentPosition) ? position + 1 : position;
//            View targetItem = mHolderList[targetPos].itemView;
//            if (mOrientation == HORIZONTAL) {
//                int left = targetItem.getLeft() + integerize(positionOffset * targetItem.getWidth());
//                mShadowView.setTop(0);
//                mShadowView.setRight(mShadowView.getLeft() + mShadowView.getWidth());
//                mShadowView.setBottom(mShadowView.getTop() + mShadowView.getHeight());
//                Log.i("haha", "moveShadow----->" + mShadowView.getLeft() + " " + mShadowView.getTop() + " " + mShadowView.getRight
//                        () + " " + mShadowView.getBottom() + " " + mShadowView.getWidth() + " " + mShadowView.getHeight());
//                requestGlobalLayout();
//                mShadowView.setLeft(left);
//            } else if (mOrientation == VERTICAL) {
//                int top = targetItem.getTop() + integerize(positionOffset * targetItem.getHeight());
//                mShadowView.setTop(top);
//            }


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // when page is scrolled by touch event, make shadow follow the scrolling
            if (mTriggerMode == MODE_TOUCH_SCROLL) {
                moveShadow(position, positionOffset);
            }
        }

        // This method will be invoked when ViewPager.setCurrentItem() or after onPageScrollStateChanged() with
        // a state that equals 2 (SCROLL_STATE_SETTING)

        @Override
        public void onPageSelected(int position) {
            // add mode filter to avoid cycle call of onItemSelected(), if scrolling is triggered by clicking
            // this method has already been invoked.

            if (mTriggerMode == MODE_TOUCH_SCROLL)
                onItemSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_DRAGGING)
                mTriggerMode = MODE_TOUCH_SCROLL;
        }

    }

    private class FocusChangedAnimator extends ValueAnimator implements Animator.AnimatorListener, ValueAnimator
            .AnimatorUpdateListener {
        // create local variables to avoid conflict with the global variables
        public int currentPosition;
        public int targetPosition;

        // for shadowAnimation
        public int startPoint;
        public int targetPoint;
        public int distance;

        public FocusChangedAnimator() {
            // common initialization
            addListener(this);
            addUpdateListener(this);
            setDuration(pagerScrollDuration);
        }

        public void start(int currentPosition, int targetPosition) {
            if (currentPosition == targetPosition) return;
            this.currentPosition = currentPosition;
            this.targetPosition = targetPosition;
            initItemAnimation();
            initShadowAnimation();
            this.start();
        }

        public void initItemAnimation() {
            setIntValues(0, mWeightFocus - mWeightNoFocus);
        }

        public void initShadowAnimation() {
            int nbItemsBeforeTarget = targetPosition;
            if (mOrientation == HORIZONTAL) {
                startPoint = mShadowView.getLeft();
                int widthNoFocus = integerize((double) getWidth() * mWeightNoFocus / mWeightCount);
                targetPoint = nbItemsBeforeTarget * widthNoFocus;
            } else if (mOrientation == VERTICAL) {
                startPoint = mShadowView.getTop();
                int heightNoFocus = integerize((double) getHeight() * mWeightNoFocus / mWeightCount);
                targetPoint = nbItemsBeforeTarget * heightNoFocus;
            }
            distance = targetPoint - startPoint;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setCurrentItem(targetPosition);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // if this animation is being canceled, end it immediately.
            onAnimationEnd(animation);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // animate items' layouts
            int delta = (int) animation.getAnimatedValue();
            onItemAnimation(delta);

            // animate shadow
            float fraction = animation.getAnimatedFraction();
            onShadowAnimation(fraction);

            Log.i("haha", "-------> onAnimate:" + delta + "   " + currentPosition + "    " + targetPosition + " " +
                    "Fraction" + fraction);
            requestGlobalLayout();
        }

        public void onItemAnimation(int delta) {
            mHolderList[currentPosition].weight = mWeightFocus - delta;
            mHolderList[targetPosition].weight = mWeightNoFocus + delta;
        }

        public void onShadowAnimation(float fraction) {
            int delta = integerize(fraction * distance);
            if (mOrientation == HORIZONTAL) {
                mShadowView.setLeft(startPoint + delta);
            } else if (mOrientation == VERTICAL) {
                mShadowView.setTop(startPoint + delta);
            }
        }
    }

    /**
     * New custom scroller with a given scroll duration, and can accept an indicated interpolator.
     */
    private class ViewPagerScroller extends Scroller {
        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, pagerScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, pagerScrollDuration);
        }
    }
}
