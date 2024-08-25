package com.example.networkstatechecker.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NetworkMonitorService extends Service {
    public static final String ACTION_NETWORK_STATUS = "NETWORK_STATUS";
    public static final String EXTRA_IS_CONNECTED = "IS_CONNECTED";
    public static final String EXTRA_IS_WIFI = "IS_WIFI";

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNetworkStatus();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkNetworkStatus();
        return START_NOT_STICKY;
    }

    private void checkNetworkStatus() {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean isWifi = activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        Intent broadcastIntent = new Intent(ACTION_NETWORK_STATUS);
        broadcastIntent.putExtra(EXTRA_IS_CONNECTED, isConnected);
        broadcastIntent.putExtra(EXTRA_IS_WIFI, isWifi);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(networkReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
