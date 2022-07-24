package com.djamari.usageadvisor.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.Objects;

public class AppStarter extends Application {
    public static final String CHANNEL_ID = "notificationServiceChannel";
    public static final String CHANNEL_ID_1 = "notificationUsage";
    public static final String CHANNEL_ID_2 = "notificationAgenda";

    @Override
    public void onCreate() {
        super.onCreate();
//        TypeFaceUtil.setDefaultFount(this, "DEFAULT");
//        TypeFaceUtil.setDefaultFount(this, "MONOSPACE");
//        TypeFaceUtil.setDefaultFount(this, "SERIF");
//        TypeFaceUtil.setDefaultFount(this, "SAN_SERIF");

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel1 = new NotificationChannel(
                    CHANNEL_ID,
                    "Service Berjalan - Info Penggunaan Layar",
                    NotificationManager.IMPORTANCE_HIGH
            );
            serviceChannel1.setDescription("serviceChannel1");

            NotificationChannel notification1 = new NotificationChannel(
                    CHANNEL_ID_1,
                    "Pengingat Penggunaan",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notification1.setDescription("notification1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel1);
            manager.createNotificationChannel(notification1);
        }
    }
}
