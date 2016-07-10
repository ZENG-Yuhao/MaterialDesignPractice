package com.esigelec.zengyuhao.materialdesignpractice.Core.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.Size;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * <p>
 * Created by ZENG Yuhao on 16/7/10. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class RoundShapeInjector {

    public static void inject(final View target, final ShapeDrawable drawable) {
        target.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = target.getWidth();
                int height = target.getHeight();
                inject(target, drawable, Math.min(width, height) / 2);
                target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static void inject(View target, ShapeDrawable drawable, float radius) {
        float[] outerRadii = new float[8];
        for (int i = 0; i < 8; i++) outerRadii[i] = radius;
        inject(target, drawable, outerRadii, null, null);
    }

    public static void inject(View target, ShapeDrawable drawable, @Size(8) float[] outerRadii) {
        inject(target, drawable, outerRadii, null, null);
    }

    public static void inject(View target, ShapeDrawable drawable, @Size(8) float[] outerRadii, RectF inset, @Size(8)
    float[] innerRadii) {
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, inset, innerRadii);
        drawable.setShape(roundRectShape);
        target.setBackground(drawable);
    }

    public static ShapeDrawable getDefaultShapeDrawable(Context context, @ColorInt int color) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(color);
        //drawable.getPaint().setAntiAlias(true);
        drawable.getPaint().setStyle(Paint.Style.FILL);

        int[][] states = new int[][]{new int[]{android.R.attr.state_activated}, new int[]{-android.R.attr
                .state_activated}};
        int[] colors = new int[]{Color.parseColor("#FFFF00"), Color.BLACK};
        ColorStateList myList = new ColorStateList(states, colors);
        RippleDrawable rippleDrawable = new RippleDrawable(myList, drawable, null);

        return drawable;
    }
}
