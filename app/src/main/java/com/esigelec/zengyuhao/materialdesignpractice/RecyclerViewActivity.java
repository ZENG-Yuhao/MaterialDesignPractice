package com.esigelec.zengyuhao.materialdesignpractice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.Core.Image.EfficientBitmap;
import com.esigelec.zengyuhao.materialdesignpractice.EXEM.BottomToolBarActivity;

import java.util.Collections;
import java.util.Random;


/**
 * REFERENCE: http://www.jianshu.com/p/12ec590f6c76
 */
public class RecyclerViewActivity extends AppCompatActivity {
    public static int[] IMG_SRC = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable
            .img4, R.drawable.img5};

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private Toolbar mToolbar;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new MyAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_activity, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ImageView img = (ImageView) holder.itemView.findViewById(R.id.img);
            TextView txt = (TextView) holder.itemView.findViewById(R.id.txt);
            Random rd = new Random();
            Bitmap mPlaceHolderBitmap = EfficientBitmap.decodeBitmap(getResources(), R.drawable.ic_action_replay,
                    75, 75);
            EfficientBitmap.loadBitmap(mContext.getResources(), img, mPlaceHolderBitmap, IMG_SRC[rd.nextInt(5)
                    ], 75, 75, EfficientBitmap.DecoderAsyncTask.MODE_MEMORY_CACHE);

            txt.setText("IMG-" + position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.notifyItemMoved(holder.getAdapterPosition(), holder.getAdapterPosition() + 1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
        }

    }

    class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0, swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int from = viewHolder.getAdapterPosition();
            int to = target.getAdapterPosition();
            //Collections.swap(meizis,from,to);//exchange element in datalist
            mAdapter.notifyItemMoved(from, to);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
