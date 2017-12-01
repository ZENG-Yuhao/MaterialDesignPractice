package com.esigelec.zengyuhao.materialdesignpractice.CoordinatorLayout;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class CoordinatorLayoutActivity extends AppCompatActivity {
    final private static String[] mActivityNames = new String[]{
            "CoordinatorLayout",
            "LazyFragmentActivity",
            "Picker Dialog",
            "MPAndroidChart",
            "AutoCompleteTextView",
            "Soft Keyboard",
            "Camera & Photo",
            "WaveScanner UI",
            "MagnifierImageView",
            "New GPSLocator",
            "GPSLocator",
            "Wifi Connection",
            "CircleImageView",
            "ViewGroupOverlay",
            "RecyclerView",
            "Scene and Transition",
            "SwipeLayout",
            "Dialog",
            "Hardware Acceleration",
            "ToolBar",
            "Extendable MVP example",
            "MVP example with Loader2",
            "MVP example with Loader",
            "MVP example",
            "Observer & Observable",
            "Shared Elements Transition",
            "CircularReveal Effect",
            "Ripple Effect",
            "CustomView",
            "IntentService",
            "Notification",
            "Navigation Drawer",
            "PagerTabStrip",
            "ActionBar",
            "Shadow and Clipping",
            "CardView",
            "Test Only",
            "ShapeTest"
    };

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setAdapter(new MyAdapter(mActivityNames));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("CoordinatorLayout");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_attach:
                Toast.makeText(this, "Item attach selected.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Item settings selected.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] dataSet;

        public MyAdapter(String[] dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder((Button) view);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            holder.button.setText(dataSet[position]);
        }

        @Override
        public int getItemCount() {
            return dataSet.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public Button button;

            public ViewHolder(Button view) {
                super(view);
                button = view;
            }
        }
    }
}
