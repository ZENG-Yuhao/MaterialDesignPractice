package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.Core.Helper.GPSLocatorHelper;
import com.esigelec.zengyuhao.materialdesignpractice.Core.Image.EfficientBitmap;

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
        map.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);
                bitmap = Bitmap.createScaledBitmap(bitmap, map.getWidth(), map.getHeight(), false);
                map.setImageBitmap(bitmap);
                helper = new GPSLocatorHelper(GPSLocatorActivity.this, map, bitmap, locator_blue, locator_red, locator_green);
                helper.getLocatorAt(0).setX(map.getX() + 100);
                helper.getLocatorAt(0).setY(map.getY() + 100);
                helper.getLocatorAt(1).setX(map.getX() + 300);
                helper.getLocatorAt(1).setY(map.getY() + 300);
                helper.getLocatorAt(2).setX(map.getX() + 600);
                helper.getLocatorAt(2).setY(map.getY() + 600);

                map.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

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


        map.setOnTouchListener(new TouchListener());
    }

    public class TouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            float relativeX = x / map.getWidth();
            float relativeY = y / map.getHeight();
            //Log.i("hah", "onTouch--->" + "rawX" + rawX + " rawY" + rawY + " map.getX()" + map.getX() + " map.getY()" + map.getY());
            if (relativeX < 0) rawX = 0;
            else if (relativeX >= 1) rawX = map.getWidth();

            if (relativeY < 0) rawY = 0;
            else if (relativeY > 1) rawY = map.getHeight();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
//                    Log.i("hah", "onTouch--->" + "rawX" + rawX + " rawY" + rawY + " relativeX" + relativeX + " relativeY" + relativeY);
                    helper.moveMagnifier(rawX, rawY, relativeX, relativeY);
                    return true;
                case MotionEvent.ACTION_UP:
                    helper.clearFocus();
                    helper.positionLocator(rawX, rawY);
                    return true;
            }
            return false;
        }
    }
}
