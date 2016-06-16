package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

public class BottomToolBarActivity extends FragmentActivity {
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

    private static final int[] BACKGROUND_COLOR = {
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_green_dark,
            android.R.color.holo_red_dark
    };

    private final int NUM_PAGES = 5;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tool_bar);


        // init BottomToolBar
        BottomToolBar toolBar = (BottomToolBar) findViewById(R.id.bottom_toolbar);
        toolBar.setAdapter(new MyAdapter());
        toolBar.setOnItemClickListener(new BottomToolBar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position);
            }
        });

        // init ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new MyPageChangedListener());


        toolBar.bindViewPager(viewPager);
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
            holder.view.setElevation(position * 8);
            holder.view.setBackgroundColor(getResources().getColor(BACKGROUND_COLOR[position]));
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


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BlankFragment.newInstance(TITLE_STR[position], String.valueOf(position));
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private class MyPageChangedListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("haha", "onPageScrolled---->" + "position: " + position + " positionOffset:" + positionOffset + " " +
                    "positionOffsetPixels:" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i("haha", "onPageSelected---->" + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i("haha", "onPageScrollStateChanged---->" + state);
        }
    }
}
