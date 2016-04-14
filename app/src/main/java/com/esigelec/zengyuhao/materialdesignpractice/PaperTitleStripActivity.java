package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.Core.Image.EfficientBitmap;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class PaperTitleStripActivity extends Activity {
    private static String[] titles = {"Dice", "Play", "Info", "Android", "Wu", "Earth"};
    private static int[] imageResId = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable
            .img4, R.drawable.img5};

    private static Bitmap mPlaceHolderBitmap;

    private int pagerHeigth, pagerWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_title_strip);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);


        final ViewTreeObserver observer = viewPager.getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pagerHeigth = viewPager.getHeight();
                    pagerWidth = viewPager.getWidth();
                    mPlaceHolderBitmap = EfficientBitmap.decodeBitmap(getResources(), R.drawable.ic_action_replay,
                            pagerHeigth, pagerWidth);

                    observer.removeOnGlobalLayoutListener(this);
                }
            });
        }
        viewPager.setAdapter(new MyPagerAdapter());


    }

    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getBaseContext());
            EfficientBitmap.loadBitmap(getResources(), imageView, mPlaceHolderBitmap, imageResId[position],
                    pagerWidth, pagerHeigth, EfficientBitmap.DecoderAsyncTask.MODE_NO_MEMORY_CACHE);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
