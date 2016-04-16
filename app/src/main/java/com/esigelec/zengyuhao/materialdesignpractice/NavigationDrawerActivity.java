package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationDrawerActivity extends Activity {
    final private static String[] DRAWER_TITLES = {"Profile", "Info", "Favorite", "Attach", "Backup"};
    final private static int[] DRAWER_IMG_RESID = {R.drawable.ic_assignment_ind_black_24dp, R.drawable
            .ic_assignment_late_black_24dp, R.drawable.ic_assistant_black_24dp, R.drawable.ic_attach_file_black_24dp,
            R.drawable.ic_backup_black_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(new DrawerListAdapter());
    }

    public class DrawerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return DRAWER_TITLES.length;
        }

        @Override
        public Object getItem(int position) {
            return DRAWER_IMG_RESID[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_drawer_list, null, false);
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
//                lp.width = 240;
//                lp.height = 100;
//                view.setLayoutParams(lp);
                // icon
                ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
                imageView.setImageResource(DRAWER_IMG_RESID[position]);

                // content
                TextView textView = (TextView) view.findViewById(R.id.text_view);
                textView.setText(DRAWER_TITLES[position]);
            } else
                view = convertView;
            return view;
        }
    }
}
