package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.CustomView.CircleProgressBar.CircleProgressBar;

public class HardwareAccelerationActivity extends Activity {
    private CircleProgressBar circleProgressBar;
    private Button btn_switch;
    private TextView txtvw_status;
    private Switch switch_hardware_acceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_acceleration);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams
//                .FLAG_HARDWARE_ACCELERATED);

        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progressbar);
        circleProgressBar.setPercentage(35);

        txtvw_status = (TextView) findViewById(R.id.txtvw_status);

        btn_switch = (Button) findViewById(R.id.btn_check);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtvw_status.setText(circleProgressBar.isHardwareAccelerated() ? "Yes" : "No");
            }
        });

        switch_hardware_acceleration = (Switch) findViewById(R.id.switch_hardware_acceleration);
        switch_hardware_acceleration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    circleProgressBar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    Log.i("Hardware Acceleration", "Hardware Acceleration---> Checked");
                } else {
                    circleProgressBar.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    Log.i("Hardware Acceleration", "Hardware Acceleration---> Unchecked");
                }
            }
        });
    }
}
