package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

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
            super.removeAllViews();
        initLayout();
        isAdapterNewlySet = false;
    }

    /**
     * To be invoked when first this layout first loads.
     */
    // TODO: 2016/6/15 add OnGlobalLayoutListener to surveille incoming layout changes, and then adjust child-views size
    public void initLayout() {

        // add views in list_holder to the layout
        for (int i = 0; i < itemCount; i++) {
            super.addView(list_holder[i].itemView);
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
        onViewHoldersLayout();
    }

    protected void onViewHoldersLayout() {
        ViewGroup.LayoutParams lp;
        int left = 0, top = 0;
        for (int i = 0; i < itemCount; i++) {
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

                setCurrentItem(position);
            }
        });
    }

    public void setCurrentItem(int position) {
        list_holder[position].weight = mAdapter.getWeightOnFocus(position);
        list_holder[currentPosition].weight = mAdapter.getWeightOnLostFocus(currentPosition);

        // delta = nextState - lastState
        int delta = mAdapter.getWeightOnFocus(position) - mAdapter.getWeightOnLostFocus(position);
        delta += mAdapter.getWeightOnLostFocus(currentPosition) - mAdapter.getWeightOnFocus(currentPosition);
        weightTotal += delta;
        requestViewHoldersLayout();

        // update current position
        currentPosition = position;
    }


    private void animateTransition(int selectedPos) {
        // display custom animations
        if (list_holder[selectedPos].onFocusAnimSet != null)
            list_holder[selectedPos].onFocusAnimSet.start();

        if (list_holder[currentPosition].onLostFocusAnimSet != null)
            list_holder[currentPosition].onLostFocusAnimSet.start();
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
