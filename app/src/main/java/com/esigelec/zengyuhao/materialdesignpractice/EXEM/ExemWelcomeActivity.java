package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.ViewWrapper;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ExemWelcomeActivity extends Activity {
    private ImageView img_logo, img_welcome;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_exem_welcome);

        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_welcome = (ImageView) findViewById(R.id.img_welcome);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int windowHeight = metrics.heightPixels;
        ObjectAnimator anim_logo = ObjectAnimator.ofInt(img_logo, "top", windowHeight, img_logo.getTop());
        anim_logo.setDuration(500);
        anim_logo.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator anim_welcome = ObjectAnimator.ofInt(img_welcome, "top", windowHeight, img_welcome.getTop());
        anim_welcome.setDuration(500);
        anim_welcome.setInterpolator(new DecelerateInterpolator());
        anim_welcome.setStartDelay(250);

        anim_logo.start();
        anim_welcome.start();

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
