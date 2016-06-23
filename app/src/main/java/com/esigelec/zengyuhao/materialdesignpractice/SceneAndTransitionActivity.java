package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SceneAndTransitionActivity extends Activity {
    private Scene scene_a, scene_b;
    private ViewGroup scene_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_and_transition);
        scene_root = (ViewGroup) findViewById(R.id.scene_root);
        scene_a = Scene.getSceneForLayout(scene_root, R.layout.scene_a, this);
        scene_b = Scene.getSceneForLayout(scene_root, R.layout.scene_b, this);

        final Button btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.go(scene_b, new ChangeBounds());

                Log.i("TEST", "--->X:" + btn_change.getX() + " TranslationX:" + btn_change.getTranslationX() + " Left:" + btn_change
                        .getLeft());
            }
        });

        Button btn_recovery = (Button) findViewById(R.id.btn_recovery);
        btn_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.go(scene_a, new Slide());
            }
        });

    }
}
