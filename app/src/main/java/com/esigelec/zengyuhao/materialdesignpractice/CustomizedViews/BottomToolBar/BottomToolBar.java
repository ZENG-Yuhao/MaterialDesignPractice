package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 * Created by root on 13/06/16.
 */
public class BottomToolBar extends FrameLayout {
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
    /* default weight */
    private int mWeightFocus = 20000;
    private int mWeightNoFocus = 10000;

    /* Synchronizer */
    private FocusChangeSynchronizer mSynchronizer = new FocusChangeSynchronizer();

    /**
     * If adapter has been newly set, must wipe all layout params when onMeasureViewHolder for secure to avoid margin
     * and padding problems.
     */
    private boolean isAdapterNewlySet = true;


    private Paint paint;

    public BottomToolBar(Context context) {
        super(context);
        init();
    }

    public BottomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BottomToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {

        setWillNotDraw(false);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.FILL);
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

    public void setWeights(int weightFocus, int weightNoFocus) {
        this.mWeightFocus = weightFocus > 0 ? weightFocus : 1;
        this.mWeightNoFocus = weightNoFocus > 0 ? weightNoFocus : 1;
    }

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
    public void initLayout() {

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
        onViewHoldersLayout();
    }

    protected void onViewHoldersLayout() {
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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, 100, 100, paint);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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
                if (mCurrentPosition == position) return;

                animateTransition(position);
                // call user customized method
                mOnItemClickListener.onItemClick(v, position);

                //setCurrentItem(position);
            }
        });
    }

    public void setCurrentItem(int position) {
        mHolderList[position].weight = mWeightFocus;
        mHolderList[mCurrentPosition].weight = mWeightNoFocus;

        requestViewHoldersLayout();

        // update current position
        mCurrentPosition = position;
    }


    private void animateTransition(int selectedPos) {
        // display custom animations
        if (mHolderList[selectedPos].onFocusAnimSet != null)
            mHolderList[selectedPos].onFocusAnimSet.start();

        if (mHolderList[mCurrentPosition].onLostFocusAnimSet != null)
            mHolderList[mCurrentPosition].onLostFocusAnimSet.start();
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
        public AnimatorSet onFocusAnimSet;
        public AnimatorSet onLostFocusAnimSet;

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

        protected final VH createViewHolder(ViewGroup parent) {
            VH holder = onCreateViewHolder(parent);
            return holder;
        }

        protected final void bindViewHolder(VH holder, int position) {
            holder.position = position;
            onBindViewHolder(holder, position);
            holder.onFocusAnimSet = onFocusAnimatorSet(holder, position);
            holder.onLostFocusAnimSet = onLostFocusAnimatorSet(holder, position);
        }

        // implementation is optional
        public AnimatorSet onFocusAnimatorSet(VH holder, int position) {
            return null;
        }

        // implementation is optional
        public AnimatorSet onLostFocusAnimatorSet(VH holder, int position) {
            return null;
        }

        public abstract int getItemCount();

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(VH holder, int position);

    }


    public interface OnItemClickListener {
        /**
         * You should never invoke setCurrentItem() of the bound ViewPager in this callback method, because
         * FocusChangeSynchronizer will handle it for you. Otherwise, you will receive a trembling of view.
         *
         * @param view     the view was clicked.
         * @param position the position of the clicked view in this layout.
         */
        void onItemClick(View view, int position);
    }

    private class FocusChangeSynchronizer implements ViewPager.OnPageChangeListener {
        public WeakReference<ViewPager> pagerWkRef;
        /* zone state */
        public static final int ZONE_LEFT = 0; // middle point of screen located at left side of current page
        public static final int ZONE_RIGHT = 1; // middle point of screen located at right side of current page

        /* focus change event */
        public static final int ITEM_CLICKING = 2;
        public static final int PAGER_SCROLLING = 3;
        public int mTriggerEvent = PAGER_SCROLLING;

        public void bindViewPager(ViewPager pager) {
            if (pager != null) {
                // if there is already one pager bound, unbind it.
                if (pagerWkRef != null && pagerWkRef.get() != null)
                    unbindViewPager();
                pagerWkRef = new WeakReference<>(pager);
                // make sure they are at the same position
                pager.setCurrentItem(mCurrentPosition);
                pager.addOnPageChangeListener(this);
            }
        }

        public void unbindViewPager() {
            if (pagerWkRef == null) return;
            ViewPager pager = pagerWkRef.get();
            if (pager != null) {
                pager.removeOnPageChangeListener(this);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*
                Distribution of positionOffset relative to middle point of screen (MP) is like:
                [0, 0.999) - MP - [0, 0.999)
             */
            int zoneFlag = (position == mCurrentPosition - 1) ? ZONE_LEFT : ZONE_RIGHT;
            // this value is relative to the middle point, the smaller the value, the closer the distance is.
            float distanceOffset = (zoneFlag == ZONE_LEFT) ? (1 - positionOffset) : positionOffset;

            /* calculate layout changes */
            int targetPos;
            if (zoneFlag == ZONE_LEFT)
                targetPos = mCurrentPosition - 1;
            else
                targetPos = mCurrentPosition + 1;

            if (targetPos >= 0 && targetPos < mItemCount) {
                int delta = Math.round((mWeightFocus - mWeightNoFocus) * distanceOffset);
                mHolderList[mCurrentPosition].weight = mWeightFocus - delta;
                mHolderList[targetPos].weight = mWeightNoFocus + delta;
                requestViewHoldersLayout();
            }
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentItem(position);
            //mCurrentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
}
