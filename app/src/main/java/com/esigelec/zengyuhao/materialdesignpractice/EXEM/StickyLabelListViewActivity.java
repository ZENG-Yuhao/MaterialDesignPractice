package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SideToolBar.SideToolbar;
import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.StickyLabelListView.StickyLabelListView;
import com.esigelec.zengyuhao.materialdesignpractice.R;

import org.w3c.dom.Text;

public class StickyLabelListViewActivity extends Activity {
    private StickyLabelListView mStickyLabelListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_label_list_view);

        mStickyLabelListView = (StickyLabelListView) findViewById(R.id.sticky_list_view);
        mStickyLabelListView.setAdapter(new MyAdapter(), MyLabelHolder.VIEW_TYPE);

    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            View view = null;
            if (viewType == MyItemHolder.VIEW_TYPE) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layer_bottom_swipelayout,
                        parent, false);
                holder = new MyItemHolder(view);
            } else if (viewType == MyLabelHolder.VIEW_TYPE) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_text_view, parent, false);
                holder = new MyLabelHolder(view);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyLabelHolder) {
                ((MyLabelHolder) holder).mBtn.setText("LABEL " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        @Override
        public int getItemViewType(int position) {
            return (position % 4 == 0) ? MyLabelHolder.VIEW_TYPE : MyItemHolder.VIEW_TYPE;
        }
    }

    private class MyItemHolder extends RecyclerView.ViewHolder {
        public static final int VIEW_TYPE = 0;

        public MyItemHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyLabelHolder extends RecyclerView.ViewHolder {
        public static final int VIEW_TYPE = 1;
        public Button mBtn;

        public MyLabelHolder(View itemView) {
            super(itemView);
            mBtn = (Button) itemView;
        }
    }

}
