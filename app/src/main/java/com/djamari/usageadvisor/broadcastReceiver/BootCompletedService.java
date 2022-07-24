package com.djamari.usageadvisor.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djamari.usageadvisor.service.ForegroundService;

import androidx.core.content.ContextCompat;

public class BootCompletedService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String input = "Mohon jangan blokir bilah notifikasi ini";
            Intent serviceIntent = new Intent(context, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", input);
            ContextCompat.startForegroundService(context, serviceIntent);
        }
    }
}
