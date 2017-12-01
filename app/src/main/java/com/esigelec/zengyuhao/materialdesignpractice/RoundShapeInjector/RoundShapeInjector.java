package com.esigelec.zengyuhao.materialdesignpractice.RoundShapeInjector;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.Size;
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
        return drawable;
    }

    public static void injectRippleEffect(final Context context, final View target) {
        target.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = target.getWidth();
                int height = target.getHeight();
                int white_color = context.getResources().getColor(android.R.color.white);
                int purple_color = context.getResources().getColor(android.R.color.holo_purple);
                int orange_color = context.getResources().getColor(android.R.color.holo_orange_light);
                ColorDrawable colorDrawable = new ColorDrawable(purple_color);

                // RoundRectShape
                float[] outerRadii = new float[8];
                for (int i = 0; i < 8; i++) outerRadii[i] = Math.min(width, height) / 2;
                RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);

                // ShapeDrawable
                ShapeDrawable shapeDrawable = new ShapeDrawable();
                shapeDrawable.getPaint().setColor(orange_color);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                shapeDrawable.setShape(roundRectShape);


                // RippleDrawable
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
//                                new int[]{android.R.attr.state_pressed},
//                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                        new int[]{
//                                context.getResources().getColor(android.R.color.holo_orange_dark),
//                                context.getResources().getColor(android.R.color.holo_purple),
                                context.getResources().getColor(android.R.color.holo_orange_dark)
                        });
                // first setup
                // shapeDrawable as content, mask layer is null, so the shape of shapeDrawable is used as mask,
                // ripple effects in the boundary of mask/shapeDrawable.
                RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, shapeDrawable, null);

                // second setup
                // shapeDrawable as content, colorDrawable as mask, ripple effects in the boundary of
                // mask/colorDrawable, but color of colorDrawable will not be draw on screen.
                RippleDrawable rippleDrawable2 = new RippleDrawable(colorStateList, shapeDrawable, colorDrawable);


                // third setup
                // no content, no mask, ripple effects as far as it can in his parent view.
                RippleDrawable rippleDrawable3 = new RippleDrawable(colorStateList, null, null);


                target.setClickable(true);
                target.setBackground(rippleDrawable);

                target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static RippleDrawable getRippleDrawable(Context context) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_activated, android.R.attr.state_enabled, android.R.attr
                                .state_focused},
                        new int[]{}
                },
                new int[]{
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_orange_light,
                        android.R.color.darker_gray
                });
        int color = context.getResources().getColor(android.R.color.holo_purple);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        ShapeDrawable shapeDrawable = getDefaultShapeDrawable(context, color);
        RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, shapeDrawable,
                shapeDrawable);


        return rippleDrawable;
    }


    public static RippleDrawable getRippleDrawableFromXml(Context context) {
        return null;
    }

    public static void injectStateListDrawable(final Context context, final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int white_color = context.getResources().getColor(android.R.color.white);
                int purple_color = context.getResources().getColor(android.R.color.holo_purple);
                int orange_color = context.getResources().getColor(android.R.color.holo_orange_light);
                int radius = Math.min(view.getWidth(), view.getHeight()) / 2;
                RoundShapeDrawable pressedDrawable = new RoundShapeDrawable(radius);
                pressedDrawable.getPaint().setStyle(Paint.Style.FILL);
                pressedDrawable.getPaint().setColor(orange_color);

                RoundShapeDrawable normalDrawable = new RoundShapeDrawable(radius);
                normalDrawable.getPaint().setStyle(Paint.Style.FILL);
                normalDrawable.getPaint().setColor(purple_color);

                // stroke width
                int strW = 8;
                ShapeDrawable shapeDrawable = new ShapeDrawable();
//                RectF inset = new RectF(view.getLeft() + strW, view.getTop() + strW, view.getRight() - strW, view
//                        .getBottom() - strW);
                RectF inset = new RectF(strW, strW, strW, strW);
                float[] outerRadii = new float[8];
                float[] innerRadii = new float[8];
                for (int i = 0; i < 8; i++) {
                    outerRadii[i] = radius;
                    innerRadii[i] = radius - strW;
                }
                RoundRectShape shape = new RoundRectShape(outerRadii, inset, innerRadii);
                shapeDrawable.setShape(shape);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                shapeDrawable.getPaint().setAntiAlias(true);
                shapeDrawable.getPaint().setColor(orange_color);

                // GradientDrawable
                GradientDrawable gradientNormal = new GradientDrawable();
                gradientNormal.setShape(GradientDrawable.RECTANGLE);
                gradientNormal.setStroke(10, orange_color);
                gradientNormal.setColor(white_color);
                gradientNormal.setCornerRadius(radius);

                GradientDrawable gradientPressed = new GradientDrawable();
                gradientPressed.setShape(GradientDrawable.RECTANGLE);
                gradientPressed.setColor(orange_color);
                gradientPressed.setCornerRadius(radius);

                StateListDrawable stateListDrawable = new StateListDrawable();
                // when we add ShapeDrawable into a StateListDrawable, there will be no shadow effect with an unknown
                // reason, if we use GradientDrawable instead, shadow effect is back.
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientPressed);
                stateListDrawable.addState(new int[]{}, gradientNormal);
                view.setBackground(stateListDrawable);
                //view.setBackground(pressedDrawable);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public static class RoundGradientDrawable extends GradientDrawable {

    }


    public static class RoundShapeDrawable extends ShapeDrawable {
        public RoundShapeDrawable(float radius) {
            float[] outerRadii = new float[8];
            for (int i = 0; i < 8; i++) outerRadii[i] = radius;
            RoundRectShape shape = new RoundRectShape(outerRadii, null, null);
            setShape(shape);
        }
    }


}
