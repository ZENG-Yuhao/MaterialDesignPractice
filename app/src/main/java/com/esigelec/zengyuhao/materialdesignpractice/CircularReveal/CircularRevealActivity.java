package com.esigelec.zengyuhao.materialdesignpractice.CircularReveal;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class CircularRevealActivity extends Activity {
    private TextView txt1, txt2, txt3, txt4;
    private Boolean isBlue = true;
    private LinearLayout linearLayout, linearLayout1;
    private Animator anim, anim1;// prevent starting another animation before the end of current animation
    private Animator anim3, anim4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_reveal);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);

        linearLayout = (LinearLayout) findViewById(R.id.my_linear_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevent starting another animation before the end of current animation
                linearLayout.setClickable(false);

                // calculate params
                double half_width = v.getWidth() / 2;
                double half_height = v.getHeight() / 2;
                int finalRadius = (int) Math.hypot(half_width, half_height);

                // set exit animator
                anim = ViewAnimationUtils.createCircularReveal(v, (int) half_width, (int) half_height,
                        finalRadius, 0);
                anim.setDuration(200);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        changeContent();
                        anim1.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                // set enter animator
                anim1 = ViewAnimationUtils.createCircularReveal(v, (int) half_width, (int) half_height, 0,
                        finalRadius);
                anim1.setDuration(200);
                anim1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // animation set finished, reset the clickable state
                        linearLayout.setClickable(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                linearLayout1.setVisibility(View.VISIBLE);
                // start exit animator and then enter animator
                //anim.start();
                int finalRadius1 = (int) Math.hypot(v.getWidth(), v.getHeight());

                anim3 = ViewAnimationUtils.createCircularReveal(linearLayout1, 0, (int)half_height/3,
                        0, finalRadius1);
                anim3.setDuration(400);
                anim3.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                anim3.start();
            }
        });


        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt3.setText("Orange Title");
        txt4.setText("Orange Content");
        linearLayout1 = (LinearLayout) findViewById(R.id.my_linear_layout_1);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout1.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

    }

    private void changeContent() {
        if (isBlue) {
            txt1.setText("Orange Title");
            txt2.setText("Orange Content");
            linearLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            txt1.setText("Blue Title");
            txt2.setText("Blue Content");
            linearLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
        isBlue = !isBlue;
    }


}
