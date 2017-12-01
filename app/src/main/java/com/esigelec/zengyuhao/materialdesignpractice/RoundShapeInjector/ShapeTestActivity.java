package com.esigelec.zengyuhao.materialdesignpractice.RoundShapeInjector;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ShapeTestActivity extends Activity {
    private Shape mShape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_test);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        int white_color = getResources().getColor(android.R.color.white);
        final int purple_color = getResources().getColor(android.R.color.holo_purple);
        final int orange_color = getResources().getColor(android.R.color.holo_orange_light);
        Log.i("ShapeTestActivity", "-->" + imageView.getPaddingTop() + " " + imageView.getPaddingRight());

        float[] outerRadii = {20, 40, 40, 40, 60, 60, 100, 100};

        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);

        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(getResources().getColor(android.R.color.holo_purple));
        drawable.getPaint().setAntiAlias(true);
        drawable.getPaint().setStyle(Paint.Style.FILL);
        //imageView.setBackground(drawable);
        ShapeDrawable drawable_new = RoundShapeInjector.getDefaultShapeDrawable(this, getResources().getColor(android
                .R.color.holo_purple));
        RoundShapeInjector.inject(imageView, drawable_new);
        imageView.setElevation(16);
        imageView.setClickable(true);

        Button textView = (Button) findViewById(R.id.text);
        RoundShapeInjector.injectRippleEffect(this, textView);

        final RippleDrawable rippleDrawable = (RippleDrawable) getResources().getDrawable(R.drawable.ripple_rect);

        final Button button1 = (Button) findViewById(R.id.text1);
        button1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = button1.getWidth();
                int height = button1.getHeight();
                int orange_color = getApplicationContext().getResources().getColor(android.R.color.holo_orange_light);

                // RoundRectShape
                float[] outerRadii = new float[8];
                for (int i = 0; i < 8; i++) outerRadii[i] = Math.min(width, height) / 2;
                RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);

                // ShapeDrawable
                ShapeDrawable shapeDrawable = new ShapeDrawable();
                shapeDrawable.getPaint().setColor(orange_color);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                shapeDrawable.setShape(roundRectShape);

                button1.setBackground(rippleDrawable);
                button1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        Button button2 = (Button) findViewById(R.id.text2);
        //button2.setClickable(true);
        RoundShapeInjector.injectStateListDrawable(this, button2);
        button2.setElevation(16);

        final TextView gradient = (TextView) findViewById(R.id.text3);
        gradient.setClickable(true);
        gradient.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setStroke(10, purple_color);
                gradientDrawable.setColor(orange_color);
                gradientDrawable.setCornerRadius(Math.min(gradient.getWidth(), gradient.getHeight()) / 2);
                gradient.setBackground(gradientDrawable);
            }
        });
        gradient.setElevation(16);
    }
}
