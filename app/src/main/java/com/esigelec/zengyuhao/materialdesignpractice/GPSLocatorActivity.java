package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.GPSLocatorHelper;

public class GPSLocatorActivity extends Activity {
    private GPSLocatorHelper helper;
    private ImageView map, locator_blue, locator_red, locator_green;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslocator);

        layout = (RelativeLayout) findViewById(R.id.root_layout);
        map = (ImageView) findViewById(R.id.map);
        locator_blue = (ImageView) findViewById(R.id.locator_blue);
        locator_red = (ImageView) findViewById(R.id.locator_red);
        locator_green = (ImageView) findViewById(R.id.locator_green);

        locator_blue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    helper.setFocusLocator(0);
                }
                return false;
            }
        });

        locator_red.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    helper.setFocusLocator(1);
                }
                return false;
            }
        });

        locator_green.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    helper.setFocusLocator(2);
                }
                return false;
            }
        });

        layout.setOnTouchListener(new TouchListener());

        map.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);
                bitmap = Bitmap.createScaledBitmap(bitmap, map.getWidth() - map.getPaddingLeft() - map
                        .getPaddingRight(), map.getHeight() - map.getPaddingTop() - map.getPaddingBottom(), false);
                map.setImageBitmap(bitmap);
                helper = new GPSLocatorHelper(GPSLocatorActivity.this, map, bitmap, locator_blue, locator_red,
                        locator_green);
                helper.getLocatorAt(0).positionTo(map.getX() + 300, map.getY() + 300);
                helper.getLocatorAt(1).positionTo(map.getX() + 400, map.getY() + 400);
                helper.getLocatorAt(2).positionTo(map.getX() + 500, map.getY() + 500);
                helper.setOnLocatorPositionListener(new GPSLocatorHelper.OnLocatorPositionListener() {
                    @Override
                    public void onPositionChanged(GPSLocatorHelper.Locator locator, int index, float pxPosX, float
                            pxPosY,
                                                  float posX, float posY) {
                        Log.i("GPS", "index:" + index + " pxPosX:" + pxPosX + " pxPosY:" + pxPosY + " posX:" + posX +
                                " " +
                                "posY:" + posY);
                    }
                });
                map.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public class TouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX() - map.getLeft();
            float y = event.getY() - map.getTop();
            float rawX = event.getRawX();
            float rawY = event.getRawY();
//            float relativeX = x / map.getWidth();
//            float relativeY = y / map.getHeight();

            float[] position = GPSLocatorHelper.getLocationRelatedTo(map, rawX, rawY, true);
            float relativeX = position[2];
            float relativeY = position[3];
            //Log.i("hah", "onTouch--->" + "rawX" + rawX + " rawY" + rawY + " map.getX()" + map.getX() + " map.getY()
            // " + map.getY());
//            if (relativeX < 0) rawX = 0;
//            else if (relativeX >= 1) rawX = map.getWidth();
//
//            if (relativeY < 0) rawY = 0;
//            else if (relativeY > 1) rawY = map.getHeight();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
//                    Log.i("hah", "onTouch--->" + "rawX" + rawX + " rawY" + rawY + " relativeX" + relativeX + "
// relativeY" + relativeY);
                    helper.moveMagnifier(rawX, rawY, relativeX, relativeY);
                    helper.positionLocator(rawX, rawY);
                    return true;
                case MotionEvent.ACTION_UP:
                    helper.clearFocus();
                    return true;
            }
            return false;
        }
    }
}
