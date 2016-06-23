package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.SwipeLayout.SwipeLayout;

public class SwipeLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_layout);

        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe_layout);
        Button btn1 = (Button) swipeLayout.findViewById(R.id.btn_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn2 = (Button) swipeLayout.findViewById(R.id.btn_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello Android", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
