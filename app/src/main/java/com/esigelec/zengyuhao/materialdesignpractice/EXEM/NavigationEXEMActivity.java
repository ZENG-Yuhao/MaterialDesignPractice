package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esigelec.zengyuhao.materialdesignpractice.R;
import com.esigelec.zengyuhao.materialdesignpractice.SwipeLayoutActivity;


public class NavigationEXEMActivity extends Activity {
    private Button btn_bottom_toolbar;
    private Button btn_welcome_screen;
    private Button btn_swipe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_exem);

        btn_bottom_toolbar = (Button) findViewById(R.id.btn_bottom_toolbar);
        btn_bottom_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationEXEMActivity.this, BottomToolBarActivity.class);
                startActivity(intent);
            }
        });

        btn_welcome_screen = (Button) findViewById(R.id.btn_welcome_screen);
        btn_welcome_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(NavigationEXEMActivity.this, v,
                        "transition_image").toBundle();
                Intent intent = new Intent(NavigationEXEMActivity.this, ExemWelcomeActivity.class);
                startActivity(intent, bundle);
            }
        });

        btn_swipe_layout = (Button) findViewById(R.id.btn_swipe_layout);
        btn_swipe_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationEXEMActivity.this, SwipeLayoutActivity.class);
                startActivity(intent);
            }
        });
    }
}
