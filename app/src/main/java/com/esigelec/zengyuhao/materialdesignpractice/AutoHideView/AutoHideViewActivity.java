package com.esigelec.zengyuhao.materialdesignpractice.AutoHideView;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class AutoHideViewActivity extends Activity {
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_hide_view);

        fab = (FloatingActionButton) findViewById(R.id.fab);


    }
}
