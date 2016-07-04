package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Float2;
import android.util.EventLogTags;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.CustomViews.MagnifierImageView;

public class MagnifierImageViewActivity extends Activity {
    private ImageView img;
    private MagnifierImageView.Magnifier magnifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier_image_view);

        magnifier = (MagnifierImageView.Magnifier) findViewById(R.id.magnifier);

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
                        float relativeX = x / img.getWidth();
                        float relativeY = y / img.getHeight();
                        magnifier.updateCenterByRelativeVals(relativeX, relativeY);
                        return true;
                }
                return false;
            }
        });

    }
}
