package com.esigelec.zengyuhao.materialdesignpractice.CustomView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.esigelec.zengyuhao.materialdesignpractice.BuildConfig;

/**
 * <p>
 * Created by ZENG Yuhao on 27/06/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class StickyLabelListView extends FrameLayout {
    private static final String TAG = "StickyLabelListView";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    /* shadow state */
    private static final int SHADOW_STAY = 0;
    private static final int SHADOW_FOLLOW = 1;
    private static final int SHADOW_HIDE = 2;
    private static final int SCROLLING_UP = 0;
    private static final int SCROLLING_DOWN = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int mLabelViewType = 1;
    private ShadowLayout mShadowLabel;
    private LinearLayoutManager mLayoutManager;

    public StickyLabelListView(Context context) {
        super(context);
        init(context);
    }

    public StickyLabelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickyLabelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public StickyLabelListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        mRecyclerView = new RecyclerView(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(lp);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new ScrollListener());
        addView(mRecyclerView);

        mShadowLabel = new ShadowLayout(context);
        addView(mShadowLabel);
    }

    public void setAdapter(RecyclerView.Adapter adapter, int labelViewType) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        setLabelViewType(labelViewType);
        View shadowView = createShadowFromPosition(0, labelViewType);
        mShadowLabel.setContentView(shadowView, 0);
    }

    public void setLabelViewType(int type) {
        mLabelViewType = type;
    }


    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mShadowLabel.update(dy);
        }
    }

    private View findViewByPosition(int position) {
        return mLayoutManager.findViewByPosition(position);
    }

    private int getItemViewType(View view) {
        return mLayoutManager.getItemViewType(view);
    }

    private int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    /* last invisible item*/
    private int getLastInvisibleItemPosition() {
        return getFirstVisibleItemPosition() - 1;
    }

    private int getLastInvisibleItemViewType() {
        return getItemViewType(getLastInvisibleItemPosition());
    }


    /* first visible item */
    private int getFirstVisibleItemPosition() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    private View getFirstVisibleItem() {
        return findViewByPosition(getFirstVisibleItemPosition());
    }

    private int getFirstVisibleItemViewType() {
        return getItemViewType(getFirstVisibleItemPosition());
    }

    /* first completely visible item */

    /**
     * Main utility of this method is to calculate top margin of {@link ShadowLayout}, according to the item next to
     * the first visible item's position.
     * But there may be a possibility that Completely Visible Item does not exist (when item height > screen height)
     * when {@link LinearLayoutManager#findFirstCompletelyVisibleItemPosition()} return -1, that means this situation
     * happens, at this moment, there is no completely visible item on screen (there are only two half visible items)
     * , so we take the last visible item's position instead.
     */
    private int getFirstCompletelyVisibleItemPosition() {
        int position = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (position == -1) {
            position = mLayoutManager.findLastVisibleItemPosition();
        }
        return position;
    }

    private View getFirstCompletelyVisibleItem() {
        return findViewByPosition(getFirstCompletelyVisibleItemPosition());
    }

    private int getFirstCompletelyVisibleItemViewType() {
        return getItemViewType(getFirstCompletelyVisibleItemPosition());
    }

    /* closest label */
    private int getClosestPreviousLabelPosition(int currPosition) {
        int posPrevious = currPosition;
        while (getItemViewType(posPrevious) != mLabelViewType && posPrevious > 0)
            posPrevious--;
        return posPrevious;
    }

    private int getClosestFollowingLabelPosition(int currPosition) {
        int posFollowing = currPosition;
        while (getItemViewType(posFollowing) != mLabelViewType && posFollowing < mAdapter.getItemCount())
            posFollowing++;
        return posFollowing;
    }

    /* shadow */
    private View createShadowFromPosition(int position, int type) {
        RecyclerView.ViewHolder holder = mAdapter.onCreateViewHolder(mShadowLabel, type);
        mAdapter.onBindViewHolder(holder, position);
        return holder.itemView;
    }

    private class ShadowLayout extends FrameLayout {
        public View shadowView;
        public int shadowOf;
        public int mInitHeight;
        public int currState = SHADOW_STAY;

        public ShadowLayout(Context context) {
            super(context);
        }

        public void init() {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT);
            setLayoutParams(lp);
        }

        public void setContentView(View view, int position) {
            if (view == null) {
                if (DEBUG) Log.i(TAG, "setContentView-->Skipped: view cannot be null.");
                return;
            }
            removeAllViews();
            shadowView = view;
            shadowOf = position;
            addViewInLayout(shadowView, -1, shadowView.getLayoutParams(), true);

            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mInitHeight = shadowView.getHeight();

                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

        public void update(int dy) {
            updateState((dy > 0) ? SCROLLING_UP : SCROLLING_DOWN);
            setMarginTop(-(mInitHeight - getFirstCompletelyVisibleItem().getTop()));
        }

        public boolean updateState(int scrollState) {
            boolean isStateChanged;
            int newState = SHADOW_STAY;
            if (scrollState == SCROLLING_UP) {
                if (getFirstVisibleItemViewType() == mLabelViewType) {
                    int pos = getFirstVisibleItemPosition();
                    // the first time the label come to current position update shadowView
                    if (shadowOf != pos) {
                        View newShadowView = createShadowFromPosition(pos, mLabelViewType);
                        setContentView(newShadowView, pos);
                    }
                    newState = SHADOW_STAY;
                }

                // next label (the item below the current) is coming, overwrite newState
                if (getFirstCompletelyVisibleItemViewType() == mLabelViewType) {
                    if (getFirstCompletelyVisibleItem().getTop() <= mInitHeight)
                        newState = SHADOW_FOLLOW;
                }
            } else if (scrollState == SCROLLING_DOWN) {
                if (getFirstCompletelyVisibleItemViewType() == mLabelViewType) {
                    int pos = getClosestPreviousLabelPosition(getFirstVisibleItemPosition());
                    // the first time the label come to current position update shadowView
                    if (shadowOf != pos) {
                        View newShadowView = createShadowFromPosition(pos, mLabelViewType);
                        setContentView(newShadowView, pos);
                    }
                    newState = SHADOW_FOLLOW;

                    // next label (the item above the current) is coming, overwrite newState
                    if (getFirstCompletelyVisibleItem().getTop() > mInitHeight)
                        newState = SHADOW_STAY;
                }
            }
            isStateChanged = (newState == currState);
            currState = newState;
            return isStateChanged;
        }

        public int getMarginTop() {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            return lp.topMargin;
        }

        public void setMarginTop(int topMargin) {
            switch (currState) {
                case SHADOW_STAY:
                    if (getMarginTop() == 0) return;
                    else topMargin = 0;
                    break;
                case SHADOW_FOLLOW:
                    if (topMargin > 0) topMargin = 0;
                    else if (topMargin < -mInitHeight) topMargin = -mInitHeight;
                    break;
                case SHADOW_HIDE:
                    if (getMarginTop() == -mInitHeight) return;
                    else topMargin = -mInitHeight;
                    break;
            }
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            lp.topMargin = topMargin;
            requestLayout();
        }
    }
}
