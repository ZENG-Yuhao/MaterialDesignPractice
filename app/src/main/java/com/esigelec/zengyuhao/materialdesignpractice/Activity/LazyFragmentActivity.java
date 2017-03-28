package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.esigelec.zengyuhao.materialdesignpractice.Fragment.temp.LazyLoadGraphFragment;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class LazyFragmentActivity extends AppCompatActivity {

    private static String[] titles = {"Dice", "Play", "Info", "Android", "Wu", "Earth", "Mouse", "Monitor", "K"};
    private static int[] imageResId = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable
            .img4, R.drawable.img5};

    private int pagerHeigth = 100, pagerWidth = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_fragment);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        viewPager.setCurrentItem(0);

        viewPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = titles.length;
                int min = 0;
                int rand = (int) (Math.random() * (max - min) + min);
                viewPager.setCurrentItem(rand);
            }
        });

        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i("addOnPageChangeListener", "-->" + state + " : " + System.currentTimeMillis());
                if (state == 0) {
                    Log.d("TAG", "      ");
                    Log.d("TAG", "      ");
                    Log.d("TAG", "      ");
                    Log.d("TAG", "      ");
                }
            }
        });


//        try {
//            Field mScroller;
//            mScroller = ViewPager.class.getDeclaredField("mScroller");
//            mScroller.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
//            // scroller.setFixedDuration(5000);
//            mScroller.set(viewPager, scroller);
//        } catch (NoSuchFieldException e) {
//        } catch (IllegalArgumentException e) {
//        } catch (IllegalAccessException e) {
//        }

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return LazyLoadTestFragment.newInstance(OldBaseLazyFragment.MODE_LAZY, position);
            LazyLoadGraphFragment fragment = new LazyLoadGraphFragment();
//            Bundle args = new Bundle();
//            args.putInt(LazyLoadGraphFragment.ARG_MODE, LazyLoadGraphFragment.MODE_DEEP_LAZY);
//            args.putInt("position", position);
//            fragment.setArguments(args);
            fragment.setPosition(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 200;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
