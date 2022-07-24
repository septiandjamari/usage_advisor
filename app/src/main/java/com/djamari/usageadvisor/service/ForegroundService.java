package com.djamari.usageadvisor.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.djamari.usageadvisor.MainActivity;
import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.broadcastReceiver.OverallScreenService;
import com.djamari.usageadvisor.broadcastReceiver.ServiceStarter;
import com.djamari.usageadvisor.helper.Helper;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import static com.djamari.usageadvisor.app.AppStarter.CHANNEL_ID;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;


public class ForegroundService extends Service {
    public static final String TAG = "ForegroundService";
    private OverallScreenService overallScreenService = new OverallScreenService();


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SHUTDOWN);
        intentFilter.addAction(Intent.ACTION_REBOOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentFilter.addAction(Intent.ACTION_USER_UNLOCKED);
        }
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(overallScreenService, intentFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final SharedPreferences sp = getSharedPreferences(VALUEID, 0);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long layarHidup = sp.getLong("layarHidup", 0), unlockScreen = sp.getLong("unlockScreen", 0);
                String input = "<b>\u2022 Penggunaan Layar : </b>" + (Helper.getDuration(layarHidup * 1000)) + "<br><b>\u2022 Kunci Layar Terbuka : </b>" + (unlockScreen) + " Kali";
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle("Service Berjalan - Info Penggunaan Layar")
                        .setOnlyAlertOnce(true)
                        .setContentText(Html.fromHtml(input))
                        .setSmallIcon(R.drawable.ic_schedule_black_24dp)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .setContentIntent(pendingIntent)
                        .setShowWhen(false)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .build();
                startForeground(1, notification);
                handler.postDelayed(this, 20000);
            }
        });
        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        final int basedTimeLock = sp.getInt(KunciLayar, 0);
        final int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
        final int oneClickLock = sp.getInt(OneClickLock, 0);
        if (basedTimeLock == 1 || basedScheduleLock == 1 || oneClickLock == 1) {
            Toast.makeText(getApplicationContext(), "Layar akan segera di kunci !", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Objects.requireNonNull(devicePolicyManager).lockNow();
                }
            }, 3000);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
//        final SharedPreferences.Editor editor = sp.edit();

        Log.i(TAG, "Service Destroyed");
        Intent broadcastIntent = new Intent(this, ServiceStarter.class);
        sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
