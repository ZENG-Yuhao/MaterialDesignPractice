package com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar;

import android.accounts.AbstractAccountAuthenticator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by root on 13/06/16.
 */
public class BottomToolBar extends LinearLayout {
    private BaseAdapter mAdapter;

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

    /**
     * A holder of a view at specific position of the BottomToolBar, this philosophy decouple dependencies between
     * classes and it gives more possibilities to define custom actions.
     */
    public abstract class ViewHolder {
        protected View itemView;
        protected int adapterPosition = 0;

        public ViewHolder(View view) {
            if (view == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            itemView = view;
        }

        protected final void setAdapterPosition(int position) {
            adapterPosition = position;
        }

        public final int getAdapterPosition() {
            return adapterPosition;
        }

    }

    public abstract class Adapter<VH extends ViewHolder>{

        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        public abstract void onBindViewHolder(VH holder, int position);

        public abstract int getItemCount();


    }


}
