package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;

/**
 * A bottom tool bar that can receive a list of tabs and their appropriate actions.
 * Created by root on 13/06/16.
 */
public class BottomToolBarRefused extends LinearLayout {
    private Adapter adapter;
    private ViewHolder[] list_holder;
    private int itemCount;
    private OnItemClickListener onItemClickListener;
    private int currentPosition = 0;
    Animator anim, anim1;

    public BottomToolBarRefused(Context context) {
        super(context);
    }

    public BottomToolBarRefused(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomToolBarRefused(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BottomToolBarRefused(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            final ViewTreeObserver observer = this.getViewTreeObserver();
            if (observer != null) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        initTabs(getHeight(), getWidth());

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
            Log.i("Haha", "initTabs--->>" + "i " + i + "; itemCount" + itemCount + ";" + view.getParent());
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
                tempAnim(v);
                //if (currentPosition == position) return;

                animateTransition(position);
                // call user customized method
                onItemClickListener.onItemClick(v, position);

                // update current position
                currentPosition = position;
            }
        });
    }

    private void tempAnim(View v){
        // calculate params
        double half_width = v.getWidth() / 2;
        double half_height = v.getHeight() / 2;
        int finalRadius = (int) Math.hypot(half_width, half_height);

        // set exit animator
        anim = ViewAnimationUtils.createCircularReveal(v, (int) half_width, (int) half_height,
                finalRadius, 0);
        anim.setDuration(200);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                anim1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        // set enter animator
        anim1 = ViewAnimationUtils.createCircularReveal(v, (int) half_width, (int) half_height, 0,
                finalRadius);
        anim1.setDuration(200);
        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // animation set finished, reset the clickable state
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        // start exit animator and then enter animator
        anim.start();
    }

    private void animateTransition(int selectedPos) {
        // display animation
        if (list_holder[selectedPos].onFocusAnimSet != null)
            list_holder[selectedPos].onFocusAnimSet.start();

        if (list_holder[currentPosition].onLostFocusAnimSet != null)
            list_holder[currentPosition].onLostFocusAnimSet.start();
    }

    public static abstract class ViewHolder {
        public View itemView;
        public int position = 0;
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
        void onItemClick(View view, int position);
    }

}
