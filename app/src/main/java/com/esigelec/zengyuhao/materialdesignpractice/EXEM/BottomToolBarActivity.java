package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar.BottomToolBar;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class BottomToolBarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tool_bar);

        BottomToolBar toolBar = (BottomToolBar) findViewById(R.id.bottom_toolbar);
        toolBar.setAdapter(new MyAdapter());
        toolBar.setOnItemClickListener(new BottomToolBar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        final Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, v.getHeight());
                params.weight = 1;
                v.setLayoutParams(params);
            }
        });
    }

    public class MyAdapter extends BottomToolBar.Adapter<MyAdapter.MyHolder> {

        @Override
        public int getItemCount() {
            return 5;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_tool_bar, parent, false);

            TextView txt = (TextView) view.findViewById(R.id.tab_title);
            ImageView img = (ImageView) view.findViewById(R.id.tab_icon);
            MyHolder holder = new MyHolder(view, img, txt);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.img.setImageResource(R.drawable.ic_devices_black_48dp);
            holder.txt.setText("TAB" + String.valueOf(position));
            //holder.view.setElevation(position * 4);
        }

        public class MyHolder extends BottomToolBar.ViewHolder {
            public View view;
            public ImageView img;
            public TextView txt;

            public MyHolder(View view, ImageView img, TextView txt) {
                super(view);
                this.view = view;
                this.img = img;
                this.txt = txt;
            }
        }

    }
}
