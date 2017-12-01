package com.esigelec.zengyuhao.materialdesignpractice.GPSLocator;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.GPSLocator.Widgets.MagnifierView;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class MagnifierImageViewActivity extends Activity {
    private static final String TAG = "MagnifierImageView";
    private ImageView img;
    private MagnifierView magnifier;
    private ViewGroupOverlay overlay;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier_image_view);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);

        //magnifier.setDefaultBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        //magnifier.setElevation(8);


        img = (ImageView) findViewById(R.id.img1);
        img.setOnTouchListener(new MyOnTouchListener());


        magnifier = new MagnifierView(this);
        magnifier.setSize(600, 600);
        magnifier.disableVisibleCenter();
        magnifier.setElevation(16);
        View rootView = img.getRootView();
        if (rootView != null && rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).addView(magnifier);
            overlay = ((ViewGroup) rootView).getOverlay();
        }

        magnifier.bindBitmap(((BitmapDrawable) img.getDrawable()).getBitmap());
        magnifier.updateCenterByFractionVals(0.5, 0.5);
        magnifier.disappearImmediately();

        magnifier.setOnAppearDisappearListener(new MagnifierView.OnAppearDisappearListener() {
            @Override
            public void onBeforeAppear() {
//                overlay.add(magnifier);
//                overlay.add(textView);
//                magnifier.setElevation(8);
//                textView.setElevation(8);
            }

            @Override
            public void onBeforeDisappear() {

            }

            @Override
            public void onAppeared() {

            }

            @Override
            public void onDisappeared() {
//                overlay.remove(magnifier);
            }
        });

        textView = (TextView) findViewById(R.id.text_view);


    }

    private class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (magnifier == null || overlay == null) return false;
            float x = event.getX();
            float y = event.getY();
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    magnifier.appear();
                case MotionEvent.ACTION_MOVE:
                    //Log.i(TAG, "ACTION_DOWN");


                    //Log.i(TAG, "ACTION_MOVE");
                    float relativeX = x / img.getWidth();
                    float relativeY = y / img.getHeight();
                    Log.i(TAG, "onTouch---->:" + relativeX + " ## " + relativeY);
                    magnifier.updateCenterByFractionVals(relativeX, relativeY);
                    if (relativeX >= 0 && relativeX <= 1)
                        magnifier.setX(rawX - magnifier.getWidth() / 2);
                    if (relativeY >= 0 && relativeY <= 1)
                        magnifier.setY(rawY - magnifier.getHeight());
                    return true;
                case MotionEvent.ACTION_UP:
                    //Log.i(TAG, "ACTION_UP");
                    magnifier.disappear();
                    return true;
            }
            return false;
        }
    }
}
