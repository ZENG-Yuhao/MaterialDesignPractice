package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.os.Bundle;
import android.app.Activity;

public class ActionBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i=0; i<3; i++){
            actionBar.addTab(actionBar.newTab().setText("Tab"+(i+1)).setTabListener(tabListener));
        }
    }

}
