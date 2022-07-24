package com.djamari.usageadvisor.broadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {
    public static final String TAG = "ScreenTimeServiceRestarter";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Service di mulai ulang !");
        context.startService(new Intent(context, OverallScreenService.class));
    }
}