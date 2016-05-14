package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;

/**
 * 1.Create a ripple in drawable @see ./res/drawable/ripple_rect.xmlt.xml which can give a shape to calculate the bounds of
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
        getActionBar().setTitle("RippleEffect");
    }
}
