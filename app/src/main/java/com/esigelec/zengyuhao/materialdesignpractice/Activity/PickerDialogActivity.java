package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class PickerDialogActivity extends Activity {
    private static final String[] SETUP_NAMES = {
            "HF", "PMR.1", "PMR.2", "TV.1", "FM_RNT.1", "FM_RNT.2", "PMR_BALISES.1", "PMR_BALISES.2", "TV.2", "TV.3",
            "LTE_700", "LTE_700.LR", "LTE_800", "LTE_800.LR", "GSM_R", "TM_900", "UMTS_900", "TM_1800", "LTE_1800",
            "LTE_1800.LR", "UMTS_2100", "LTE_2600", "LTE_2600.LR", "DECT", "PMR_BALISES.3", "PMR_BALISES.4",
            "RADAR_BALISES.1", "WIFI.1", "WIFI.2", "WIFI.3", "RADAR_BLR.1", "RADAR_BLR.2", "RADAR_BLR.3", "RADAR_BLR.4",
            "RADAR_BLR.5", "RADAR_BLR.6", "RADAR_BLR.7"
    };
    private static final String[] HEIGHT_NAMES = {"1m10", "1m50", "1m70"};

    private NumberPicker picker_height, picker_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_dialog);

        picker_height = (NumberPicker) findViewById(R.id.picker_height);
        picker_height.setMinValue(0);
        picker_height.setMaxValue(HEIGHT_NAMES.length - 1);
        picker_height.setDisplayedValues(HEIGHT_NAMES);
        picker_height.setWrapSelectorWheel(true);

        String[] setupNames = new String[SETUP_NAMES.length];
        int i = 0;
        for (String str : SETUP_NAMES) {
            if (i < 10)
                setupNames[i] = str + "   (" + (i + 1) + ") ";
            else
                setupNames[i] = str + "   (" + (i + 1) + ") ";
            i++;
        }
        picker_setup = (NumberPicker) findViewById(R.id.picker_setup);
        picker_setup.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        picker_setup.setMinValue(0);
        picker_setup.setMaxValue(SETUP_NAMES.length - 1);
        picker_setup.setDisplayedValues(setupNames);
        picker_setup.setWrapSelectorWheel(true);
    }

}
