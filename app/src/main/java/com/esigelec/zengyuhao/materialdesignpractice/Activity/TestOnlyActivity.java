package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.esigelec.zengyuhao.materialdesignpractice.Core.ViewTest;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class TestOnlyActivity extends Activity {
    private ViewTest view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_only);

        view = (ViewTest) findViewById(R.id.view_test);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("RelativeLayout", "onLayout-->");
            }
        });

        Button btn1 = (Button) findViewById(R.id.btn_1);
        Button btn2 = (Button) findViewById(R.id.btn_2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setX(view.getX() + 10);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setLeft(view.getLeft() + 10);
                //layout.requestLayout();
            }
        });
    }
}
