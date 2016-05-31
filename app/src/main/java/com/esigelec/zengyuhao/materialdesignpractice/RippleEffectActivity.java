package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 1.Create a ripple in drawable @see ./res/drawable/ripple_rect.xmlt.xml which can give a shape to calculate the
 * bounds of
 * ripple effect.
 * 2.Set the ripple shape as background of the view
 * 3 Create animator with selector to change translationZ @see ./res/animator/pressed_raise.xml
 * 3.Set android:stateListAnimator attribute of the view
 */
public class RippleEffectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_effect);
    }

    @Override
    protected void onResume() {
         /*
         If you hide the system bars in your activity's onCreate() method and the user presses Home, the system bars
         will reappear. When the user reopens the activity, onCreate() won't get called, so the system bars will
         remain visible. If you want system UI changes to persist as the user navigates in and out of your activity,
         set UI flags in onResume() or onWindowFocusChanged().
         */
//        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View
//                .SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        super.onResume();

    }
}
