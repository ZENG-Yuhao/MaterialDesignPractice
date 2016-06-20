package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SideToolBar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 */
public class SideToolBar extends FrameLayout {
    /* layout orientation */
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private int mOrientation = HORIZONTAL;

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

    /* default weight */
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
    public static final int MODE_CLICK = 0;
    public static final int MODE_SCROLL = 1;
    private FocusChangeSynchronizer mSynchronizer = new FocusChangeSynchronizer();

    /* ViewPagerScroller */
    public static final int pagerScrollDuration = 150;

    public SideToolBar(Context context) {
        super(context);
        init();
    }

    public SideToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SideToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {

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
     * @param weightFocus weight for the selected item
     * @param weightNoFocus weight for non-selected items
     */
    public void setWeights(int weightFocus, int weightNoFocus) {
        this.mWeightFocus = weightFocus > 0 ? weightFocus : 1;
        this.mWeightNoFocus = weightNoFocus > 0 ? weightNoFocus : 1;
    }

    /**
     * After doing layout animations, there may be errors of widths, use this method can fix it.
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

    /**
     * To set an adapter for creating views of items, defining default actions, getting information etc.
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
            requestViewHoldersLayout();
        } else {
            /*
                if height and width get 0 before the first load of the layout, register listener to obtain height and
                width when onLayout() finished.
             */
            final ViewTreeObserver observer = this.getViewTreeObserver();
            if (observer != null) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //initTabs(getHeight(), getWidth());
                        requestViewHoldersLayout();

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
     * Refresh layout changes, in some way, it's equivalent to requestLayout(), because onMeasure() method isn't
     * overridden, so an measure for items before requestLayout() is required. And this method is necessary after each
     * layout animation or layout change.
     */
    public void requestViewHoldersLayout() {
        onViewHoldersMeasure();
        requestLayout();
    }

    /**
     * Measurement for each ViewHolder, make sure getWidth() and getHeight() are nonzero before invoking this method.
     * These measures must be done outside onMeasure() method and before requestLayout() or requestViewHolderLayout().
     */
    protected void onViewHoldersMeasure() {
        if (getWidth() == 0 || getHeight() == 0) return;
        int width, height;
        ViewGroup.LayoutParams lp;
        for (int i = 0; i < mItemCount; i++) {
            if (getOrientation() == HORIZONTAL) {
                height = getHeight();
                width = (int) Math.ceil((float) getWidth() * mHolderList[i].weight / mWeightCount);
            } else {
                height = (int) Math.ceil((float) getHeight() * mHolderList[i].weight / mWeightCount);
                width = getWidth();
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
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ViewGroup.LayoutParams lp;
        int left = 0, top = 0;
        for (int i = 0; i < mItemCount; i++) {
            lp = mHolderList[i].itemView.getLayoutParams();
            if (getOrientation() == HORIZONTAL) {
                mHolderList[i].itemView.layout(left, 0, left + lp.width, lp.height);
                left += lp.width;
            } else {
                mHolderList[i].itemView.layout(0, top, lp.width, top + lp.height);
                top += lp.height;
            }
        }
    }

    /**
     * Bind a listener to handle click event for each item.
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        if (listener == null)  return;

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
        requestViewHoldersLayout();
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

        public int mTriggerMode = MODE_SCROLL;

        public WeakReference<ViewPager> pagerWeakReference;
        public ItemChangedAnimator mAnimator;
        public Interpolator mInterpolator;


        public FocusChangeSynchronizer() {
            mInterpolator = new DecelerateInterpolator();
            mAnimator = new ItemChangedAnimator();
            mAnimator.setInterpolator(mInterpolator);
        }

        /**
         * @return true if there is already a pager bound to the synchronizer.
         */
        public boolean isPagerBound() {
            return (pagerWeakReference != null && pagerWeakReference.get() != null);
        }

        public void bindViewPager(ViewPager pager) {
            if (pager != null) {
                // if pager and this ToolBar have different number of children, return.
                if (pager.getChildCount() != mItemCount) return;

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
            mTriggerMode = MODE_CLICK;
            onItemSelected(position);
        }

        public void onItemSelected(int position) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            // Canceling animation must be executed before return
            if (mCurrentPosition == position) return;

            mAnimator.start(mCurrentPosition, position);
            if (isPagerBound())
                pagerWeakReference.get().setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*
                Distribution of positionOffset relative to middle point of screen (MP) is like:
                [0, 0.999) - MP - [0, 0.999)
             */
        }

        /* This method will be invoked when ViewPager.setCurrentItem() or after onPageScrollStateChanged() with
           a state that equals 2 (SCROLL_STATE_SETTING)
         */
        @Override
        public void onPageSelected(int position) {
            if (mTriggerMode == MODE_SCROLL)
                onItemSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_DRAGGING)
                mTriggerMode = MODE_SCROLL;
        }

    }

    private class ItemChangedAnimator extends ValueAnimator implements Animator.AnimatorListener, ValueAnimator
            .AnimatorUpdateListener {
        // create local variables to avoid conflict with the global variables
        public int currentPosition;
        public int targetPosition;

        public ItemChangedAnimator() {
            setIntValues(0, mWeightFocus - mWeightNoFocus);
            addListener(this);
            addUpdateListener(this);
            setDuration(pagerScrollDuration);
        }

        public void start(int currentPosition, int targetPosition) {
            if (currentPosition == targetPosition) return;
            this.currentPosition = currentPosition;
            this.targetPosition = targetPosition;
            this.start();
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
            int delta = (int) animation.getAnimatedValue();
            mHolderList[currentPosition].weight = mWeightFocus - delta;
            mHolderList[targetPosition].weight = mWeightNoFocus + delta;
            Log.i("haha", "-------> onAnimate:" + delta + "   " + currentPosition + "    " + targetPosition);
            requestViewHoldersLayout();
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
