package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.GPSLocatorHelper;
import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.SimpleGPSLocatorInitializer;

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

        helper = new SimpleGPSLocatorInitializer(this, map, locator_blue, locator_red, locator_green).init();

    }
}
