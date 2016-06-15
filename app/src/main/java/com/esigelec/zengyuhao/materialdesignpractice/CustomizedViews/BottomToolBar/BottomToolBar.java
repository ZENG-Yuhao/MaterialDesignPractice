package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 * Created by root on 13/06/16.
 */
public class BottomToolBar extends FrameLayout {
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private final int mOrientation = HORIZONTAL;

    // Holder and adapter
    private Adapter mAdapter;
    private ViewHolder[] list_holder;
    // number of items in list_holder
    private int itemCount;
    private int weightTotal;
    private OnItemClickListener onItemClickListener;
    private int currentPosition = 0;
    /**
     * If adapter has been newly set, must wipe all layout params when onMeasureViewHolder for secure to avoid margin
     * and padding problems.
     */
    private boolean isAdapterNewlySet = true;

    public BottomToolBar(Context context) {
        super(context);
    }

    public BottomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BottomToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        //init list_holder through adapter
        itemCount = mAdapter.getItemCount();
        list_holder = new ViewHolder[itemCount];
        weightTotal = 0;
        for (int i = 0; i < itemCount; i++) {
            list_holder[i] = mAdapter.createViewHolder(this);

            // set weight
            int weight = (i == currentPosition) ? mAdapter.getWeightOnFocus(i) : mAdapter.getWeightOnLostFocus(i);
            list_holder[i].weight = (weight > 0) ? weight : 1;
            weightTotal += list_holder[i].weight;

            // bind
            mAdapter.bindViewHolder(list_holder[i], i);
        }

