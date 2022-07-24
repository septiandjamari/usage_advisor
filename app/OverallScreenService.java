package com.djamari.usageadvisor.broadcastReceiver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.djamari.usageadvisor.database.PersonalUsageDatabase.AppUsageCollectionModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.DaftarAplikasiModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase;
import com.djamari.usageadvisor.firebaseModel.AppUsageCollectionModelFirebase;
import com.djamari.usageadvisor.firebaseModel.DaftarAplikasiModelFirebase;
import com.djamari.usageadvisor.helper.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class OverallScreenService extends BroadcastReceiver {
    public static final String VALUEID = "val Id";
    private final String TAG = "sreenUsageService";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat clockFormat = new SimpleDateFormat("H:mm:ss");

    private String dateNow, dateValue;
    private Calendar calendar = Calendar.getInstance();
    private long seconds = 0;
    private boolean startCount;
    private static boolean powerOff;
    private static boolean getCurrentApp;
    private String currentApp = "NULL", compareApp;
    private DaftarAplikasiModel daftarAplikasiInput;
    private AppUsageCollectionModel appUsageCollectionInput;
    private AppUsageCollectionModelFirebase appUsageCollectionModelFirebase;
    private DaftarAplikasiModelFirebase daftarAplikasiModelFirebase;
    private FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public void onReceive(Context context, final Intent intent) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();


        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) || Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            powerOff = false;

        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            getCurrentApp = false;
            startCount = false;
            powerOff = false;
            basedTimeLockFinish(context.getApplicationContext());
            basedSchedulePhoneFinish(context.getApplicationContext());

        } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
            Runtime.getRuntime().addShutdownHook(new Thread());
            Log.d(TAG, "Interupsi Selama 4 detik sebelum mematikan smartphone");
            powerOff = true;
            getCurrentApp = false;
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Intent.ACTION_REBOOT.equals(intent.getAction())) {
            Runtime.getRuntime().addShutdownHook(new Thread());
            Log.d(TAG, "Interupsi Selama 4 detik sebelum memulai ulang smartphone");
            powerOff = true;
            getCurrentApp = false;
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Intent.ACTION_USER_UNLOCKED.equals(intent.getAction()) || Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            getCurrentApp = true;
            long savedunlockScreen = sp.getLong("unlockScreen", 0);
            Log.d(TAG, "Hitung kunci terbuka : " + savedunlockScreen);
            Log.e(TAG, "context service: " + context.toString());
            //    private long timerKunciLayar;
            //    private long timerLayarHidup;
            //    private int pernahLock;
            //    private int kunciLayar;
            long unlockScreen = savedunlockScreen;
            unlockScreen++;
            Log.d(TAG, "Kunci terbuka " + unlockScreen + " kali");
            editor.putLong("unlockScreen", unlockScreen);
            editor.apply();
            if (user != null) {
                String userId = user.getUid();
                printForegroundTask(context.getApplicationContext(), userId);
                basedTimeLockStart(context.getApplicationContext());
                basedScheduleLockPhoneStart(context.getApplicationContext());
            }
            startCount = true;
            Timer(context.getApplicationContext());
        }
    }

    private void Timer(final Context context) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final PersonalUsageDatabase db = new PersonalUsageDatabase(context.getApplicationContext());
        final long waktuPembanding = sp.getLong("layarHidup", 0);

        Log.d(TAG, "Timer: waktu pembanding : " + Helper.getDuration(waktuPembanding * 1000));
        Log.d(TAG, "keydate : " + dateValue + " tanggal pembanding : " + dateFormat.format(calendar.getTimeInMillis()));

        if (seconds < waktuPembanding) {
            seconds += waktuPembanding;
        }

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                dateValue = sp.getString("keyDate", "");
                if (startCount) {
                    long layarHidup = sp.getLong("layarHidup", 0);
                    Log.d(TAG, "run: " + layarHidup + " - " + seconds + " dateValue "
                            + dateValue + " tanggal pembanding " + dateFormat.format(System.currentTimeMillis()));
                    if (!dateValue.equals(dateFormat.format(System.currentTimeMillis()))) {
                        seconds = 0;
                        dateNow = dateFormat.format(System.currentTimeMillis());
                        editor.putLong("layarHidup", 0);
                        //db deleteRecord
                        db.deleteAllDaftarAplikasiRecord();
                        db.deleteAllAppCollectionRecord();
                        editor.putString("keyDate", dateNow);
                        editor.putLong("unlockScreen", 1);
                        editor.apply();
                        //db
                        Log.d(TAG, "timer di reset ke 0");
                        Log.d(TAG, "keydate diset ke - " + dateNow);

                    } else if (seconds != 0 && seconds % 60 == 0) {
                        Log.d(TAG, "waktu bertambah 60 detik");
                        editor.putLong("layarHidup", seconds);
                        editor.apply();
                        Log.d(TAG, "Waktu disimpan di sharedpreference");
                    }
                    seconds++;
                    handler.postDelayed(this, 1000);
                } else if (!startCount || powerOff) {
                    Log.d(TAG, "Stop Counting Time");
                    Log.d(TAG, "Stop Updating Time ");
                    editor.putLong("layarHidup", seconds);
                    editor.apply();
                    long layarHidup = sp.getLong("layarHidup", 0);

                    Log.d(TAG, "saved : " + Helper.getDuration(layarHidup * 1000) + " Time Running " + Helper.getDuration(seconds * 1000));
                    Log.d(TAG, "Time saved into SharedPreference");
                }
            }
        });
    }

    private void printForegroundTask(final Context context, final String userId) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final PersonalUsageDatabase db = new PersonalUsageDatabase(context.getApplicationContext());
