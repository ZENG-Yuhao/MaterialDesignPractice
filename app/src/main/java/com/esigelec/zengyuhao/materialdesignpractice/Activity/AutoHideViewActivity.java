package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

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
