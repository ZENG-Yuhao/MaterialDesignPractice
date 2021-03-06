package com.esigelec.zengyuhao.materialdesignpractice.Others;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class AutoCompleteTextActivity extends Activity {
    private AutoCompleteTextView autotxt;
    private String[] str_list = new String[]{
            "bacon", "beef", "chicken", "cooked meat", "duck", "ham", "kidneys", "lamb", "liver",
            "mince", "paté", "salami", "sausages", "pork", "turkey", "veal", "butter", "cream",
            "cheese", "blue cheese", "goats cheese", "margarine", "milk", "yoghurt", "apple", "apricot", "blackberry"
    };


    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_text);
        autotxt = (AutoCompleteTextView) findViewById(R.id.autotxt);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout
                .simple_dropdown_item_1line, str_list);
        autotxt.setAdapter(arrayAdapter);
        autotxt.setThreshold(1);
        autotxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("AutoCompleteTextView", "--->onItemClick : " + "position-" + position + " id-" + id);
            }
        });
        autotxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("AutoCompleteTextView", "--->onItemSelected : " + "position-" + position + " id-" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("AutoCompleteTextView", "--->onNothingSelected");
            }
        });
        // Spinner
        mSpinner = (Spinner) findViewById(R.id.spinner1);
        mSpinner.setAdapter(arrayAdapter);
    }
}
