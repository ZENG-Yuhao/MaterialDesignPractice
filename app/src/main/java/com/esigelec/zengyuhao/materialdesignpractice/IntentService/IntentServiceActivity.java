package com.esigelec.zengyuhao.materialdesignpractice.IntentService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * Steps:
 * <p>
 * 1.Create IntentService and implement onHandleIntent() method.
 * 2.Define actions and status (like those in Constants class)
 * 3.Instantiate new intent with the action defined and put the status difined as an extras.
 * 4.Call LocalBroadcastManager.getInstance(context).sendBroadcast(intent) in onHandleIntent() when task is done.
 * 5.Create Receiver Class which extends BroadcastReceiver and implement onReceive() method to handle incoming data
 * and manipulate UI in Activity/Fragment.
 * 6.In Activity/Fragment, create IntentFilter and register it in LocalBroadcastManager.
 * <p>
 * <a href="https://developer.android.com/training/run-background-service/create-service.html">Tutorial Reference</a>
 */
public class IntentServiceActivity extends Activity {

    private TextView textView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        Button btn_start_service = (Button) findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntentServiceActivity.this, MyIntentService.class);
                intent.putExtra("secret", "There is no secret.");
                startService(intent);
            }
        });

        textView = (TextView) findViewById(R.id.text_view);

        // Sets filter and receiver
        IntentFilter statusIntentFilter = new IntentFilter(MyIntentService.Constants.BROADCAST_ACTION);
        /*
          Add a new Intent data scheme to match against. If any schemes are included in the filter, then an Intent's
          data must be either one of these schemes or a matching data type. If no schemes are included, then an
          Intent will match only if it includes no data.
         */
        //statusIntentFilter.addDataScheme("http");

        ResponseReceiver receiver = new ResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, statusIntentFilter);
    }

    private class ResponseReceiver extends BroadcastReceiver {

        // Prevents instantiation
        private ResponseReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText(intent.getStringExtra(MyIntentService.Constants.EXTENDED_DATA_STATUS) + ":" + count);
            count++;
            Log.i("Receiver", "onReceive()---->message got.");
        }
    }

}
