package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

public class ShadowAndClippingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_and_clipping);

        CardView cardview = (CardView) findViewById(R.id.card_view);
        cardview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setTranslationZ(26);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setTranslationZ(0);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
