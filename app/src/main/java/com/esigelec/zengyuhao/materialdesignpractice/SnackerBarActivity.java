package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SnackerBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacker_bar);

        CoordinatorLayout mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.mCoordinateLayout);
        final Snackbar snackerbar = Snackbar.make(mCoordinateLayout, R.string.str_snackerbar_message,
                Snackbar.LENGTH_SHORT);

        Button btn_show = (Button) findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackerbar.show();
            }
        });

    }
}
