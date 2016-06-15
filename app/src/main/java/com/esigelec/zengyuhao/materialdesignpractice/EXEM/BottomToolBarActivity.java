package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.BottomToolBar.BottomToolBar;
import com.esigelec.zengyuhao.materialdesignpractice.R;

import org.w3c.dom.Text;

public class BottomToolBarActivity extends Activity {
    private static final int[] IMG_SRC = {
            R.drawable.ic_assistant_black_24dp,
            R.drawable.ic_attach_file_black_24dp,
            R.drawable.ic_assignment_ind_black_24dp,
            R.drawable.ic_assignment_late_black_24dp,
            R.drawable.ic_backup_black_24dp,
    };

    private static final String[] TITLE_STR = {
            "Info Site",
            "Installation",
            "Cartographie",
            "Cas B",
            "Scanner"
    };

    private TextView txt_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tool_bar);

        final TextView txt_state = (TextView) findViewById(R.id.txtvw_state);

        BottomToolBar toolBar = (BottomToolBar) findViewById(R.id.bottom_toolbar);
        toolBar.setAdapter(new MyAdapter());
        toolBar.setOnItemClickListener(new BottomToolBar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                txt_state.setText(TITLE_STR[position]);
            }
        });

    }

    public class MyAdapter extends BottomToolBar.Adapter<MyAdapter.MyHolder> {

        @Override
        public int getItemCount() {
            return IMG_SRC.length;
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
            holder.img.setImageResource(IMG_SRC[position]);
            holder.txt.setText(TITLE_STR[position]);
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
