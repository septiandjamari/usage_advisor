package com.djamari.usageadvisor.helper;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;

public class Helper {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static String TAG = Helper.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usageStatsManager = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        long startTime = calendar.getTimeInMillis() - 864000;
        Log.d(TAG, "Range Start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range End:" + dateFormat.format(endTime));

        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public static String getDuration(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Durasi harus lebih dari nol!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder stringBuilder = new StringBuilder(64);
        if (hours > 0) {
            stringBuilder.append(hours);
            stringBuilder.append(" Jam ");
        }
        if (minutes > 0) {
            stringBuilder.append(minutes);
            stringBuilder.append(" Menit ");
        }
        if (seconds < 59 && seconds != 0 && hours < 1) {
            stringBuilder.append(seconds);
            stringBuilder.append(" Detik");
        }
        return (stringBuilder.toString());
    }

    public static String getAppNameFromPkgName(Context context, String packagename) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(packagename, PackageManager.GET_META_DATA);
            return (String) packageManager.getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
            return "";

        }
    }
}
