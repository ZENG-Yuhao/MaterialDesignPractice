package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ExemWelcomeActivity extends Activity {
    private ImageView img_logo, img_welcome;
    private int windowHeight;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_exem_welcome);

        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_welcome = (ImageView) findViewById(R.id.img_welcome);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        windowHeight = metrics.heightPixels;

        ViewTreeObserver observer = img_logo.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ObjectAnimator anim_logo = ObjectAnimator.ofFloat(img_logo, "translationY", windowHeight, img_logo
                        .getTranslationY());
                anim_logo.setDuration(700);
                anim_logo.setInterpolator(new DecelerateInterpolator());
                anim_logo.setStartDelay(200);
                anim_logo.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        img_logo.bringToFront();
                        img_logo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                anim_logo.start();

                ViewTreeObserver observer1 = img_logo.getViewTreeObserver();
                observer1.removeOnGlobalLayoutListener(this);
            }
        });

        ViewTreeObserver observer2 = img_welcome.getViewTreeObserver();
        observer2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

//                ObjectAnimator anim_welcome = ObjectAnimator.ofFloat(img_welcome, "translationY", windowHeight,
//                        img_welcome.getTranslationY());
                ObjectAnimator anim_welcome = ObjectAnimator.ofFloat(img_welcome, "alpha", 0f, 1f);
                anim_welcome.setDuration(2000);
                anim_welcome.setInterpolator(new AccelerateInterpolator());
                anim_welcome.setStartDelay(700);
                anim_welcome.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        img_welcome.bringToFront();
                        img_welcome.setVisibility(View.VISIBLE);
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
                anim_welcome.start();

                ViewTreeObserver observer3 = img_welcome.getViewTreeObserver();
                observer3.removeOnGlobalLayoutListener(this);
            }
        });
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
