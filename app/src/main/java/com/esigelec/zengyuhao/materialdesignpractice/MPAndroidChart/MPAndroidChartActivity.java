package com.esigelec.zengyuhao.materialdesignpractice.MPAndroidChart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.esigelec.zengyuhao.materialdesignpractice.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

public class MPAndroidChartActivity extends Activity {
    private LineChart mLineChart;
    private Button btn_run, btn_switch;
    private LimitLine limitLineNF, limitLineD;
    private float limitNF, limitD, tempNF, tempD;
    private boolean state_btn_switch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpandroid_chart);

        mLineChart = (LineChart) findViewById(R.id.chart);
        mLineChart.setBackgroundColor(Color.BLACK);
        mLineChart.setDrawBorders(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setScaleYEnabled(false);
        mLineChart.getLegend().setEnabled(false);

        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setGranularity(1f);
        /* Can be used for formatting large values > "1.000". It will turn values like "1.000" into "1k", "1.000.000"
         will be "1m" (million), "1.000.000.000" will be "1b" (billion) and values like one trillion will be e.g.
         "1t". */
        mLineChart.getXAxis().setValueFormatter(new LargeValueFormatter());
        mLineChart.getXAxis().setTextColor(Color.WHITE);
        //mLineChart.getXAxis().setLabelCount(4);

        mLineChart.getAxisLeft().setAxisMaxValue(200000);
        mLineChart.getAxisLeft().setAxisMinValue(-200000);
        mLineChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        mLineChart.getAxisLeft().setTextColor(Color.WHITE);

        mLineChart.setDescription("");
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setDragDecelerationEnabled(false);

        limitNF = 140000f;
        limitD = -100000f;
        limitLineNF = new LimitLine(limitNF, "Noise Floor");
        limitLineNF.setLineColor(Color.GREEN);
        limitLineNF.setTextSize(12f);
        limitLineNF.setTextColor(Color.WHITE);
        mLineChart.getAxisLeft().addLimitLine(limitLineNF);

        limitLineD = new LimitLine(limitD, "Delta");
        limitLineD.setLineColor(Color.YELLOW);
        limitLineD.setTextSize(12f);
        limitLineD.setTextColor(Color.WHITE);
        mLineChart.getAxisLeft().addLimitLine(limitLineD);


        btn_run = (Button) findViewById(R.id.btn_run);
        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRunButtonClick();
            }
        });

        final TouchListener mListener = new TouchListener();

        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state_btn_switch = !state_btn_switch;
                if (state_btn_switch) {
                    btn_switch.setText("NF D");
                    mLineChart.setDragEnabled(false);
                    mLineChart.setScaleXEnabled(false);
                } else {
                    btn_switch.setText("ZOOM SCROLL");
                    mLineChart.setDragEnabled(true);
                    mLineChart.setScaleXEnabled(true);
                }
            }
        });

        mLineChart.setOnTouchListener(mListener);
    }

    private void onRunButtonClick() {
        float[] rawData = generateRawData(2000, 0.1f, 100000f);
        LineData lineData = generateLineData(rawData);
        mLineChart.setData(lineData);
        //mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

    private LineData generateLineData(float[] rawData) {
        ArrayList<Entry> entries = new ArrayList<>(rawData.length);
        int i = 0;
        for (float data : rawData) {
            entries.add(new Entry(i, data));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "data set 1");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleRadius(2);
        dataSet.setLineWidth(1.75f);
        dataSet.setHighLightColor(Color.RED);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        LineData lineData = new LineData(dataSet);
        return lineData;
    }

    private float[] generateRawData(int length, float minValue, float maxValue) {
        float[] rawData = new float[length];
        for (int i = 0; i < length; i++) {
            rawData[i] = (float) (Math.sin(i * 0.01) * (maxValue - minValue) + minValue);
        }
        addNoise(rawData, -maxValue * 0.5f, maxValue * 0.5f);
        return rawData;
    }

    private void addNoise(float[] data, float minNoise, float maxNoise) {
        int length = data.length;
        for (int i = 0; i < length; i++) {
            data[i] += getRandomFloat(minNoise, maxNoise);
        }
    }

    private float getRandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    private class TouchListener implements View.OnTouchListener {
        private float lastY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!state_btn_switch) return false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dY = event.getY() - lastY;

                    float x = event.getX();
                    if (x < mLineChart.getWidth() / 2) {
                        Log.i("LineChart", "----> touch left " + dY);
                        limitNF -= dY * 30;
                        mLineChart.getAxisLeft().removeLimitLine(limitLineNF);
                        limitLineNF = new LimitLine(limitNF, "Noise Floor");
                        limitLineNF.setLineColor(Color.GREEN);
                        limitLineNF.setTextSize(12f);
                        limitLineNF.setTextColor(Color.WHITE);
                        mLineChart.getAxisLeft().addLimitLine(limitLineNF);
                        mLineChart.invalidate();
                    } else {
                        Log.i("LineChart", "----> touch right " + dY);
                        limitD -= dY * 30;
                        mLineChart.getAxisLeft().removeLimitLine(limitLineD);
                        limitLineD = new LimitLine(limitD, "Delta");
                        limitLineD.setLineColor(Color.YELLOW);
                        limitLineD.setTextSize(12f);
                        limitLineD.setTextColor(Color.WHITE);
                        mLineChart.getAxisLeft().addLimitLine(limitLineD);
                        mLineChart.invalidate();
                    }

                    lastY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    }

}
