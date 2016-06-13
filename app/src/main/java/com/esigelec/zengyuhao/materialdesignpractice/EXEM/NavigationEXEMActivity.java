package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esigelec.zengyuhao.materialdesignpractice.R;


public class NavigationEXEMActivity extends Activity {
    private Button btn_bottom_toolbar;

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
    }
}
