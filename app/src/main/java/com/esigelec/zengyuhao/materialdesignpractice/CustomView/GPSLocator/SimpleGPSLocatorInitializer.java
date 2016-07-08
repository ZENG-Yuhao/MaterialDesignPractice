package com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * <p>
 * Created by ZENG Yuhao on 16/7/8. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class SimpleGPSLocatorInitializer {
    private Context context;
    private ImageView map;
    private View[] locatorViews;
    private GPSLocatorHelper helper;

    public SimpleGPSLocatorInitializer(Context context, ImageView map, View... locatorViews) {
        this.context = context;
        this.map = map;
        this.locatorViews = locatorViews;
    }

    public GPSLocatorHelper init() {
        View rootView = map.getRootView();
        rootView.setOnTouchListener(new ScreenTouchListener());

        map.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.map);
                bitmap = Bitmap.createScaledBitmap(bitmap, map.getWidth() - map.getPaddingLeft() - map
                        .getPaddingRight(), map.getHeight() - map.getPaddingTop() - map.getPaddingBottom(), false);
                map.setImageBitmap(bitmap);
                helper = new GPSLocatorHelper(context, map, bitmap, locatorViews);
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
                helper.getLocatorAt(0).positionTo(map.getX() + 300, map.getY() + 300);
                helper.getLocatorAt(1).positionTo(map.getX() + 400, map.getY() + 300);
                helper.getLocatorAt(2).positionTo(map.getX() + 500, map.getY() + 300);

                map.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        int index = 0;
        for (View view : locatorViews) {
            view.setOnTouchListener(new LocatorViewTouchListener(index));
            index++;
        }

        return helper;
    }

    private class LocatorViewTouchListener implements View.OnTouchListener {
        private int index;

        public LocatorViewTouchListener(int index) {
            this.index = index;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                helper.setFocusLocator(index);
            }
            return false;
        }
    }

    private class ScreenTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX() - map.getLeft();
            float y = event.getY() - map.getTop();
            float rawX = event.getRawX();
            float rawY = event.getRawY();

            float[] position = GPSLocatorHelper.getLocationRelatedTo(map, rawX, rawY, true);
            float relativeX = position[2];
            float relativeY = position[3];


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
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
