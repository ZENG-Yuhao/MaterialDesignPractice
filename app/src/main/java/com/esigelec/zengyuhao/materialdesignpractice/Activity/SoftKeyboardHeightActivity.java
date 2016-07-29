package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

import java.lang.reflect.Field;

public class SoftKeyboardHeightActivity extends Activity {
    private TextView txt_keyboard_height;
    private ImageView img_keyboard_height;
    private RelativeLayout layout_main;
    private ViewTreeObserver.OnGlobalLayoutListener mListener;
    private StringBuilder output = new StringBuilder();
    private boolean keyboardState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_keyboard_height);

        txt_keyboard_height = (TextView) findViewById(R.id.txt_keyboard_height);
        img_keyboard_height = (ImageView) findViewById(R.id.img_keyboard_height);
        layout_main = (RelativeLayout) findViewById(R.id.layout_main);

        mListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                int screenHeight = layout_main.getRootView().getHeight();
                int heightDiff = screenHeight - (rect.bottom - rect.top);
                int statusBarHeight = getStatusBarHeight();
                int navBarHeight = getNavigationBarHeight();

                boolean isShown = (heightDiff - statusBarHeight < screenHeight / 5);
                if (isShown != keyboardState) {
                    keyboardState = isShown;
                    output.append("screenHeight = " + screenHeight);
                    output.append("\n");
                    output.append("statusBarHeight = " + statusBarHeight);
                    output.append("\n");
                    output.append("navBarHeight = " + navBarHeight);
                    output.append("\n");
                    output.append("heightDiff = " + heightDiff);
                    output.append("\n");
                    output.append("getWindow().getDecorView() = " + getWindow().getDecorView().toString());
                    output.append("\n");
                    output.append("getWindow().getDecorView().getRootView() = " + getWindow().getDecorView().getRootView
                            ().toString());
                    output.append("\n");
                    output.append("layout_main.getRootView() = " + layout_main.getRootView().toString());

                    Log.i("Keyboard", "------> /n" + output);
                    ViewGroup.LayoutParams lp = img_keyboard_height.getLayoutParams();
                    lp.height = heightDiff - statusBarHeight - navBarHeight;
                    img_keyboard_height.requestLayout();

                }

            }
        };

        layout_main.getViewTreeObserver().addOnGlobalLayoutListener(mListener);
    }

    private void onKeyboardShow(int height) {
        txt_keyboard_height.setText("Keyboard show - height: " + height);
    }

    private void onKeyboardHide(int height) {
        txt_keyboard_height.setText("Keyboard hide - height: " + height);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        layout_main.getViewTreeObserver().removeOnGlobalLayoutListener(mListener);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
