package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Setting exit transition (xml)
 * 1.add an explode-transition into resources folder @see ./res/transition/grid_exit.xml
 * 2.declare the explode-transition in AppTheme @see ./res/values/styles.xml
 * 3.create bundle using ActivityOptions.makeSceneTransitionAnimation(context) (different with shared-element transi.)
 * <p/>
 *
 * Setting enter transition (code)
 * 1.add Slide programmatically @see #SharedElementDetailsActivity
 * 2.add target for Slide instance
 * 3.config (not compulsive)
 * 4.getWindow().setEnterTransition()
 * <p/>
 *
 * Setting shared-elements transition
 * 1.Indicate same android:transitionName between shared elements
 * (transitionName is a string for that it can be used between applications)
 * 2.Create Bundle using ActivityOptions.makeSceneTransitionAnimation(context, (sharedView, sharedViewName)...)
 * 3.Start the activity with bundle
 * <p/>
 *
 * ### Attention ###
 * 1.We may face the problem that shared-elements transitions does not work. Make sure the bundle is created off the
 * onCreate() method. Because in onCreate(), the view does not have height and width yet. So the
 * makeSceneTransitionAnimation() method may create the wrong animations.
 * <p/>
 *
 * 2.We may see <b>blinking effect</b> on status bar or navigation bar when transition between two activities.
 * That's because these bars fade in and out during the transition.
 * More details: {@see
 * <a href="http://stackoverflow.com/questions/26600263/how-do-i-prevent-the-status-bar-and-navigation-bar-from-animating-during-an-acti">Fix Blinking Effect</a> }
 *
 * 3.We may need declare windowContentTransition to true in style.xml. This attribute will be set to true automatically
 * if the AppTheme inherits already AppCompat or Material. @see ./res/values/styles.xml
 * <p/>
 * Reference:
 * @sse <a href="https://classroom.udacity.com/courses/ud862/lessons/4969789009/concepts/49083289230923">Udacity</a>
 * @see <a href="http://developer.android.com/training/material/animations.html">Defining Custom Animations</a>
 */
public class SharedElementsTransitionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_elements_transition);

        ImageView img = (ImageView) findViewById(R.id.img);
        /* Dangerous configuration in this field
           final Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(SharedElementsTransitionActivity.this,
           img, "transition_image").toBundle();
        */
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(SharedElementsTransitionActivity.this, v,
                        "transition_image").toBundle();
                Intent intent = new Intent(SharedElementsTransitionActivity.this, SharedElementDetailsActivity.class);
                startActivity(intent, bundle);
            }
        });
    }

}
