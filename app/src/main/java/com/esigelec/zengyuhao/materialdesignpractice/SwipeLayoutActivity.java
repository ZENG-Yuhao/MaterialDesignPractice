package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.SwipeLayout;

public class SwipeLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_layout);

        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe_layout);
        ImageView btn_edit = (ImageView) swipeLayout.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeLayoutActivity.this, "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView btn_delete = (ImageView) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeLayoutActivity.this, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
