package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.esigelec.zengyuhao.materialdesignpractice.BuildConfig;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class BottomToolBarActivity extends AppCompatActivity {
    private static final int[] IMG_SRC = {
            R.drawable.ic_information_white,
            R.drawable.ic_antenne_white,
            R.drawable.ic_localization_white,
            R.drawable.ic_antenne_casb_white,
            R.drawable.ic_scanner_white,
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
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SideToolbar", "------>start oncreate:" + System.currentTimeMillis());
        setContentView(R.layout.activity_bottom_tool_bar);
        Log.i("SideToolbar", "------>finish setContentView:" + System.currentTimeMillis());
        // init BottomToolBar
        MyAdapter adapter = new MyAdapter();
        Log.i("SideToolbar", "------>finish createAdapter:" + System.currentTimeMillis());
        SideToolbar toolBar = (SideToolbar) findViewById(R.id.bottom_toolbar);
        toolBar.setAdapter(adapter);
        Log.i("SideToolbar", "------>finish setAdapter:" + System.currentTimeMillis());
        toolBar.setOnItemClickListener(new SideToolbar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        Log.i("SideToolbar", "------>finish setOnItemClick:" + System.currentTimeMillis());
        // init ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolBar.bindViewPager(viewPager);
        Log.i("SideToolbar", "------>finish bindViewPager:" + System.currentTimeMillis());

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sidebar");
        }
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //toolBar.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new MyPageChangedListener());
        Log.i("SideToolbar", "------>finish onCreate:" + System.currentTimeMillis());
    }

    public class MyAdapter extends SideToolbar.Adapter<MyAdapter.MyHolder> {

        @Override
        public int getItemCount() {
            return IMG_SRC.length;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_tool_bar, parent, false);

            //TextView txt = (TextView) view.findViewById(R.id.tab_title);
            ImageView img = (ImageView) view.findViewById(R.id.tab_icon);
            //MyHolder holder = new MyHolder(view, img, txt);
            MyHolder holder = new MyHolder(view, img, null);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.img.setImageResource(IMG_SRC[position]);
            //holder.txt.setText(TITLE_STR[position]);
            holder.view.setElevation(2);
            holder.view.setBackgroundColor(getResources().getColor(BACKGROUND_COLOR[position]));
        }

        public class MyHolder extends SideToolbar.ViewHolder {
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


    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
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
            Log.i("haha", "onPageScrolled---->" + position + " : " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
//
//            double half_width = 0;
//            int finalRadius = (int) Math.hypot(half_width, myToolbar.getHeight());
//            Animator animator = ViewAnimationUtils.createCircularReveal(myToolbar, (int)half_width, myToolbar
//                    .getHeight(), 0, finalRadius);
//            animator.setDuration(200);
//            animator.start();

            if (BuildConfig.DEBUG)
                Log.i("haha", "onPageSelected--------------------------->" + position);
            getSupportActionBar().setTitle(TITLE_STR[position]);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i("haha", "onPageScrollStateChanged--------------------------->" + state);
        }
    }
}
