package com.esigelec.zengyuhao.materialdesignpractice.Keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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
//        removeAllKeyboardStateChangedListener();
//        unregisterKeyboardGlobalLayoutListener();
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

//    /* for soft keyboard */
//    private ArrayList<KeyboardStateChangedListener> mKeyboardStateListeners;
//    private KeyboardGlobalLayoutListener mKeyboardGlobalLayoutListener;
//    private boolean mKeyBoardVisibleState = false;
//    private int mKeyBoardHeight = 0;

//    public void addKeyboardStateChangedListener(KeyboardStateChangedListener listener) {
//        if (mKeyboardStateListeners == null) {
//            mKeyboardStateListeners = new ArrayList<>();
//            mKeyboardStateListeners.add(listener);
//            registerKeyboardGlobalLayoutListener();
//        } else
//            mKeyboardStateListeners.add(listener);
//    }
//
//    public boolean removeKeyboardStateChangedListener(KeyboardStateChangedListener listener) {
//        boolean isFound = false;
//        for (KeyboardStateChangedListener elem : mKeyboardStateListeners) {
//            if (elem == listener) {
//                mKeyboardStateListeners.remove(listener);
//                isFound = true;
//            }
//        }
//        return isFound;
//    }
//
//    public void removeAllKeyboardStateChangedListener() {
//        if (mKeyboardStateListeners == null) return;
//        for (KeyboardStateChangedListener elem : mKeyboardStateListeners) {
//            mKeyboardStateListeners.remove(elem);
//        }
//    }
//
//    private void notifyAllKeyboardStateChangedListeners(boolean isKeyboardVisible, int keyboardHeight) {
//        if (mKeyboardStateListeners == null) return;
//        for (KeyboardStateChangedListener elem : mKeyboardStateListeners) {
//            elem.onKeyboardStateChanged(isKeyboardVisible, keyboardHeight);
//        }
//    }
//
//    private void registerKeyboardGlobalLayoutListener() {
//        if (mKeyboardGlobalLayoutListener == null) {
//            mKeyboardGlobalLayoutListener = new KeyboardGlobalLayoutListener();
//            getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener
//                    (mKeyboardGlobalLayoutListener);
//        }
//    }
//
//    private void unregisterKeyboardGlobalLayoutListener() {
//        if (mKeyboardGlobalLayoutListener != null)
//            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener
//                    (mKeyboardGlobalLayoutListener);
//    }
//
//    public interface KeyboardStateChangedListener {
//        void onKeyboardStateChanged(boolean isKeyboardVisible, int keyboardHeight);
//    }
//
//    private class KeyboardGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
//
//        @Override
//        public void onGlobalLayout() {
//            // get visible are of current window
//            Rect r = new Rect();
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//
//            // get window height
////            int screenHeight = getWindow().getDecorView().getRootView().getRootView().getHeight();
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int screenHeight = dm.heightPixels;
//
//            int heightDifference = screenHeight - (r.bottom - r.top);
//            boolean isKeyboardVisible = heightDifference > (float) (screenHeight / 5);
//            Log.i("GLOBAL LAYOUT", "---> screen h:" + screenHeight + "; hDiff:" + heightDifference + "; " +
//                    isKeyboardVisible);
//            if (isKeyboardVisible != mKeyBoardVisibleState) {
//                mKeyBoardVisibleState = isKeyboardVisible;
//                notifyAllKeyboardStateChangedListeners(isKeyboardVisible, heightDifference);
//            }
//        }
//    }
}