        // remove all children views when adapter has been changed
        if (getChildCount() > 0)
            removeAllViews();
        initLayout();
        isAdapterNewlySet = false;
    }

    /**
     * To be invoked when first this layout first loads.
     */
    // TODO: 2016/6/15 add OnGlobalLayoutListener to surveille incoming layout changes, and then adjust child-views size
    public void initLayout() {

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

        // after measurement, add views in list_holder to the layout
        for (int i = 0; i < itemCount; i++) {
            this.addView(list_holder[i].itemView);
        }

        requestLayout();
    }

    private void initTabs(int containerHeight, int containerWidth) {
        int height, width;
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final float density = metrics.density;
        final float w = density * 2;
        if (getOrientation() == HORIZONTAL) {
            height = containerHeight;
            //width = (int)Math.ceil((containerWidth - w * (itemCount - 1)) / itemCount);
            width = 0;
        } else {
            //height = (int)Math.ceil((containerHeight - w * (itemCount - 1)) / itemCount);
            height = 0;
            width = containerWidth;
        }

        for (int i = 0; i < itemCount; i++) {
            View view = list_holder[i].itemView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i == currentPosition)
                params.weight = 1650;

            else
                params.weight = 1000;
            params.gravity = Gravity.CENTER;
            view.setLayoutParams(params);
            Log.i("Haha", "initTabs--->>" + "i " + i + "; itemCount" + itemCount + ";" + view.getParent());
            this.addView(view);
        }
    }

    public void requestViewHoldersLayout() {
        onViewHoldersMeasure();
        requestLayout();
    }

    /**
     * Measurement for each ViewHolder, make sure getWidth() and getHeight() are nonzero before invoking this method.
     */
    public void onViewHoldersMeasure() {
        if (getWidth() == 0 || getHeight() == 0) return;
        int width, height;
        ViewGroup.LayoutParams lp;
        for (int i = 0; i < itemCount; i++) {
            if (getOrientation() == HORIZONTAL) {
                height = getHeight();
                width = getWidth() * list_holder[i].weight / weightTotal;
            } else {
                height = getHeight() * list_holder[i].weight / weightTotal;
                width = getWidth();
            }

            if (isAdapterNewlySet) {
                lp = new ViewGroup.LayoutParams(width, height);
                list_holder[i].itemView.setLayoutParams(lp);
            } else {
                lp = list_holder[i].itemView.getLayoutParams();
                lp.width = width;
                lp.height = height;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        onViewHoldersLayout();
    }

    public void onViewHoldersLayout() {
        ViewGroup.LayoutParams lp;
        View view;
        int left = 0, top = 0;
        for (int i = 0; i < itemCount; i++) {
            view = list_holder[i].itemView;
            lp = list_holder[i].itemView.getLayoutParams();
            if (getOrientation() == HORIZONTAL) {
                list_holder[i].itemView.layout(left, 0, left + lp.width, lp.height);
                left += lp.width;
            } else {
                list_holder[i].itemView.layout(0, top, lp.width, top + lp.height);
                top += lp.height;
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;

        // register listener for each holder
        for (int i = 0; i < itemCount; i++) {
            registerOnItemClickListener(list_holder[i], i);
        }
    }

    private void registerOnItemClickListener(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == position) return;

                animateTransition(position);
                // call user customized method
                onItemClickListener.onItemClick(v, position);

                // update current position
                currentPosition = position;
            }
        });
    }

    private void animateTransition(int selectedPos) {
        // display custom animations
        if (list_holder[selectedPos].onFocusAnimSet != null)
            list_holder[selectedPos].onFocusAnimSet.start();

        if (list_holder[currentPosition].onLostFocusAnimSet != null)
            list_holder[currentPosition].onLostFocusAnimSet.start();

//        LinearLayout.LayoutParams param_curr = (LinearLayout.LayoutParams) list_holder[currentPosition].itemView
//                .getLayoutParams();
//        param_curr.weight = 1000;
//
//        LinearLayout.LayoutParams params_select = (LinearLayout.LayoutParams) list_holder[selectedPos].itemView
//                .getLayoutParams();
//        params_select.weight = 1650;
//        requestLayout();

        list_holder[selectedPos].weight = mAdapter.getWeightOnFocus(selectedPos);
        list_holder[currentPosition].weight = mAdapter.getWeightOnLostFocus(currentPosition);

        // delta = nextState - lastState
        int delta = mAdapter.getWeightOnFocus(selectedPos) - mAdapter.getWeightOnLostFocus(selectedPos);
        delta += mAdapter.getWeightOnLostFocus(currentPosition) - mAdapter.getWeightOnFocus(currentPosition);
        weightTotal += delta;
        requestViewHoldersLayout();
    }


    private void animation1(int selectedPos) {
        // these animations below force a declaration in root view as "clipChildren = false" to enable his children view
        // to stretch out of their parent.
        Animator animCurr = ObjectAnimator.ofInt(list_holder[currentPosition], "bottomMargin",
                list_holder[currentPosition].getBottomMargin(), 0);
        animCurr.setDuration(100);

        Animator animSelect = ObjectAnimator.ofInt(list_holder[selectedPos], "bottomMargin", list_holder[selectedPos]
                .getBottomMargin(), 40);
        animSelect.setDuration(100);

        animCurr.start();
        animSelect.start();
    }

    public int getOrientation() {
        return mOrientation;
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

        /**
         * Wrapper method for that ObjectAnimator can animate itemView's margin through ViewHolder.
         *
         * @param margin bottomMarin to be set to itemView
         */
        public void setBottomMargin(int margin) {
            if (margin < 0)
                margin = 0;

            MarginLayoutParams params = (MarginLayoutParams) itemView.getLayoutParams();
            params.bottomMargin = margin;
            itemView.requestLayout();
        }

        /**
         * Wrapper method for that ObjectAnimator can animate itemView's margin through ViewHolder.
         *
         * @return bottomMargin of itemView
         */
        public int getBottomMargin() {
            MarginLayoutParams params = (MarginLayoutParams) itemView.getLayoutParams();
            return params.bottomMargin;
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

        public abstract int getWeightOnFocus(int position);

        public abstract int getWeightOnLostFocus(int position);

        public abstract int getItemCount();

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(VH holder, int position);

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
