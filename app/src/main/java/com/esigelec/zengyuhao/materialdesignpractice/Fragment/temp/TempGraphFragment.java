package com.esigelec.zengyuhao.materialdesignpractice.Fragment.temp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esigelec.zengyuhao.materialdesignpractice.Fragment.BaseLazyFragment;
import com.esigelec.zengyuhao.materialdesignpractice.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TempGraphFragment extends BaseLazyFragment {
    private LineChart mLineChart;
    private LimitLine limitLineNF, limitLineD;
    private float limitNF, limitD, tempNF, tempD;
    private boolean state_btn_switch = false;

    public TempGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateLoadingView(@Nullable ViewGroup parent) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_lazy_fragment_loading_view, parent, false);
    }

    @Override
    public View onCreateLazyView(@Nullable ViewGroup parent) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_temp_graph, parent, false);

        return view;
    }

    @Override
    public void onLazyLoad() {
        notifyDataLoaded();
    }

    @Override
    public void onBindData(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.chart);
        mLineChart.setBackgroundColor(Color.parseColor("#252525"));
        mLineChart.setDrawBorders(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setScaleYEnabled(false);
        mLineChart.setEnabled(false);
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
        float[] rawData = generateRawData(20000, 0.1f, 100000f);
        LineData lineData = generateLineData(rawData);
        mLineChart.setData(lineData);
        //mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_temp_graph, container, false);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
}
