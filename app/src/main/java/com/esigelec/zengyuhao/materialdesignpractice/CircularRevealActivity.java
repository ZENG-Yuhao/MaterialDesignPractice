package com.esigelec.zengyuhao.materialdesignpractice;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircularRevealActivity extends Activity {
    private TextView txt1, txt2;
    private Boolean isBlue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_reveal);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.my_linear_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double half_width = v.getWidth() / 2;
                double half_height = v.getHeight() / 2;
                int finalRadius = (int) Math.hypot(half_width, half_height);
                Animator anim = ViewAnimationUtils.createCircularReveal(v, (int) half_width, (int) half_height, 0,
                        finalRadius);
                anim.setDuration(400);
                if (isBlue) {
                    txt1.setText("Orange Title");
                    txt2.setText("Orange Content");
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                } else {
                    txt1.setText("Blue Title");
                    txt2.setText("Blue Content");
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
                isBlue = !isBlue;
                anim.start();
            }
        });
    }
}
