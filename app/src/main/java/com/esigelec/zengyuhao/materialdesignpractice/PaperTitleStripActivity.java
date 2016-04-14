package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class PaperTitleStripActivity extends Activity {
    private ArrayList<View> viewList;
    private String[] titles = {"Dice", "Play", "Info", "Android", "Wu", "Earth"};
    private ImageView[] images;
    private int[] imageResId = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_title_strip);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);


    }

    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
