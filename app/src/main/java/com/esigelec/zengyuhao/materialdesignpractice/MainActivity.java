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

import com.esigelec.zengyuhao.materialdesignpractice.ActionBar.ActionBarActivity;
import com.esigelec.zengyuhao.materialdesignpractice.CameraPhoto.CameraActivity;
import com.esigelec.zengyuhao.materialdesignpractice.CircleImageView.CircleImageViewActivity;
import com.esigelec.zengyuhao.materialdesignpractice.CircleProgressBar.CircleProgressBarActivity;
import com.esigelec.zengyuhao.materialdesignpractice.CircularReveal.CircularRevealActivity;
import com.esigelec.zengyuhao.materialdesignpractice.CoordinatorLayout.CoordinatorLayoutActivity;
import com.esigelec.zengyuhao.materialdesignpractice.EfficientBitmap.PaperTabStripActivity;
import com.esigelec.zengyuhao.materialdesignpractice.EfficientBitmap.RecyclerViewActivity;
import com.esigelec.zengyuhao.materialdesignpractice.GPSLocator.GPSLocatorActivity;
import com.esigelec.zengyuhao.materialdesignpractice.GPSLocator.MagnifierImageViewActivity;
import com.esigelec.zengyuhao.materialdesignpractice.GPSLocator.NewGPSLocatorActivity;
import com.esigelec.zengyuhao.materialdesignpractice.HardwareAcceleration.HardwareAccelerationActivity;
import com.esigelec.zengyuhao.materialdesignpractice.IntentService.IntentServiceActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Keyboard.SoftKeyboardHeightActivity;
import com.esigelec.zengyuhao.materialdesignpractice.LazyLoading.LazyFragmentActivity;
import com.esigelec.zengyuhao.materialdesignpractice.MPAndroidChart.MPAndroidChartActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Notification.NotificationActivity;
import com.esigelec.zengyuhao.materialdesignpractice.ObserverObservable.ObserverAndObservableActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Others.AutoCompleteTextActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Others.CardViewActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Others.NavigationDrawerActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Others.ShadowAndClippingActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Others.ToolBarActivity;
import com.esigelec.zengyuhao.materialdesignpractice.RippleEffect.RippleEffectActivity;
import com.esigelec.zengyuhao.materialdesignpractice.RoundShapeInjector.ShapeTestActivity;
import com.esigelec.zengyuhao.materialdesignpractice.SceneAndTransition.SceneAndTransitionActivity;
import com.esigelec.zengyuhao.materialdesignpractice.SharedElementTransition.SharedElementsTransitionActivity;
import com.esigelec.zengyuhao.materialdesignpractice.SwipeLayout.SwipeLayoutActivity;
import com.esigelec.zengyuhao.materialdesignpractice.ViewGroupOverlay.ViewGroupOverlayActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Wifi.WifiConnectionActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Dialog.DialogActivity;
import com.esigelec.zengyuhao.materialdesignpractice.Dialog.PickerDialogActivity;
import com.esigelec.zengyuhao.materialdesignpractice.EXEM.NavigationEXEMActivity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.Login2Activity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.Login3Activity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.LoginActivity;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.LoginExtendedActivity;

public class MainActivity extends Activity {
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

    final private static Class[] mActivityClasses = new Class[]{
            CoordinatorLayoutActivity.class,
            LazyFragmentActivity.class,
            PickerDialogActivity.class,
            MPAndroidChartActivity.class,
            AutoCompleteTextActivity.class,
            SoftKeyboardHeightActivity.class,
            CameraActivity.class,
            NavigationEXEMActivity.class,
            MagnifierImageViewActivity.class,
            NewGPSLocatorActivity.class,
            GPSLocatorActivity.class,
            WifiConnectionActivity.class,
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
            CircleProgressBarActivity.class,
            IntentServiceActivity.class,
            NotificationActivity.class,
            NavigationDrawerActivity.class,
            PaperTabStripActivity.class,
            ActionBarActivity.class,
            ShadowAndClippingActivity.class,
            CardViewActivity.class,
            ShapeTestActivity.class
    };
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this announce to improve performance if the layout size of RecyclerView will not be changed.
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(mActivityNames);
        mRecyclerView.setAdapter(mAdapter);
    }

    private String[] dataGenerator(int n) {
        String[] data = new String[n];
        for (int i = 0; i < n; i++) {
            data[i] = "RecyclerView ListItem " + i;
        }
        return data;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] dataSet;

        public MyAdapter(String[] dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            ViewHolder vh = new ViewHolder((Button) view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.button.setText(dataSet[position]);
        }

        @Override
        public int getItemCount() {
            return dataSet.length;
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
                Intent intent = new Intent(MainActivity.this, mActivityClasses[getAdapterPosition()]);
                startActivity(intent);
            }
        }
    }
}
