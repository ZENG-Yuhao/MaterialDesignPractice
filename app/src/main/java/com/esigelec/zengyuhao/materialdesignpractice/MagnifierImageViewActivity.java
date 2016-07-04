package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esigelec.zengyuhao.materialdesignpractice.CustomViews.MagnifierImageView;

public class MagnifierImageViewActivity extends Activity {
    private static final String TAG = "MagnifierImageView";
    private ImageView img;
    private MagnifierImageView.Magnifier magnifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier_image_view);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
        magnifier = new MagnifierImageView.Magnifier(this);
        magnifier.setSize(500, 500);
        magnifier.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        layout.addView(magnifier);


        img = (ImageView) findViewById(R.id.img1);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        float x = event.getX();
                        float y = event.getY();
                        //Log.i(TAG, "onTouch---->:" + x + " " + y);
                        float relativeX = x / img.getWidth();
                        float relativeY = y / img.getHeight();
                        //Log.i(TAG, "onTouch---->:" + relativeX + " ## " + relativeY);
                        magnifier.updateCenterByRelativeVals(relativeX, relativeY);
                        return true;
                }
                return false;
            }
        });
        magnifier.bindBitmap(((BitmapDrawable) img.getDrawable()).getBitmap());
        magnifier.updateCenterByRelativeVals(0.5, 0.5);
        ImageView img2 = (ImageView) findViewById(R.id.img2);
        img2.setImageBitmap(((BitmapDrawable) img.getDrawable()).getBitmap());

    }
}
