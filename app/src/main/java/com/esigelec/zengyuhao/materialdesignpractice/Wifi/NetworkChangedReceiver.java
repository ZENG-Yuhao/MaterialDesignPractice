package com.esigelec.zengyuhao.materialdesignpractice.Wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

/**
 * <p>
 * Created by ZENG Yuhao on 16/7/16. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
    private OnWifiStateChangedListener mWifiStateListener;
    private OnNetworkStateChangedListener mNetworkStateListener;
    private OnStateChangedAdapter mStateChangedAdapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()) && mWifiStateListener != null) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    mWifiStateListener.onEnabled();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    mWifiStateListener.onEnabling();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    mWifiStateListener.onDisabled();
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    mWifiStateListener.onDisabling();
                    break;
                //
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，
        // 和WIFI_STATE_DISABLED的时候，根本不会接到这个广播. 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会
        // 接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction()) && mNetworkStateListener != null) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();

                switch (state) {
                    case CONNECTING:
                        mNetworkStateListener.onConnecting();
                        break;
                    case CONNECTED:
                        mNetworkStateListener.onConnected();
                        break;
                    case DISCONNECTING:
                        mNetworkStateListener.onDisconnecting();
                        break;
                    case DISCONNECTED:
                        mNetworkStateListener.onDisconnected();
                        break;
                }
            }
        }

    }

    public void setOnWifiStateChangedListener(OnWifiStateChangedListener listener) {
        mWifiStateListener = listener;
    }

    public void setOnNetworkStateChangedListener(OnNetworkStateChangedListener listener) {
        mNetworkStateListener = listener;
    }

    public void setOnStateChangedAdapter(OnStateChangedAdapter adapter) {
        mStateChangedAdapter = adapter;
        setOnWifiStateChangedListener(mStateChangedAdapter);
        setOnNetworkStateChangedListener(mStateChangedAdapter);
    }

    public interface OnWifiStateChangedListener {
        void onEnabling();

        void onEnabled();

        void onDisabling();

        void onDisabled();
    }

    public interface OnNetworkStateChangedListener {
        void onConnecting();

        void onConnected();

        void onDisconnecting();

        void onDisconnected();
    }

    public static class OnStateChangedAdapter implements OnWifiStateChangedListener, OnNetworkStateChangedListener {

        @Override
        public void onConnecting() {
        }

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnecting() {
        }

        @Override
        public void onDisconnected() {
        }

        @Override
        public void onEnabling() {
        }

        @Override
        public void onEnabled() {
        }

        @Override
        public void onDisabling() {
        }

        @Override
        public void onDisabled() {
        }
    }
}
