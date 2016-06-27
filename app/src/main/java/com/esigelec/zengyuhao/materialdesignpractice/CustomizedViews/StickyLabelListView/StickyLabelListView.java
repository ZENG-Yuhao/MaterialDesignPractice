package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.StickyLabelListView;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.esigelec.zengyuhao.materialdesignpractice.BuildConfig;
import com.esigelec.zengyuhao.materialdesignpractice.EXEM.BottomToolBarActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZENG Yuhao on 27/06/16.
 * Contact: enzo.zyh@gmail.com
 */
public class StickyLabelListView extends FrameLayout {
    private static final String TAG = "StickyLabelListView";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int mLabelViewType = 1;
    private ShadowLayout mShadowLabel;
    private View buffView;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addOnScrollListener(new ScrollListener());
        addView(mRecyclerView);

        mShadowLabel = new ShadowLayout(context);
//        lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
//        mShadowLabel.setLayoutParams(lp);
        addView(mShadowLabel);
    }

    public void setAdapter(RecyclerView.Adapter adapter, int labelViewType) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        setLabelViewType(labelViewType);
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(this, labelViewType);
        mAdapter.onBindViewHolder(holder, 0);
        View shadowView = holder.itemView;
        mShadowLabel.setContentView(shadowView);
    }

    public void setLabelViewType(int type) {
        mLabelViewType = type;
    }


    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            View firstVisibleView = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findViewByPosition(0);
//            if (firstVisibleView == null) return;
//            RecyclerView.ViewHolder firstVisibleViewHolder = mRecyclerView.getChildViewHolder(firstVisibleView);
//            if (firstVisibleViewHolder.getItemViewType() == mLabelViewType) {
//                if (DEBUG) Log.i(TAG, "onScrolled--> Label detected.");
//            }


            int pos = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (DEBUG) Log.i(TAG, "onScrolled--> firstVisibleItemPosition " + pos);
            if (mAdapter.getItemViewType(pos) == mLabelViewType)
                if (DEBUG) Log.i(TAG, "onScrolled--> Label detected.");

            int pos_next = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();

            View currView = mRecyclerView.getLayoutManager().findViewByPosition(pos);
            int currViewType = mRecyclerView.getLayoutManager().getItemViewType(currView);

            View nextView = mRecyclerView.getLayoutManager().findViewByPosition(pos_next);
            if (DEBUG) Log.i(TAG, "onScrolled--> View:" + mRecyclerView.getLayoutManager()
                    .getItemViewType(nextView));

            int nextViewType = mRecyclerView.getLayoutManager()
                    .getItemViewType(nextView);

            if (nextViewType == mLabelViewType) {
                mShadowLabel.setHeight(nextView.getTop());
                if (nextView.getTop() - dy < 0) {
                    RecyclerView.ViewHolder holder = mAdapter.onCreateViewHolder(mShadowLabel, mLabelViewType);
                    mAdapter.onBindViewHolder(holder, pos_next);
                    View shadowView = holder.itemView;
                    mShadowLabel.setContentView(shadowView);
                }
            } else if (currViewType == mLabelViewType) {
            }
        }
    }

    private class ShadowLayout extends FrameLayout {
        public View contentView;
        public int mInitHeight;

        public ShadowLayout(Context context) {
            super(context);
        }

        public void setContentView(View view) {
            if (view == null) {
                if (DEBUG) Log.i(TAG, "setContentView-->Skipped: view cannot be null.");
                return;
            }

            contentView = view;
            addView(contentView);

//            ViewTreeObserver observer = getViewTreeObserver();
//            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    mInitHeight = contentView.getHeight();
//
//                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//            });
        }

        public void setHeight(int height) {
            if (height < 0) {
                if (DEBUG) Log.i(TAG, "setHeight-->Skipped: height cannot be negative.");
                return;
            }

            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            lp.height = height;
            requestLayout();
        }
    }
}
