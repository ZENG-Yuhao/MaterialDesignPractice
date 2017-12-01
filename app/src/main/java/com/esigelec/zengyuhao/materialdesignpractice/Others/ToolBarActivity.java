package com.esigelec.zengyuhao.materialdesignpractice.Others;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ToolBarActivity extends AppCompatActivity {
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("ToolBar");

        myToolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = myToolbar.findViewById(R.id.action_attach);

                if (item != null) {
                    myToolbar.removeOnLayoutChangeListener(this);
                    ObjectAnimator anim = ObjectAnimator.ofInt(item, "paddingRight", -100, item.getPaddingRight());
                    anim.setDuration(0);
                    anim.setInterpolator(new DecelerateInterpolator());

                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(item, "scaleX", 0f, 1f);
                    anim1.setDuration(1500);
                    anim1.setInterpolator(new BounceInterpolator());

                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(anim).before(anim1);
                    animSet.start();

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_attach:
                Toast.makeText(ToolBarActivity.this, "Item attach selected.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(ToolBarActivity.this, "Item settings selected.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