//        final AppUsageCollection db1 = new AppUsageCollection(context.getApplicationContext());
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void run() {
                compareApp = sp.getString("compareApp", "");
                long firstActive = sp.getLong("firstActive", 0), appDuration;
                String keydate = sp.getString("keyDate", "");

                if (getCurrentApp) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        context.getApplicationContext();
                        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                        long time = System.currentTimeMillis();
                        List<UsageStats> appList = Objects.requireNonNull(usm).queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                        if (appList != null && appList.size() > 0) {
                            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                            for (UsageStats usageStats : appList) {
                                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                            }
                            if (!mySortedMap.isEmpty()) {
                                currentApp = Objects.requireNonNull(mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                            }
                        }
                    } else {
                        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningAppProcessInfo> tasks = Objects.requireNonNull(am).getRunningAppProcesses();
                        currentApp = tasks.get(0).processName;
                    }
                    if (!compareApp.equals(currentApp)) {
                        if (compareApp.equals("NULL") || compareApp.trim().equals("")) {
                            Log.e(TAG, "Layar kunci terbuka");
                            Log.e(TAG, "Current App in foreground is: '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "'");

                            editor.putString("compareApp", currentApp);
                            editor.putLong("firstActive", System.currentTimeMillis());
                            editor.apply();
                        } else {
                            Log.e(TAG, "Current App in foreground is: '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "'");
                            appDuration = System.currentTimeMillis() - firstActive;

                            //Simpan terlebih dahulu nama_aplikasi dengan "compareApp"
                            Log.e(TAG, "Durasi pakai Aplikasi '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), compareApp) + "':" + Helper.getDuration(appDuration));
                            //kelas objek untuk database lokal
                            daftarAplikasiInput = new DaftarAplikasiModel(-1, compareApp, appDuration, -1, keydate, clockFormat.format(System.currentTimeMillis()));
                            appUsageCollectionInput = new AppUsageCollectionModel(-1, compareApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), keydate, Helper.getDuration(appDuration));
                            //kelas objek untuk firestore
                            daftarAplikasiModelFirebase = new DaftarAplikasiModelFirebase(clockFormat.format(System.currentTimeMillis()), appDuration, 1);
                            appUsageCollectionModelFirebase = new AppUsageCollectionModelFirebase(compareApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), Helper.getDuration(appDuration));

                            // DaftarAplikasi
                            db.checkRecordIfExist(daftarAplikasiInput);
                            firestoreDaftarAplikasi(clockFormat.format(System.currentTimeMillis()), appDuration, userId, dateFormat.format(System.currentTimeMillis()), compareApp, daftarAplikasiModelFirebase);
                            // AppCollection
                            db.addRecordAppCollection(appUsageCollectionInput);
                            firestoreAppCollection(userId, dateFormat.format(System.currentTimeMillis()), clockFormat.format(System.currentTimeMillis()), appUsageCollectionModelFirebase);
                            editor.putLong("statusPerubahanInteraksi", System.currentTimeMillis());
                            Log.d(TAG, "run: informasi penggunaan di simpan di database");
                            editor.putLong("statusPerubahanInteraksi", System.currentTimeMillis());
                            editor.putString("compareApp", currentApp);
                            editor.putLong("firstActive", System.currentTimeMillis());
                            editor.apply();
                        }
                    }
                    handler.postDelayed(this, 1000);
                } else if (!getCurrentApp) {
                    appDuration = System.currentTimeMillis() - firstActive;
                    Log.e(TAG, "Durasi pakai Aplikasi '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "' : " + Helper.getDuration(appDuration) + " sebelum layar terkunci");

                    //setealah itu baru simpan nama_aplikasi

                    //kelas objek untuk database lokal
                    daftarAplikasiInput = new DaftarAplikasiModel(-1, currentApp, appDuration, -1, keydate, clockFormat.format(System.currentTimeMillis()));
                    appUsageCollectionInput = new AppUsageCollectionModel(-1, currentApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), keydate, Helper.getDuration(appDuration));
                    //kelas objek untuk firestore
                    daftarAplikasiModelFirebase = new DaftarAplikasiModelFirebase(clockFormat.format(System.currentTimeMillis()), appDuration, 1);
                    appUsageCollectionModelFirebase = new AppUsageCollectionModelFirebase(currentApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), Helper.getDuration(appDuration));

                    // DaftarAplikasi
                    db.checkRecordIfExist(daftarAplikasiInput);
                    firestoreDaftarAplikasi(clockFormat.format(System.currentTimeMillis()), appDuration, userId, dateFormat.format(System.currentTimeMillis()), compareApp, daftarAplikasiModelFirebase);
                    // AppCollection
                    db.addRecordAppCollection(appUsageCollectionInput);
                    firestoreAppCollection(userId, dateFormat.format(System.currentTimeMillis()), clockFormat.format(System.currentTimeMillis()), appUsageCollectionModelFirebase);

                    Log.e(TAG, "run: informasi penggunaan disimpan di database");
                    currentApp = "NULL";
                    editor.putString("compareApp", currentApp);
                    editor.putLong("firstActive", System.currentTimeMillis());
                    editor.apply();
                }
            }
        });
    }

    private void firestoreAppCollection(String userId, String tanggalPakai, String jamPakai, AppUsageCollectionModelFirebase appUsageCollectionModelFirebase) {
        firestore.setFirestoreSettings(firestoreSettings);
        firestore.collection("AppUsage")
                .document(userId)
                .collection("AppCollection")
                .document(tanggalPakai)
                .collection("jam_pakai")
                .document(jamPakai)
                .set(appUsageCollectionModelFirebase)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "AppCollection berhasil ditambahkan ke dalam firestore");
                        }
                    }
                });
    }

    private void firestoreDaftarAplikasi(final String jamPakai, final long durasiPakai, String userId, String tanggalPakai, String AppName, final DaftarAplikasiModelFirebase daftarAplikasiModelFirebase) {
        final DocumentReference docIdRef = firestore.collection("AppUsage")
                .document(userId)
                .collection("DaftarAplikasi")
                .document(tanggalPakai)
                .collection("AppName")
                .document(AppName);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //  Cek apakah daftar aplikasi sudah tersimpan atau belum untuk melakukan penambahan record atau update record
                    if (Objects.requireNonNull(document).exists()) {
                        docIdRef.update("durasi_pakai", FieldValue.increment(durasiPakai));
                        docIdRef.update("frekuensi_pakai", FieldValue.increment(1));
                        docIdRef.update("terakhir_dipakai", jamPakai);
                    } else {
                        docIdRef.set(daftarAplikasiModelFirebase);
                    }
                }
            }
        });
    }

    private void basedTimeLockStart(final Context context) {
        Log.e(TAG, "basedTimeLockStart: Menghitung mulai");
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            long timerLayarHidup = sp.getLong("timerLayarHidup", 0);
            int kunciLayar = sp.getInt("kunciLayar", 0);
            //            long timerKunciLayar = sp.getLong("timerKunciLayar", 0);
//            long pembatasLayarhidup = 60;

            @Override
            public void run() {
                final SharedPreferences sp1 = context.getSharedPreferences(VALUEID, 0);
                long basedTimeStartHour = sp1.getLong("basedTimeStartHour", 0);
                long basedTimeStartMinute = sp1.getLong("basedTimeStartMinute", 0);
                long pembatasLayarhidup = ((basedTimeStartHour * 3600) + (basedTimeStartMinute * 60));
                int basedScheduleLock = sp.getInt("basedScheduleLock", 0);

                String dateLockPhone = sp.getString("dateLockPhone", "");
                int pernahLock = sp.getInt("pernahLock", 0);
                if (kunciLayar == 1) {
                    Log.e(TAG, "kunci layar aktif!!");
                }

                if (getCurrentApp && kunciLayar == 0) {
                    String switchbasedTime = sp.getString("basedTimeSwitchState", "");
                    int radioTimeBaseState = sp.getInt("radioTimeBaseState", 0);
                    if (!dateLockPhone.equals(dateFormat.format(System.currentTimeMillis())) && kunciLayar == 0) {
                        Log.e(TAG, "basedTimeLockStart : tanggal pembanding" + dateLockPhone);
                        editor.putInt("pernahLock", 0);
                        editor.putString("dateLockPhone", dateFormat.format(System.currentTimeMillis()));
                        editor.apply();
                        Log.e(TAG, "basedTimeLockStart : tanggal di ubah ke" + dateFormat.format(System.currentTimeMillis()));
                    }
                    if (switchbasedTime.equals("Aktif")) {
                        if (!(pernahLock == 1 && radioTimeBaseState == 0)) {
                            Log.e(TAG, "pembatasLayarHidup : " + pembatasLayarhidup + " timerLayarHidup : " + timerLayarHidup);
                            if (pembatasLayarhidup >= 0) {
                                if (timerLayarHidup < pembatasLayarhidup) {
                                    timerLayarHidup++;
                                    Log.e(TAG, "run: basedTimeLockStart " + timerLayarHidup);
                                }
                            }
                        }
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    editor.putLong("timerLayarHidup", timerLayarHidup);
                    editor.apply();
                }
                if (timerLayarHidup >= pembatasLayarhidup || basedScheduleLock == 1) {
                    Log.e(TAG, "timerLayarHidup : " + Helper.getDuration(timerLayarHidup * 1000) + "pembatasLayarHidup :" + Helper.getDuration(pembatasLayarhidup * 1000) + "basedScheduleLock : " + basedScheduleLock);
                    final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    Objects.requireNonNull(devicePolicyManager).lockNow();
                    Log.e(TAG, "timerLayarHidup >= pembatasLayarhidup");
                    editor.putInt("kunciLayar", 1);
                    editor.putLong("timerLayarHidup", timerLayarHidup);
                    editor.putLong("timerKunciLayar", 0);
                    editor.apply();
                    handler.removeCallbacks(this);
                }
            }
        });
    }

    private void basedTimeLockFinish(Context context) {
        Log.e(TAG, "basedTimeLockFinish: Menghitung mulai");
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();

        final Handler handler = new Handler();
        handler.post(new Runnable() {

            int kunciLayar = sp.getInt("kunciLayar", 0);
            long timerKunciLayar = sp.getLong("timerKunciLayar", 0);
            //        long timerLayarHidup = sp.getLong("timerLayarHidup", 0);
            long basedTimeFinishHour = sp.getLong("basedTimeFinishHour", 0);
            long basedTimeFinishMinute = sp.getLong("basedTimeFinishMinute", 0);
            long pembatasKunciLayar = ((basedTimeFinishHour * 3600) + (basedTimeFinishMinute * 60));
//            long pembatasKunciLayar = 60;

            @Override
            public void run() {
                String dateLockPhone = sp.getString("dateLockPhone", "");
                int pernahLock = sp.getInt("pernahLock", 0);
                if (!getCurrentApp && kunciLayar == 1) {
                    String switchbasedTime = sp.getString("basedTimeSwitchState", "");
                    int radioTimeBaseState = sp.getInt("radioTimeBaseState", 0);
                    if (switchbasedTime.equals("Aktif")) {
                        if (!(pernahLock == 1 && radioTimeBaseState == 0)) {
                            if (timerKunciLayar < pembatasKunciLayar) {
                                timerKunciLayar++;
                                Log.e(TAG, "run: basedTimeLockFinish " + timerKunciLayar);
                            }
                        }
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    editor.putLong("timerKunciLayar", timerKunciLayar);
                    editor.apply();
                }
                if (timerKunciLayar >= pembatasKunciLayar) {
                    Log.e(TAG, "timerKunciLayar >= pembatasKunciLayar");
                    if (!dateLockPhone.equals(dateFormat.format(System.currentTimeMillis())) && kunciLayar == 1) {
                        editor.putInt("pernahLock", 0);
                        Log.e(TAG, "Tanggal dateLockPhone di ubah!!");
                    } else {
                        editor.putInt("pernahLock", 1);
                    }
                    editor.putInt("kunciLayar", 0);
                    editor.putLong("timerKunciLayar", timerKunciLayar);
                    editor.putLong("timerLayarHidup", 0);
                    editor.apply();
                    handler.removeCallbacks(this);
                }
            }
        });
    }

    private void basedScheduleLockPhoneStart(final Context context) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            String dateLockPhone1 = sp.getString("dateLockPhone1", "");

            @Override
            public void run() {
                if (getCurrentApp) {
                    String basedScheduleSwitchState = sp.getString("basedScheduleSwitchState", "");
                    Calendar jam = Calendar.getInstance();
                    long hour = jam.get(Calendar.HOUR_OF_DAY) * 3600;
                    long minute = jam.get(Calendar.MINUTE) * 60;
                    long second = jam.get(Calendar.SECOND);
                    long jamSekarang = hour + minute + second;
                    long basedScheduleStartHour = sp.getLong("basedScheduleStartHour", 0);
                    long basedScheduleStartMinute = sp.getLong("basedScheduleStartMinute", 0);
                    long pembatasLayarHidup1 = ((basedScheduleStartHour * 3600) + (basedScheduleStartMinute * 60));

                    long basedScheduleFinishHour = sp.getLong("basedScheduleFinishHour", 0);
                    long basedScheduleFinishMinute = sp.getLong("basedScheduleFinishMinute", 0);
                    long pembatasKunciLayar = ((basedScheduleFinishHour * 3600) + (basedScheduleFinishMinute * 60));

                    int basedScheduleLock = sp.getInt("basedScheduleLock", 0);
                    int pernahLockBasedSchedule = sp.getInt("pernahLockBasedSchedule", 0);
                    if (!dateLockPhone1.equals(dateFormat.format(System.currentTimeMillis()))) {
                        editor.putString("dateLockPhone1", dateFormat.format(System.currentTimeMillis()));
                        editor.putInt("pernahLockBasedSchedule", 0);
                        editor.apply();
                    }
                    if (pernahLockBasedSchedule == 0) {
                        if (pembatasLayarHidup1 < pembatasKunciLayar) {
                            if (jamSekarang >= pembatasLayarHidup1) {
                                if (basedScheduleLock == 1) {
                                    Log.e(TAG, "baseScheduledLockPhone kunci layar aktif");
                                    final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                                    Objects.requireNonNull(devicePolicyManager).lockNow();
                                }
                            }
                        }
                        if (pembatasLayarHidup1 > pembatasKunciLayar) {
                            if (jamSekarang > pembatasLayarHidup1 || jamSekarang < pembatasKunciLayar) {
                                if (basedScheduleLock == 1) {
                                    Log.e(TAG, "baseScheduledLockPhone kunci layar aktif");
                                    final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                                    Objects.requireNonNull(devicePolicyManager).lockNow();
                                }
                            }
                        }
                        if (getCurrentApp && basedScheduleLock == 0) {
                            if (basedScheduleSwitchState.equals("Aktif")) {
                                if (jamSekarang >= pembatasLayarHidup1) {
                                    editor.putInt("basedScheduleLock", 1);
                                    editor.apply();
                                }
                            }
                        }
                    }
                    Log.e(TAG, "pernahLockBasedSchedule : " + sp.getInt("pernahLockBasedSchedule", 0));
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void basedSchedulePhoneFinish(Context context) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            String dateLockPhone1 = sp.getString("dateLockPhone1", "");

            @Override
            public void run() {
                if (!getCurrentApp) {
                    String basedScheduleSwitchState = sp.getString("basedScheduleSwitchState", "");
                    Calendar jam = Calendar.getInstance();
                    long hour = jam.get(Calendar.HOUR_OF_DAY) * 3600;
                    long minute = jam.get(Calendar.MINUTE) * 60;
                    long second = jam.get(Calendar.SECOND);
                    long jamSekarang = hour + minute + second;
                    long basedScheduleFinishHour = sp.getLong("basedScheduleFinishHour", 0);
                    long basedScheduleFinishMinute = sp.getLong("basedScheduleFinishMinute", 0);
                    long pembatasKunciLayar = ((basedScheduleFinishHour * 3600) + (basedScheduleFinishMinute * 60));
                    int basedScheduleLock = sp.getInt("basedScheduleLock", 0);
                    if (!getCurrentApp && basedScheduleLock == 1) {
                        if (basedScheduleSwitchState.equals("Aktif")) {
                            if (jamSekarang >= pembatasKunciLayar) {
                                Log.e(TAG, "baseScheduledLockPhone kunci layar telah nonaktif");
                                editor.putInt("basedScheduleLock", 0);
                                if (!dateLockPhone1.equals(dateFormat.format(System.currentTimeMillis()))) {
                                    editor.putString("dateLockPhone1", dateFormat.format(System.currentTimeMillis()));
                                    editor.putInt("pernahLockBasedSchedule", 0);
                                } else {
                                    editor.putInt("pernahLockBasedSchedule", 1);
                                }
                                editor.apply();
                            }
                        }
                    }
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }


    //==================================================================================================
    public static void oneClickLockPhone(final Context context, final long oneClickTimer) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            long timerOneClickLock = sp.getLong("timerOneClickLock", 0);

            @Override
            public void run() {
                final int oneClickLock = sp.getInt("oneClickLock", 0);
                if (getCurrentApp && oneClickLock == 1) {
                    final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    Objects.requireNonNull(devicePolicyManager).lockNow();
                }
                if (timerOneClickLock < oneClickTimer) {
                    timerOneClickLock++;
                }
                if (timerOneClickLock >= oneClickTimer) {
                    editor.putInt("oneClickLock", 0);
                    editor.apply();
                }
                handler.postDelayed(this, 1000);
                if (powerOff || getCurrentApp) {
                    editor.putLong("timerOneClickLock", timerOneClickLock);
                    editor.apply();
                }
            }
        });
    }
}