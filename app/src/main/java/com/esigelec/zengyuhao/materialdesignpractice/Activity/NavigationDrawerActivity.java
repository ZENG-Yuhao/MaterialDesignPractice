package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.R;

import org.w3c.dom.Text;

public class NavigationDrawerActivity extends Activity {
    final private static String[] DRAWER_TITLES = {"Profile", "Info", "Favorite", "Attach", "Backup"};
    final private static int[] DRAWER_IMG_RESID = {R.drawable.ic_assignment_ind_black_24dp, R.drawable
            .ic_assignment_late_black_24dp, R.drawable.ic_assistant_black_24dp, R.drawable.ic_attach_file_black_24dp,
            R.drawable.ic_backup_black_24dp};

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        final ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(new DrawerListAdapter());

        // listen item click events
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item Clicked, position " + position, Toast.LENGTH_SHORT)
                        .show();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // listen drawer open and close events
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(getApplicationContext(), "Drawer opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(getApplicationContext(), "Drawer closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.str_open, R.string.str_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(mDrawerToggle);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
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
