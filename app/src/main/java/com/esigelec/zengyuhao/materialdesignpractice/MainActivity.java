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

import com.esigelec.zengyuhao.materialdesignpractice.CustomViews.CircleImageView;
import com.esigelec.zengyuhao.materialdesignpractice.EXEM.NavigationEXEMActivity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.Login2Activity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.Login3Activity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.LoginActivity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.LoginExtendedActivity;

public class MainActivity extends Activity {
    final private static String[] xActivityNames = new String[]{
            "WaveScanner UI",
            "MagnifierImageView",
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
            "CardView"
    };

    final private static Class[] xActivityClasses = new Class[]{
            NavigationEXEMActivity.class,
            MagnifierImageViewActivity.class,
            CircleImageViewActivity.class,
            ViewGroupOverlayActivity.class,
            RecyclerViewActivity.class,
            SceneAndTransitionActivity.class,
            SwipeLayoutActivity.class,
            DialogActivity.class,
            HardwareAccelerationActivity.class,
            ToolBarActivity.class,
            LoginExtendedActivity.class,
            Login3Activity.class,
            Login2Activity.class,
            LoginActivity.class,
            ObserverAndObservableActivity.class,
            SharedElementsTransitionActivity.class,
            CircularRevealActivity.class,
            RippleEffectActivity.class,
            CustomViewActivity.class,
            IntentServiceActivity.class,
            NotificationActivity.class,
            NavigationDrawerActivity.class,
            PaperTabStripActivity.class,
            ActionBarActivity.class,
            ShadowAndClippingActivity.class,
            CardViewActivity.class
    };

    @Override
    protected void onResume() {
         /*
         If you hide the system bars in your activity's onCreate() method and the user presses Home, the system bars
         will reappear. When the user reopens the activity, onCreate() won't get called, so the system bars will
         remain visible. If you want system UI changes to persist as the user navigates in and out of your activity,
         set UI flags in onResume() or onWindowFocusChanged().
         */
//        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View
//                .SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        super.onResume();

    }

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
