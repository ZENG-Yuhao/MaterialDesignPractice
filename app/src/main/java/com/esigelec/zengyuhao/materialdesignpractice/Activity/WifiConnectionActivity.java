package com.esigelec.zengyuhao.materialdesignpractice.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;
import com.esigelec.zengyuhao.materialdesignpractice.Receiver.NetworkChangedReceiver;

public class WifiConnectionActivity extends Activity {
    private NetworkChangedReceiver networkStateReceiver;
    private IntentFilter intentFilter;
    private TextView txt_wifi_state;
    private Button btn_open_wifi, btn_close_wifi, btn_check_wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connection);

        networkStateReceiver = new NetworkChangedReceiver();
        networkStateReceiver.setOnStateChangedAdapter(new NetworkChangedReceiver.OnStateChangedAdapter() {
            @Override
            public void onConnecting() {
                Log.i("WifiConnectionActivity", "--->network onConnecting");
            }

            @Override
            public void onConnected() {
                Log.i("WifiConnectionActivity", "--->network onConnected");
            }

            @Override
            public void onDisconnecting() {
                Log.i("WifiConnectionActivity", "--->network onDisconnecting");
            }

            @Override
            public void onDisconnected() {
                Log.i("WifiConnectionActivity", "--->network onDisconnected");
            }

            @Override
            public void onEnabling() {
                Log.i("WifiConnectionActivity", "--->wifi onEnabling");
            }

            @Override
            public void onEnabled() {
                Log.i("WifiConnectionActivity", "--->wifi onEnabled");
            }

            @Override
            public void onDisabling() {
                Log.i("WifiConnectionActivity", "--->wifi onDisabling");
            }

            @Override
            public void onDisabled() {
                Log.i("WifiConnectionActivity", "--->wifi onDisabled");
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        txt_wifi_state = (TextView) findViewById(R.id.txtvw_wifi_state);

        btn_open_wifi = (Button) findViewById(R.id.btn_activate_wifi);
        btn_open_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWifi(WifiConnectionActivity.this, true);
            }
        });

        btn_close_wifi = (Button) findViewById(R.id.btn_deactivate_wifi);
        btn_close_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWifi(WifiConnectionActivity.this, false);
            }
        });

        btn_check_wifi = (Button) findViewById(R.id.btn_check_wifi);
        btn_check_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_wifi_state.setText("WIFI STATE: " + checkWifi(WifiConnectionActivity.this));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, intentFilter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateReceiver);
    }

    private void toggleWifi(Context context, boolean enabled) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(enabled);
    }

    private boolean checkWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        manager.addDefaultNetworkActiveListener(new ConnectivityManager.OnNetworkActiveListener() {
            @Override
            public void onNetworkActive() {
                Log.i("network", "active");
            }
        });
        return wifiInfo.isConnected();
    }
}
