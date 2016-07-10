package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.GPSLocatorHelper;
import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.SimpleGPSLocatorInitializer;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class NewGPSLocatorActivity extends Activity {
    private GPSLocatorHelper helper;
    private ImageView map, locator_blue, locator_red, locator_green;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslocator);

        map = (ImageView) findViewById(R.id.map);
        locator_blue = (ImageView) findViewById(R.id.locator_blue);
        locator_red = (ImageView) findViewById(R.id.locator_red);
        locator_green = (ImageView) findViewById(R.id.locator_green);

        SimpleGPSLocatorInitializer initializer = new SimpleGPSLocatorInitializer(this, map, locator_blue,
                locator_red, locator_green);
        helper = initializer.init();
        initializer.setOnHelperObtainedListener(new SimpleGPSLocatorInitializer.OnHelperObtainedListener() {
            @Override
            public void onHelperObtained(GPSLocatorHelper helper) {
                locator_blue.setLeft(100);
                locator_blue.setRight(100);
                locator_blue.setTop(100);
                locator_blue.setBottom(100);
                helper.getLocatorAt(0).positionTo(map.getX() + 300, map.getY() + 300);
                helper.getLocatorAt(1).positionTo(map.getX() + 400, map.getY() + 300);
                helper.getLocatorAt(2).positionTo(map.getX() + 500, map.getY() + 300);

                helper.setOnLocatorPositionListener(new GPSLocatorHelper.OnLocatorPositionListener() {
                    @Override
                    public void onPositionChanged(GPSLocatorHelper.Locator locator, int index, float pxPosX, float
                            pxPosY,
                                                  float posX, float posY) {
//                        Log.i("GPS", "index:" + index + " pxPosX:" + pxPosX + " pxPosY:" + pxPosY + " posX:" + posX +
//                                " " +
//                                "posY:" + posY);

                    }
                });
            }
        });

        Button btn = (Button) findViewById(R.id.btn_request_layout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locator_blue.setLeft(locator_blue.getLeft() + 50);
//                locator_blue.setRight(locator_blue.getRight() + 50);
            }
        });
    }
}
