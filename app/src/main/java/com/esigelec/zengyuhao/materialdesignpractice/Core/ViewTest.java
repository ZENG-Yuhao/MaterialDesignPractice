package com.esigelec.zengyuhao.materialdesignpractice.Core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.GPSLocator.GPSLocatorHelper;

/**
 * <p>
 * Created by ZENG Yuhao on 06/07/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class ViewTest extends View {


    public ViewTest(Context context) {
        super(context);
    }

    public ViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("ViewTest", "onLayout---->");
    }
}
