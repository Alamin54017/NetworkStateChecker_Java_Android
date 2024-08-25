package com.example.networkstatechecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.networkstatechecker.Service.NetworkMonitorService;

public class BaseActivity extends AppCompatActivity {
    private Dialog dialog;
    private final BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(NetworkMonitorService.ACTION_NETWORK_STATUS.equals(intent.getAction())) {
                boolean isConnected = intent.getBooleanExtra(NetworkMonitorService.EXTRA_IS_CONNECTED, false);
                boolean isWifi = intent.getBooleanExtra(NetworkMonitorService.EXTRA_IS_WIFI, false);
                showNetworkStatus(isConnected, isWifi);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        LocalBroadcastManager.getInstance(this).registerReceiver(networkStatusReceiver,
                new IntentFilter(NetworkMonitorService.ACTION_NETWORK_STATUS));

        Intent serviceIntent = new Intent(this, NetworkMonitorService.class);
        startService(serviceIntent);
    }


    private void showNetworkStatus(boolean isConnected, boolean isWifi) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog=new Dialog(this);
        if (isConnected){
            if (isWifi){
                //dialog.setContentView(R.layout.alertdesign);
            }else{
                //dialog.setContentView(R.layout.alertdesign);
            }
        }
        else {
            dialog.setContentView(R.layout.alertdesign);
        }
        Window window=dialog.getWindow();
        if (window!=null){
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params=window.getAttributes();
            params.width=WindowManager.LayoutParams.MATCH_PARENT;
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}