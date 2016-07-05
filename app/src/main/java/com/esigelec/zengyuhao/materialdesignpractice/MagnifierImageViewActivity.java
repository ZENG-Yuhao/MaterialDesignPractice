package com.esigelec.zengyuhao.materialdesignpractice;

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

import com.esigelec.zengyuhao.materialdesignpractice.CustomViews.MagnifierView;

public class MagnifierImageViewActivity extends Activity {
    private static final String TAG = "MagnifierImageView";
    private ImageView img;
    private MagnifierView magnifier;
    private ViewGroupOverlay overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier_image_view);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);

        //magnifier.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        //magnifier.setElevation(8);


        img = (ImageView) findViewById(R.id.img1);
        img.setOnTouchListener(new MyOnTouchListener());


        magnifier = new MagnifierView(this);
        magnifier.setSize(500, 500);
        View rootView = img.getRootView();
        if (rootView != null && rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).addView(magnifier);
            overlay = ((ViewGroup) rootView).getOverlay();
        }

        magnifier.bindBitmap(((BitmapDrawable) img.getDrawable()).getBitmap());
        magnifier.updateCenterByRelativeVals(0.5, 0.5);
        magnifier.disappearFast();

        magnifier.setOnAppearDisappearListener(new MagnifierView.OnAppearDisappearListener() {
            @Override
            public void onBeforeAppear() {
                overlay.add(magnifier);
            }

            @Override
            public void onBeforeDisappear() {

            }

            @Override
            public void onAppeared() {

            }

            @Override
            public void onDisappeared() {
                overlay.remove(magnifier);
            }
        });
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
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_DOWN");
                    magnifier.appear();
                    magnifier.setX(rawX - magnifier.getWidth() / 2);
                    magnifier.setY(rawY - magnifier.getHeight());

                    Log.i(TAG, "ACTION_MOVE");
                    float relativeX = x / img.getWidth();
                    float relativeY = y / img.getHeight();
                    //Log.i(TAG, "onTouch---->:" + relativeX + " ## " + relativeY);
                    magnifier.updateCenterByRelativeVals(relativeX, relativeY);
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP");
                    magnifier.disappear();
                    return true;
            }
            return false;
        }
    }
}
