package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.Core.Utils.RoundShapeInjector;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class ShapeTestActivity extends Activity {
    private Shape mShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_test);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

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

        TextView textView = (TextView) findViewById(R.id.text);
        RoundShapeInjector.inject(textView, RoundShapeInjector.getDefaultShapeDrawable(this, getResources().getColor
                (android.R.color.holo_purple)));
    }
}
