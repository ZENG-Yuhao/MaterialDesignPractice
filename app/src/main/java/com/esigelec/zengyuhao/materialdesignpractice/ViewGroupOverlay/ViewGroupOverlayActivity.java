package com.esigelec.zengyuhao.materialdesignpractice.ViewGroupOverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ViewGroupOverlayActivity extends Activity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_overlay);

        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent_layout);
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateNinjaEffect(v, parent);
            }
        });
    }

    public static void animateNinjaEffect(final View target, final ViewGroup parent) {
        View rootView = target.getRootView();
        if (rootView != null && rootView instanceof ViewGroup) {
            final ViewGroupOverlay overlay = ((ViewGroup) rootView).getOverlay();
            overlay.add(target);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(ObjectAnimator.ofFloat(target, "scaleX", 1, 7f),
                    ObjectAnimator.ofFloat(target, "scaleY", 1, 7f),
                    ObjectAnimator.ofFloat(target, "alpha", 1, 0.0f)
            );
            animSet.setInterpolator(new AccelerateInterpolator());
            animSet.setDuration(400);
            animSet.start();
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    overlay.remove(target);
                    parent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parent.addView(target);
                            target.setAlpha(1f);
                            target.setScaleX(1f);
                            target.setScaleY(1f);
                        }
                    }, 400);
                }
            });

        }
    }
}
