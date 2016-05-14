package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {
    final private static String[] xActivityNames = new String[]{
            "Ripple Effect",
            "CustomView",
            "IntentService",
            "SnackerBar",
            "Notification",
            "Navigation Drawer",
            "PagerTabStrip",
            "ActionBar",
            "Shadow and Clipping",
            "CardView"
    };

    final private static Class[] xActivityClasses = new Class[]{
            RippleEffectActivity.class,
            CustomViewActivity.class,
            IntentServiceActivity.class,
            SnackerBarActivity.class,
            NotificationActivity.class,
            NavigationDrawerActivity.class,
            PaperTabStripActivity.class,
            ActionBarActivity.class,
            ShadowAndClippingActivity.class,
            CardViewActivity.class
    };

    private RecyclerView xRecyclerView;
    private RecyclerView.Adapter xAdapter;
    private RecyclerView.LayoutManager xLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this announce to improve performance if the layout size of RecyclerView will not be changed.
        xRecyclerView.setHasFixedSize(true);
        xRecyclerView.setClickable(true);

        xLayoutManager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(xLayoutManager);

        xAdapter = new MyAdapter(xActivityNames);
        xRecyclerView.setAdapter(xAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] xDataSet;

        public MyAdapter(String[] dataSet) {
            xDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            ViewHolder vh = new ViewHolder((Button) view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.button.setText(xDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return xDataSet.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public Button button;

            public ViewHolder(Button view) {
                super(view);
                button = view;
                button.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.i("Clicked", "---> Clicked: " + getAdapterPosition());
                Intent intent = new Intent(MainActivity.this, xActivityClasses[getAdapterPosition()]);
                startActivity(intent);
            }
        }
    }

    private String[] dataGenerator(int n) {
        String[] data = new String[n];
        for (int i = 0; i < n; i++) {
            data[i] = "RecyclerView ListItem " + i;
        }
        return data;
    }
}
