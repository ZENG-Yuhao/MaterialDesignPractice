package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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

    public void init() {

    }

    public void setAdapter(Adapter<? extends ViewHolder> adapter) {
        this.adapter = adapter;

        if (adapter == null) {
            throw new IllegalArgumentException("adapter may not be null");
        }

        itemCount = this.adapter.getItemCount();
        list_holder = new ViewHolder[itemCount];
        for (int i = 0; i < itemCount; i++) {
            list_holder[i] = this.adapter.createViewHolder(this);
            this.adapter.bindViewHolder(list_holder[i], i);
        }

        // adapter has been changed, must refresh the layout
        requestLayout();
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
