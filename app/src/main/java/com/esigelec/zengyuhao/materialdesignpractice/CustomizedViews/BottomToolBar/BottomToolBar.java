package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 * Created by root on 13/06/16.
 */
public class BottomToolBar extends LinearLayout {
    private Adapter adapter;
    private ViewHolder[] list_holder;
    private int itemCount;
    private OnItemClickListener onItemClickListener;
    private int currentPosition = 0;

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
        this.adapter = adapter;

        if (adapter == null) {
            throw new IllegalArgumentException("adapter may not be null");
        }
        if (adapter.getItemCount() == 0) {
            throw new IllegalArgumentException("item count may not be 0");
        }

        itemCount = this.adapter.getItemCount();
        list_holder = new ViewHolder[itemCount];
        for (int i = 0; i < itemCount; i++) {
            list_holder[i] = this.adapter.createViewHolder(this);
            this.adapter.bindViewHolder(list_holder[i], i);
        }

        initLayout();
    }

    public void initLayout() {
        if (getChildCount() > 0)
            removeAllViews();

        int height = getHeight();
        int width = getWidth();

        if (height != 0 && width != 0) {
            initTabs(height, width);
        } else {
            /*
                if height and width get 0 before the first load of the layout, register listener to obtain height and
                 width when onLayout() finished.
             */
            ViewTreeObserver observer = this.getViewTreeObserver();
            if (observer != null) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        initTabs(getHeight(), getWidth());
                    }
                });
            }
        }
    }

    private void initTabs(int containerHeight, int containerWidth) {
        int height, width;
        if (getOrientation() == HORIZONTAL) {
            height = containerHeight;
            width = containerWidth / itemCount;
        } else {
            height = containerHeight / itemCount;
            width = containerWidth;
        }

        for (int i = 0; i < itemCount; i++) {
            View view = list_holder[i].itemView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.gravity = Gravity.CENTER;
            view.setLayoutParams(params);
            this.addView(view);
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
        // display animation
        if (list_holder[selectedPos].onFocusAnimSet != null)
            list_holder[selectedPos].onFocusAnimSet.start();

        if (list_holder[currentPosition].onLostFocusAnimSet != null)
            list_holder[currentPosition].onLostFocusAnimSet.start();
    }

    public abstract class ViewHolder {
        public View itemView;
        protected int position = 0;
        protected AnimatorSet onFocusAnimSet;
        protected AnimatorSet onLostFocusAnimSet;

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


    public abstract class Adapter<VH extends ViewHolder> {

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
        void onItemClick(View view, int position);
    }

}
