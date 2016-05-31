package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.Core.Image.EfficientBitmap;

public class PaperTabStripActivity extends Activity {
    private static String[] titles = {"Dice", "Play", "Info", "Android", "Wu", "Earth"};
    private static int[] imageResId = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable
            .img4, R.drawable.img5};

    private static Bitmap mPlaceHolderBitmap;

    private int pagerHeigth = 100, pagerWidth = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_title_strip);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        final ViewTreeObserver observer = viewPager.getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pagerHeigth = viewPager.getHeight();
                    pagerWidth = viewPager.getWidth();
                    mPlaceHolderBitmap = EfficientBitmap.decodeBitmap(getResources(), R.drawable.ic_action_replay,
                            pagerHeigth, pagerWidth);

                    ViewTreeObserver observer1 = viewPager.getViewTreeObserver();
                    observer1.removeOnGlobalLayoutListener(this);
                }
            });
        }
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(0);

    }

    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            EfficientBitmap.loadBitmap(getResources(), imageView, mPlaceHolderBitmap, imageResId[position],
                    pagerWidth, pagerHeigth, EfficientBitmap.DecoderAsyncTask.MODE_NO_MEMORY_CACHE);
            container.addView(imageView);
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
