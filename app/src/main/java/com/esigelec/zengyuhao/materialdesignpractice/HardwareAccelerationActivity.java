package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.CustomizedViews.CircleProgressBar;

import org.w3c.dom.Text;

public class HardwareAccelerationActivity extends Activity {
    private CircleProgressBar circleProgressBar;
    private Button btn_switch;
    private TextView txtvw_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_acceleration);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams
//                .FLAG_HARDWARE_ACCELERATED);

        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progressbar);
        circleProgressBar.setPercentage(35);

        txtvw_status = (TextView) findViewById(R.id.txtvw_status);
        txtvw_status.setText(txtvw_status.isHardwareAccelerated() ? "Yes" : "No");

        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_switch.setText("Go to activity 2");
    }
}
