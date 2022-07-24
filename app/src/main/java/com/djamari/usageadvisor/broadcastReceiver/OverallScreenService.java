package com.djamari.usageadvisor.broadcastReceiver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.AppUsageCollectionModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.DaftarAplikasiModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase;
import com.djamari.usageadvisor.firebaseModel.AppUsageCollectionModelFirebase;
import com.djamari.usageadvisor.firebaseModel.DaftarAplikasiModelFirebase;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.service.ForegroundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static com.djamari.usageadvisor.app.AppStarter.CHANNEL_ID_1;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleSwitchState;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeSwitchState;
import static com.djamari.usageadvisor.helper.SharePref_keys.CompareApp;
import static com.djamari.usageadvisor.helper.SharePref_keys.DateLockPhone;
import static com.djamari.usageadvisor.helper.SharePref_keys.DateLockPhone1;
import static com.djamari.usageadvisor.helper.SharePref_keys.DateLockPhone2;
import static com.djamari.usageadvisor.helper.SharePref_keys.FirstActive;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedTimeLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.HadRestart;
import static com.djamari.usageadvisor.helper.SharePref_keys.KeyDate;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.LayarHidup;
import static com.djamari.usageadvisor.helper.SharePref_keys.LockTimeFinishDayOfYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.LockTimeFinishHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.LockTimeFinishMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.LockTimeFinishSecond;
import static com.djamari.usageadvisor.helper.SharePref_keys.LockTimeFinishYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickDayOfYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickHourFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickMinuteFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickSecondFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.PernahLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.PernahLockBasedSchedule;
import static com.djamari.usageadvisor.helper.SharePref_keys.RadioTimeBaseState;
import static com.djamari.usageadvisor.helper.SharePref_keys.RadioTimeBaseStateGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.StatusPerubahanInteraksi;
import static com.djamari.usageadvisor.helper.SharePref_keys.SwitchBasedScheduleGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.SwitchBasedTimeGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.TimerLayarHidup;
import static com.djamari.usageadvisor.helper.SharePref_keys.UnlockScreen;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID2;

public class OverallScreenService extends BroadcastReceiver {
    private static final String TAG = "screenUsageService";
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat clockFormat = new SimpleDateFormat("HH:mm:ss");

    NotificationManagerCompat notificationManager;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String dateNow, dateValue;
    private Calendar calendar = Calendar.getInstance();
    private long seconds = 0;
    public static boolean lockScreen;
    private static boolean powerOff;
    private static boolean getCurrentApp;
    private String currentApp = "NULL", compareApp;
    private DaftarAplikasiModel daftarAplikasiInput;
    private AppUsageCollectionModel appUsageCollectionInput;
    private AppUsageCollectionModelFirebase appUsageCollectionModelFirebase;
    private DaftarAplikasiModelFirebase daftarAplikasiModelFirebase;
    private SharedPreferences sp1;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        sp1 = context.getSharedPreferences(VALUEID1, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final int basedTimeLock = sp.getInt(KunciLayar, 0);
        final int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
        final int oneClickLock = sp.getInt(OneClickLock, 0);

        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) || Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            lockScreen = true;
            powerOff = false;

        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            lockScreen = false;
            getCurrentApp = false;
            powerOff = false;
//            if (user != null) {
////                basedTimeLockFinish(context.getApplicationContext());
////                basedSchedulePhoneFinish(context.getApplicationContext());
//                oneClickLockPhone(context.getApplicationContext());
//            }
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
            int hadRestart = sp.getInt(HadRestart, 0);

            if (hadRestart == 1) {
                oneClickLockPhone(context.getApplicationContext());
            }
            lockScreen = false;
            getCurrentApp = true;
            long savedunlockScreen = sp.getLong(UnlockScreen, 0);
            long layarHidup = sp.getLong(LayarHidup, 0);
            Log.d(TAG, "Hitung kunci terbuka : " + savedunlockScreen);
//            Toast.makeText(context, "context service: " + context.toString(), Toast.LENGTH_SHORT).show();
            //    private long timerKunciLayar;
            //    private long timerLayarHidup;
            //    private int pernahLock;
            //    private int kunciLayar;
            long unlockScreen = savedunlockScreen;
            Log.d(TAG, "Kunci terbuka " + unlockScreen + " kali");
            String input = "<b>U+2022 Penggunaan Layar : </b>" + (Helper.getDuration(layarHidup * 1000)) + "<<br><b>U+2022 Kunci Layar Terbuka : </b>" + (unlockScreen) + " Kali";
            Intent serviceIntent = new Intent(context, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", Html.fromHtml(input));
            ContextCompat.startForegroundService(context, serviceIntent);

            unlockScreen++;
            editor.putLong(UnlockScreen, unlockScreen);
            editor.apply();
            if (user != null) {
                Toast.makeText(context.getApplicationContext(), "Service UsageAdvisor Berjalan", Toast.LENGTH_SHORT).show();
                final String userId = user.getUid();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!sp1.getString(SharePref_id_group, "").equals("") && basedTimeLock != 1 && basedScheduleLock != 1 && oneClickLock != 1) {
                            updateLockSettingGrup(context);
                        }
                    }
                }, 3000);

                final Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        final int basedTimeLock = sp.getInt(KunciLayar, 0);
                        final int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
                        final int oneClickLock = sp.getInt(OneClickLock, 0);
                        if (getCurrentApp) {
                            if (basedTimeLock == 1 || basedScheduleLock == 1 || oneClickLock == 1) {
                                captureCurrentApp(context);
                                if (currentApp.toLowerCase().contains("setting")) {
                                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                                    startMain.addCategory(Intent.CATEGORY_HOME);
                                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(startMain);
                                }
                                compareApp = "NULL";
                            }
                            if (basedTimeLock != 1 && basedScheduleLock != 1 && oneClickLock != 1) {
                                Log.d(TAG, "basedTimeLock != 1 && basedScheduleLock != 1 && oneClickLock != 1");
                                printForegroundTask(context.getApplicationContext(), userId);
                            } else {
                                Log.d(TAG, "!(basedTimeLock != 1 && basedScheduleLock != 1 && oneClickLock != 1)");
                                handler1.postDelayed(this, 1000);
                            }
                        }
                    }
                });

                if (getCurrentApp && basedTimeLock != 1 && basedScheduleLock != 1 && oneClickLock != 1) {
                    printForegroundTask(context.getApplicationContext(), userId);
                }
                basedTimeLockStart(context.getApplicationContext());
                basedScheduleLockPhoneStart(context.getApplicationContext());
            }
            Timer(context.getApplicationContext(), user);
        }
    }

    private void Timer(final Context context, final FirebaseUser user) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final PersonalUsageDatabase db = new PersonalUsageDatabase(context.getApplicationContext());
        final long waktuPembanding = sp.getLong(LayarHidup, 0);

        Log.d(TAG, "Timer: waktu pembanding : " + Helper.getDuration(waktuPembanding * 1000));
        Log.d(TAG, "keydate : " + dateValue + " tanggal pembanding : " + dateFormat.format(calendar.getTimeInMillis()));

        if (seconds < waktuPembanding) {
            seconds += waktuPembanding;
        }
        final Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                dateValue = sp.getString(KeyDate, "");
                int basedTimeLock = sp.getInt(KunciLayar, 0);
                int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
                int oneClickLock = sp.getInt(OneClickLock, 0);
                if (getCurrentApp) {
                    long layarHidup = sp.getLong(LayarHidup, 0);
//                    int notifikasi = sp.getInt("notifikasiPenggunaan", 0);
                    Log.d(TAG, "run: " + layarHidup + " - " + seconds + " dateValue "
                            + dateValue + " tanggal pembanding " + dateFormat.format(System.currentTimeMillis()));
                    if (!dateValue.equals(dateFormat.format(System.currentTimeMillis()))) {
                        seconds = 0;
                        dateNow = dateFormat.format(System.currentTimeMillis());
//                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        if (user != null) {
                            String Uid = FirebaseAuth.getInstance().getUid();
                            @SuppressWarnings("WeakerAccess")
                            class AdditionInfo {
                                public String dateOfAdd;

                                AdditionInfo(String dateOfAdd) {
                                    this.dateOfAdd = dateOfAdd;
                                }
                            }
                            AdditionInfo addInfo = new AdditionInfo(dateFormat.format(System.currentTimeMillis()));
                            assert Uid != null;
                            firestore.collection("AppUsage").document(Uid).collection("AppCollection").document(dateFormat.format(System.currentTimeMillis())).set(addInfo);
                            firestore.collection("AppUsage").document(Uid).collection("DaftarAplikasi").document(dateFormat.format(System.currentTimeMillis())).set(addInfo);
                        }
                        editor.putLong(LayarHidup, 0);
                        //db deleteRecord
                        db.deleteAllDaftarAplikasiRecord();
                        db.deleteAllAppCollectionRecord();
                        editor.putString(KeyDate, dateNow);
                        editor.putLong(UnlockScreen, 1);
                        editor.putLong(TimerLayarHidup, 0);
                        editor.apply();
                        //db
                        Log.d(TAG, "timer di reset ke 0");
                        Log.d(TAG, "keydate diset ke - " + dateNow);

                    }
                    if (seconds != 0 && seconds % 60 == 0) {
                        Log.d(TAG, "waktu bertambah 60 detik");
                        editor.putLong(LayarHidup, seconds);
                        editor.apply();
                        Log.d(TAG, "Waktu disimpan di sharedpreference");

                    }
                    if (seconds == 3600) {
                        openNotificationUsage(context, seconds);
                    }
                    if (seconds > 3600 && seconds < 14400 && seconds % 1800 == 0) {
                        openNotificationUsage(context, seconds);
                    }
                    if (seconds > 14400 && seconds % 600 == 0) {
                        openNotificationUsage(context, seconds);
                    }
                    seconds++;
                    handler.postDelayed(this, 1000);
                    if (seconds % 15 == 0 && user != null && basedTimeLock != 1 || oneClickLock != 1 || basedScheduleLock != 1) {
                        fireStoreUpdateUsage(context, Objects.requireNonNull(user));
                    }
                    if (seconds % 57 == 0 && seconds % 60 != 0 && user != null && (basedScheduleLock != 1 || basedTimeLock != 1) && !sp1.getString(SharePref_id_group, "").equals("")) {
                        updateLockSettingGrup(context);
                    }
                } else {
                    Log.d(TAG, "Stop Counting Time");
                    Log.d(TAG, "Stop Updating Time ");
                    editor.putLong(LayarHidup, seconds);
                    editor.apply();
                    long layarHidup = sp.getLong(LayarHidup, 0);
                    Log.d(TAG, "saved : " + Helper.getDuration(layarHidup * 1000) + " Time Running " + Helper.getDuration(seconds * 1000));
                    Log.d(TAG, "Time saved into SharedPreference");
                }
                if (powerOff) {
                    Log.d(TAG, "Stop Counting Time");
                    Log.d(TAG, "Stop Updating Time ");
                    editor.putLong(LayarHidup, seconds);
                    editor.apply();
                    long layarHidup = sp.getLong(LayarHidup, 0);
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
                compareApp = sp.getString(CompareApp, "");
                long firstActive = sp.getLong(FirstActive, 0), appDuration;
                String keydate = sp.getString(KeyDate, "");
                if (getCurrentApp) {
                    captureCurrentApp(context);
                    if (!compareApp.equals(currentApp)) {
                        if (compareApp.equals("NULL") || compareApp.trim().equals("")) {
                            Log.e(TAG, "Layar kunci terbuka");
                            Log.e(TAG, "Current App in foreground is: '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "'");

                        } else if (!compareApp.equals("NULL") && !compareApp.trim().equals("")) {
                            Log.e(TAG, "Current App in foreground is: '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "'");
                            appDuration = System.currentTimeMillis() - firstActive;

                            //Simpan terlebih dahulu nama_aplikasi dengan "compareApp"
                            Log.e(TAG, "Durasi pakai Aplikasi '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), compareApp) + "':" + Helper.getDuration(appDuration));
                            //kelas objek untuk database lokal
                            daftarAplikasiInput = new DaftarAplikasiModel(-1, compareApp, appDuration, -1, keydate, clockFormat.format(System.currentTimeMillis()));
                            appUsageCollectionInput = new AppUsageCollectionModel(-1, compareApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), keydate, Helper.getDuration(appDuration));
                            //kelas objek untuk firestore
                            daftarAplikasiModelFirebase = new DaftarAplikasiModelFirebase(clockFormat.format(System.currentTimeMillis()), appDuration, 1);
                            appUsageCollectionModelFirebase = new AppUsageCollectionModelFirebase(Helper.getAppNameFromPkgName(context, compareApp), clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), Helper.getDuration(appDuration));

                            // DaftarAplikasi
                            db.checkRecordIfExist(daftarAplikasiInput);
                            firestoreDaftarAplikasi(clockFormat.format(System.currentTimeMillis()), appDuration, userId, dateFormat.format(System.currentTimeMillis()), Helper.getAppNameFromPkgName(context.getApplicationContext(), compareApp), daftarAplikasiModelFirebase);
                            // AppCollection
                            db.addRecordAppCollection(appUsageCollectionInput);
                            firestoreAppCollection(userId, dateFormat.format(System.currentTimeMillis()), clockFormat.format(System.currentTimeMillis()), appUsageCollectionModelFirebase);
                            editor.putLong(StatusPerubahanInteraksi, System.currentTimeMillis());
                            Log.d(TAG, "run: informasi penggunaan di simpan di database");
                        }
                        editor.putString(CompareApp, currentApp);
                        editor.putLong(FirstActive, System.currentTimeMillis());
                        editor.apply();
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    if (!currentApp.equals("NULL") && !currentApp.trim().equals("")) {
                        appDuration = System.currentTimeMillis() - firstActive;
                        Log.e(TAG, "Durasi pakai Aplikasi '" + Helper.getAppNameFromPkgName(context.getApplicationContext(), currentApp) + "' : " + Helper.getDuration(appDuration) + " sebelum layar terkunci");

                        //setealah itu baru simpan nama_aplikasi

                        //kelas objek untuk database lokal
                        daftarAplikasiInput = new DaftarAplikasiModel(-1, currentApp, appDuration, -1, keydate, clockFormat.format(System.currentTimeMillis()));
                        appUsageCollectionInput = new AppUsageCollectionModel(-1, currentApp, clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), keydate, Helper.getDuration(appDuration));
                        //kelas objek untuk firestore
                        daftarAplikasiModelFirebase = new DaftarAplikasiModelFirebase(clockFormat.format(System.currentTimeMillis()), appDuration, 1);
                        appUsageCollectionModelFirebase = new AppUsageCollectionModelFirebase(Helper.getAppNameFromPkgName(context, currentApp), clockFormat.format(firstActive), clockFormat.format(System.currentTimeMillis()), Helper.getDuration(appDuration));

                        // DaftarAplikasi
                        db.checkRecordIfExist(daftarAplikasiInput);
                        firestoreDaftarAplikasi(clockFormat.format(System.currentTimeMillis()), appDuration, userId, dateFormat.format(System.currentTimeMillis()), Helper.getAppNameFromPkgName(context, currentApp), daftarAplikasiModelFirebase);
                        // AppCollection
                        db.addRecordAppCollection(appUsageCollectionInput);
                        firestoreAppCollection(userId, dateFormat.format(System.currentTimeMillis()), clockFormat.format(System.currentTimeMillis()), appUsageCollectionModelFirebase);

                        Log.e(TAG, "run: informasi penggunaan disimpan di database");
                        currentApp = "NULL";
                        editor.putString(CompareApp, currentApp);
                        editor.putLong(FirstActive, System.currentTimeMillis());
                        editor.apply();
                    }
                }
            }
        });
    }

    private void firestoreAppCollection(String userId, String tanggalPakai, String jamPakai, AppUsageCollectionModelFirebase appUsageCollectionModelFirebase) {

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

    private void fireStoreUpdateUsage(Context context, final FirebaseUser user) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final String userID = user.getUid();
        final PersonalUsageDatabase db = new PersonalUsageDatabase(context.getApplicationContext());
        final List<DaftarAplikasiModel> getAllDaftarAplikasi = db.getAllDaftarAplikasi();
        long totaltime = 0;
        long frekuensi = 0;
        for (DaftarAplikasiModel dam : getAllDaftarAplikasi) {
            totaltime = totaltime + dam.getDurasi_pakai();
            frekuensi = frekuensi + dam.getFrekuensi_pakai();
        }
        final DocumentReference docRef = firestore.collection("Users").document(userID);
        final long finalFrekuensi = frekuensi;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (Objects.requireNonNull(document).exists()) {
                        docRef.update("durasi_layar", sp.getLong(LayarHidup, 0));
                        docRef.update("buka_kunci", sp.getLong(UnlockScreen, 0));
                        docRef.update("interaksi_aplikasi", finalFrekuensi);
                        docRef.update("terakhir_aktif", dateformat1.format(System.currentTimeMillis()) + " " + clockFormat.format(System.currentTimeMillis()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "onSuccess: fireStoreUpdateUsage update");
                                    }
                                });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "fireStoreUpdateUsage :" + e.toString());
            }
        });
    }


    private void basedTimeLockStart(final Context context) {
        Log.e(TAG, "basedTimeLockStart: Menghitung mulai");
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final SharedPreferences sp1 = context.getSharedPreferences(VALUEID2, 0);
        final SharedPreferences sp2 = context.getSharedPreferences(VALUEID1, 0);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            long timerLayarHidup = sp.getLong(TimerLayarHidup, 0);
            int pernahLock = sp.getInt(PernahLock, 0);
            final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            String id_group = sp2.getString(SharePref_id_group, "");
            long basedTimeStartHour, basedTimeStartMinute, basedTimeFinishHour, basedTimeFinishMinute;
            int radioTimeBaseState;
            String switchbasedTime;

            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                int grupBasedTimeLock = sp1.getInt(GrupBasedTimeLock, 0);
                int kunciLayar = sp.getInt(KunciLayar, 0);
                if (!id_group.equals("") && grupBasedTimeLock == 1) {
                    basedTimeStartHour = sp1.getInt(BasedTimeStartHourGrup, 0);
                    basedTimeStartMinute = sp1.getInt(BasedTimeStartMinuteGrup, 0);
                    basedTimeFinishHour = sp1.getInt(BasedTimeFinishHourGrup, 0);
                    basedTimeFinishMinute = sp1.getInt(BasedTimeFinishMinuteGrup, 0);
                    radioTimeBaseState = sp1.getInt(RadioTimeBaseStateGrup, 0);
                    switchbasedTime = sp1.getString(SwitchBasedTimeGrup, "");

                } else {
                    basedTimeStartHour = sp.getLong(BasedTimeStartHour, 0);
                    basedTimeStartMinute = sp.getLong(BasedTimeStartMinute, 0);
                    basedTimeFinishHour = sp.getLong(BasedTimeFinishHour, 0);
                    basedTimeFinishMinute = sp.getLong(BasedTimeFinishMinute, 0);
                    radioTimeBaseState = sp.getInt(RadioTimeBaseState, 0);
                    switchbasedTime = sp.getString(BasedTimeSwitchState, "");
                }
                long pembatasLayarhidup = ((basedTimeStartHour * 3600) + (basedTimeStartMinute * 60));
                int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
                String dateLockPhone = sp.getString(DateLockPhone, "");
                if (kunciLayar == 1) {
                    Log.e(TAG, "kunci layar aktif!!");
                }

                long lockFinishHour = sp.getLong(LockTimeFinishHour, 0);
                long lockFinishMinute = sp.getLong(LockTimeFinishMinute, 0);
                long lockFinishSecond = sp.getLong(LockTimeFinishSecond, 0);
                long lockTimeFinishYear = sp.getLong(LockTimeFinishYear, 0);
                long lockTimeFinishDayOfYear = sp.getLong(LockTimeFinishDayOfYear, 0);
                long lockTimeFinishYearDate = (lockTimeFinishYear * 1000) + (lockTimeFinishDayOfYear);

                long yearNow = cal.get(Calendar.YEAR);
                long dayOfYearNow = cal.get(Calendar.DAY_OF_YEAR);
                long yearDateNow = (yearNow * 1000) + dayOfYearNow;
                if (getCurrentApp && kunciLayar == 1) {
                    if (lockFinishMinute >= 60) {
                        lockFinishMinute = lockFinishMinute - 60;
                        lockFinishHour = lockFinishHour + 1;
                    }
                    if (lockFinishHour >= 24) {
                        if (!dateLockPhone.equals(dateFormat.format(System.currentTimeMillis()))) {
                            lockTimeFinishYearDate = (yearNow * 1000) + dayOfYearNow;
                            lockFinishHour = lockFinishHour - 24;
                        }
                    }
                    long lockTimeFinish = (lockFinishHour * 3600) + (lockFinishMinute * 60) + (lockFinishSecond);
                    long jamSekarang = (cal.get(Calendar.HOUR_OF_DAY) * 3600) + (cal.get(Calendar.MINUTE) * 60) + (cal.get(Calendar.SECOND));
                    Log.i(TAG, "lockTimeFinish " + lockTimeFinish + " ::: " + lockFinishHour + ":" + lockFinishMinute + ":" + lockFinishSecond);
                    Log.i(TAG, "jamSekarang " + jamSekarang + " ::: " + (cal.get(Calendar.HOUR_OF_DAY)) + ":" + (cal.get(Calendar.MINUTE)) + ":" + (cal.get(Calendar.SECOND)));
                    if ((jamSekarang > lockTimeFinish && yearDateNow == lockTimeFinishYearDate) || ((jamSekarang < lockTimeFinish) && yearDateNow > lockTimeFinishYearDate)) {
                        Log.e(TAG, "rune31qd: kuncilayar terbuka");
                        editor.putInt(KunciLayar, 0);
                        editor.putLong(TimerLayarHidup, 0);
                        editor.apply();
                        kunciLayar = 0;
                        timerLayarHidup = 0;
                        getCurrentApp = false;
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Kunci terbuka pada jam : " + lockFinishHour + ":" + lockFinishMinute + ":" + lockFinishSecond, Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "yearDateNow :: " + yearDateNow + " lockTimeFinishYearDate :: " + lockTimeFinishYearDate);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Objects.requireNonNull(devicePolicyManager).lockNow();
                            }
                        }, 3000);
                    }
                }
                if (getCurrentApp && kunciLayar == 0) {
                    if (!dateLockPhone.equals(dateFormat.format(System.currentTimeMillis()))) {
                        Log.e(TAG, "basedTimeLockStart : tanggal pembanding" + dateLockPhone);
                        editor.putInt(PernahLock, 0);
                        editor.putString(DateLockPhone, dateFormat.format(System.currentTimeMillis()));
                        editor.apply();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timerLayarHidup = sp.getLong(TimerLayarHidup, 0);
                                pernahLock = sp.getInt(PernahLock, 0);
                            }
                        }, 1000);
                        Log.e(TAG, "basedTimeLockStart : tanggal di ubah ke" + dateFormat.format(System.currentTimeMillis()));
                    }
                    if (switchbasedTime.equals("Aktif")) {
                        if (!(pernahLock == 1 && radioTimeBaseState == 0)) {
                            Log.e(TAG, "pembatasLayarHidup : " + pembatasLayarhidup + " timerLayarHidup : " + timerLayarHidup);
                            if (pembatasLayarhidup > 0) {
                                if (timerLayarHidup < pembatasLayarhidup) {
                                    timerLayarHidup++;
                                }
                            }
                        }
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    editor.putLong(TimerLayarHidup, timerLayarHidup);
                    editor.apply();
                }
                if ((timerLayarHidup >= pembatasLayarhidup && pembatasLayarhidup > 0) && kunciLayar == 0 && switchbasedTime.equals("Aktif")) {
                    long hour = cal.get(Calendar.HOUR_OF_DAY);
                    long minute = cal.get(Calendar.MINUTE);
                    long seconds = cal.get(Calendar.SECOND);
                    long year = cal.get(Calendar.YEAR);
                    long dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                    editor.putLong(LockTimeFinishHour, hour + basedTimeFinishHour);
                    editor.putLong(LockTimeFinishMinute, minute + basedTimeFinishMinute);
                    editor.putLong(LockTimeFinishSecond, seconds);
                    editor.putLong(LockTimeFinishYear, year);
                    editor.putLong(LockTimeFinishDayOfYear, dayOfYear);
                    editor.putInt(KunciLayar, 1);
                    editor.putLong(TimerLayarHidup, timerLayarHidup);
                    editor.apply();
                    Toast.makeText(context, "Kunci berdasarkan waktu Penggunaan mulai", Toast.LENGTH_SHORT).show();
                    getCurrentApp = false;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Objects.requireNonNull(devicePolicyManager).lockNow();
                            Log.e(TAG, "timerLayarHidup >= pembatasLayarhidup");
                        }
                    }, 3000);
                    Log.e(TAG, "timerLayarHidup : " + Helper.getDuration(timerLayarHidup * 1000) + "pembatasLayarHidup :" + Helper.getDuration(pembatasLayarhidup * 1000) + "basedScheduleLock : " + basedScheduleLock);
                }
            }
        });
    }

    private void captureCurrentApp(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            context.getApplicationContext();
            @SuppressLint("InlinedApi") UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
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
    }
//    private void basedTimeLockFinish(Context context) {
//        Log.e(TAG, "basedTimeLockFinish: Menghitung mulai");
//        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
//        final SharedPreferences.Editor editor = sp.edit();
//        final SharedPreferences sp1 = context.getSharedPreferences(VALUEID2, 0);
//        final SharedPreferences sp2 = context.getSharedPreferences(VALUEID1, 0);
//        Log.e(TAG, "timerKunciLayar : " + sp.getLong(TimerKunciLayar, 0));
//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            long timerKunciLayar = sp.getLong(TimerKunciLayar, 0);
//            String id_group = sp2.getString(SharePref_id_group, "");
//            long basedTimeFinishHour, basedTimeFinishMinute;
//            String switchbasedTime;
//            int radioTimeBaseState;
//
//            @Override
//            public void run() {
//                int grupBasedTimeLock = sp1.getInt(GrupBasedTimeLock, 0);
//                if (!id_group.equals("") && grupBasedTimeLock == 1) {
//                    basedTimeFinishHour = sp1.getInt(BasedTimeFinishHourGrup, 0);
//                    basedTimeFinishMinute = sp1.getInt(BasedTimeFinishMinuteGrup, 0);
//                    switchbasedTime = sp1.getString(SwitchBasedTimeGrup, "");
//                    radioTimeBaseState = sp1.getInt(RadioTimeBaseStateGrup, 0);
//                } else {
//                    basedTimeFinishHour = sp.getLong(BasedTimeFinishHour, 0);
//                    basedTimeFinishMinute = sp.getLong(BasedTimeFinishMinute, 0);
//                    switchbasedTime = sp.getString(BasedTimeSwitchState, "");
//                    radioTimeBaseState = sp.getInt(RadioTimeBaseState, 0);
//                }
//                long pembatasKunciLayar = ((basedTimeFinishHour * 3600) + (basedTimeFinishMinute * 60));
//                int kunciLayar = sp.getInt(KunciLayar, 0);
//                String dateLockPhone = sp.getString(DateLockPhone, "");
//                int pernahLock = sp.getInt(PernahLock, 0);
//                if ((!getCurrentApp) && kunciLayar == 1) {
//                    if (switchbasedTime.equals("Aktif")) {
//                        if (!(pernahLock == 1 && radioTimeBaseState == 0)) {
//                            if (timerKunciLayar < pembatasKunciLayar) {
//                                timerKunciLayar++;
//                                Log.e(TAG, "run: basedTimeLockFinish " + timerKunciLayar + " pembatas kunciLayar :" + pembatasKunciLayar);
//                            }
//                        }
//                        Log.e(TAG, "run: basedTimeLockFinish");
//                    }
//                    if (timerKunciLayar >= pembatasKunciLayar) {
//                        Log.e(TAG, "timerKunciLayar >= pembatasKunciLayar");
//                        if (!dateLockPhone.equals(dateFormat.format(System.currentTimeMillis()))) {
//                            editor.putInt(PernahLock, 0);
//                            Log.e(TAG, "Tanggal dateLockPhone di ubah!!");
//                        } else {
//                            editor.putInt(PernahLock, 1);
//                        }
//                        editor.putInt(KunciLayar, 0);
//                        editor.putLong(TimerKunciLayar, 0);
//                        editor.putLong(TimerLayarHidup, 0);
//                        editor.apply();
//                        handler.removeCallbacks(this);
//                    }
//                    handler.postDelayed(this, 1000);
//                }
//                if (lockScreen || getCurrentApp && kunciLayar == 1) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            editor.putLong(TimerKunciLayar, timerKunciLayar);
//                            editor.apply();
//                        }
//                    }, 1500);
//                }
//            }
//        });
//    }

    private void basedScheduleLockPhoneStart(final Context context) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final SharedPreferences sp1 = context.getSharedPreferences(VALUEID2, 0);
        final SharedPreferences sp2 = context.getSharedPreferences(VALUEID1, 0);
        final Handler handler = new Handler();

//        int basedScheduleLock = sp.getInt("basedScheduleLock", 0);
//        Toast.makeText(context, "basedScheduleLock " + basedScheduleLock, Toast.LENGTH_SHORT).show();
        handler.post(new Runnable() {
            String dateLockPhone1 = sp.getString(DateLockPhone1, "");
            final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            String id_group = sp2.getString(SharePref_id_group, "");
            String basedScheduleSwitchState;
            long basedScheduleStartHour, basedScheduleStartMinute, basedScheduleFinishHour, basedScheduleFinishMinute;

            @Override
            public void run() {
                int grupBasedScheduleLock = sp1.getInt(GrupBasedScheduleLock, 0);
                if (getCurrentApp) {
                    Calendar jam = Calendar.getInstance();
                    long hour = jam.get(Calendar.HOUR_OF_DAY) * 3600;
                    long minute = jam.get(Calendar.MINUTE) * 60;
                    long second = jam.get(Calendar.SECOND);
                    long jamSekarang = hour + minute + second;
                    if (!id_group.equals("") && grupBasedScheduleLock == 1) {
                        basedScheduleStartHour = sp1.getInt(BasedScheduleStartHourGrup, 0);
                        basedScheduleStartMinute = sp1.getInt(BasedScheduleStartMinuteGrup, 0);
                        basedScheduleFinishHour = sp1.getInt(BasedScheduleFinishHourGrup, 0);
                        basedScheduleFinishMinute = sp1.getInt(BasedScheduleFinishMinuteGrup, 0);
                        basedScheduleSwitchState = sp1.getString(SwitchBasedScheduleGrup, "");
                    } else {
                        basedScheduleStartHour = sp.getLong(BasedScheduleStartHour, 0);
                        basedScheduleStartMinute = sp.getLong(BasedScheduleStartMinute, 0);
                        basedScheduleFinishHour = sp.getLong(BasedScheduleFinishHour, 0);
                        basedScheduleFinishMinute = sp.getLong(BasedScheduleFinishMinute, 0);
                        basedScheduleSwitchState = sp.getString(BasedScheduleSwitchState, "");
                    }

                    long pembatasLayarHidup1 = ((basedScheduleStartHour * 3600) + (basedScheduleStartMinute * 60));
                    Log.e(TAG, "jam sekarang " + jam.get(Calendar.HOUR_OF_DAY) + ":" + jam.get(Calendar.MINUTE) + ":" + jam.get(Calendar.SECOND));
                    Log.e(TAG, "pembatasLayarHidup1 " + Helper.getDuration(pembatasLayarHidup1 * 1000));
                    long pembatasKunciLayar = ((basedScheduleFinishHour * 3600) + (basedScheduleFinishMinute * 60));

//                    int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
                    if (!dateLockPhone1.equals(dateFormat.format(System.currentTimeMillis()))) {
                        editor.putString(DateLockPhone1, dateFormat.format(System.currentTimeMillis()));
                        editor.putInt(PernahLockBasedSchedule, 0);
                        editor.apply();
                    }

                    if ((pembatasLayarHidup1 < pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
                        if (jamSekarang >= pembatasLayarHidup1 && jamSekarang <= pembatasKunciLayar) {
//                            if (basedScheduleLock == 1) {
                            editor.putInt(BasedScheduleLock, 1);
                            editor.apply();
                            Toast.makeText(context, "Kunci berdasarkan jadwal berjalan", Toast.LENGTH_SHORT).show();
                            getCurrentApp = false;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "baseScheduledLockPhone kunci layar aktif");
                                    Objects.requireNonNull(devicePolicyManager).lockNow();
                                }
                            }, 3000);
//                            }
                        } else {
                            editor.putInt(BasedScheduleLock, 0);
                            editor.apply();
                        }
                    }
                    if ((pembatasLayarHidup1 > pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
                        if (jamSekarang >= pembatasLayarHidup1 || jamSekarang <= pembatasKunciLayar) {
//                            if (basedScheduleLock == 1) {
                            editor.putInt(BasedScheduleLock, 1);
                            editor.apply();
                            Toast.makeText(context, "Kunci berdasarkan jadwal berjalan jamSekarang >= pembatasLayarHidup1 || jamSekarang <= pembatasKunciLayar", Toast.LENGTH_SHORT).show();
                            getCurrentApp = false;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "baseScheduledLockPhone kunci layar aktif");
                                    Objects.requireNonNull(devicePolicyManager).lockNow();
                                }
                            }, 3000);
//                            }
                        } else {
                            editor.putInt(BasedScheduleLock, 0);
                            editor.apply();
                        }
                    }
//                    if (getCurrentApp && basedScheduleLock == 0) {
//                        if ((pembatasLayarHidup1 > pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
//                            if (jamSekarang >= pembatasLayarHidup1 || jamSekarang <= pembatasKunciLayar) {
//                                editor.putInt(BasedScheduleLock, 1);
//                                editor.apply();
//                            }
//                        } else if ((pembatasLayarHidup1 < pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
//                            if (jamSekarang >= pembatasLayarHidup1 && jamSekarang <= pembatasKunciLayar) {
//                                editor.putInt(BasedScheduleLock, 1);
//                                editor.apply();
//                            }
//                        }
//                    }
//                    if (getCurrentApp && basedScheduleLock == 1) {
//                        if ((pembatasLayarHidup1 > pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
//                            if (!(jamSekarang >= pembatasLayarHidup1 || jamSekarang <= pembatasKunciLayar)) {
//                                editor.putInt(BasedScheduleLock, 0);
//                                editor.apply();
//                            }
//                        } else if ((pembatasLayarHidup1 < pembatasKunciLayar) && basedScheduleSwitchState.equals("Aktif")) {
//                            if (!(jamSekarang >= pembatasLayarHidup1 && jamSekarang <= pembatasKunciLayar)) {
//                                editor.putInt(BasedScheduleLock, 0);
//                                editor.apply();
//                            }
//                        }
//                    }
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

//    private void basedSchedulePhoneFinish(Context context) {
//        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
//        final SharedPreferences.Editor editor = sp.edit();
//        final SharedPreferences sp1 = context.getSharedPreferences(VALUEID2, 0);
//        final SharedPreferences sp2 = context.getSharedPreferences(VALUEID1, 0);
//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            String dateLockPhone1 = sp.getString(DateLockPhone1, "");
//            long timerBasedSchedule = sp.getLong(TimerBasedSchedule, 0);
//            String id_group = sp2.getString(SharePref_id_group, "");
//
//            long basedTimeFinishHour, basedTimeFinishMinute, basedScheduleFinishHour, basedScheduleFinishMinute, basedScheduleStartHour, basedScheduleStartMinute;
//
//
//            @Override
//            public void run() {
//                int grupBasedScheduleLock = sp1.getInt(GrupBasedScheduleLock, 0);
//                if (!id_group.equals("") && grupBasedScheduleLock == 1) {
//                    basedTimeFinishHour = sp.getInt(BasedTimeFinishHourGrup, 0);
//                    basedTimeFinishMinute = sp.getInt(BasedTimeFinishMinuteGrup, 0);
//                    basedScheduleFinishHour = sp.getInt(BasedScheduleFinishHourGrup, 0);
//                    basedScheduleFinishMinute = sp.getInt(BasedScheduleFinishMinuteGrup, 0);
//                    basedScheduleStartHour = sp.getInt(BasedScheduleStartHourGrup, 0);
//                    basedScheduleStartMinute = sp.getInt(BasedScheduleStartMinuteGrup, 0);
//                } else {
//                    basedTimeFinishHour = sp.getLong(BasedTimeFinishHour, 0);
//                    basedTimeFinishMinute = sp.getLong(BasedTimeFinishMinute, 0);
//                    basedScheduleFinishHour = sp.getLong(BasedScheduleFinishHour, 0);
//                    basedScheduleFinishMinute = sp.getLong(BasedScheduleFinishMinute, 0);
//                    basedScheduleStartHour = sp.getLong(BasedScheduleStartHour, 0);
//                    basedScheduleStartMinute = sp.getLong(BasedScheduleStartMinute, 0);
//                }
//                int kunciLayarBasedTime = sp.getInt(KunciLayar, 0);
//                long pembatasKunciLayarBasedTime = ((basedTimeFinishHour * 3600) + (basedTimeFinishMinute * 60));
//                long pembatasKunciLayar = ((basedScheduleFinishHour * 3600) + (basedScheduleFinishMinute * 60));
//                long pembatasLayarHidup1 = ((basedScheduleStartHour * 3600) + (basedScheduleStartMinute * 60));
//                String basedScheduleSwitchState = sp.getString(BasedScheduleSwitchState, "");
//                int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
//                if (!getCurrentApp && basedScheduleLock == 1) {
//                    Calendar jam = Calendar.getInstance();
//                    long hour = jam.get(Calendar.HOUR_OF_DAY);
//                    long minute = jam.get(Calendar.MINUTE);
//                    int gantiHari = sp.getInt(GantiHari, 0);
//                    if (!dateLockPhone1.equals(dateFormat.format(System.currentTimeMillis()))) {
//                        editor.putString(DateLockPhone1, dateFormat.format(System.currentTimeMillis()));
//                        editor.putInt(GantiHari, 1);
//                        editor.apply();
//                    }
//                    Log.e(TAG, "basedScheduleLock : " + basedScheduleLock);
//                    if (basedScheduleSwitchState.equals("Aktif")) {
//                        if (pembatasLayarHidup1 > pembatasKunciLayar) {
//                            if (gantiHari == 1) {
//                                if (hour >= basedScheduleFinishHour && minute >= basedScheduleFinishMinute) {
//                                    Log.e(TAG, "baseScheduledLockPhone kunci layar telah nonaktif");
//                                    editor.putInt(BasedScheduleLock, 0);
//                                    editor.putInt(GantiHari, 0);
//                                    editor.putLong(TimerBasedSchedule, 0);
//                                    if (timerBasedSchedule > pembatasKunciLayarBasedTime && kunciLayarBasedTime == 1) {
//                                        editor.putInt(KunciLayar, 0);
//                                        editor.putLong(TimerKunciLayar, timerBasedSchedule);
//                                        editor.putLong(TimerLayarHidup, 0);
//                                    }
//                                    if (timerBasedSchedule < pembatasKunciLayarBasedTime && kunciLayarBasedTime == 1) {
//                                        editor.putLong(TimerKunciLayar, timerBasedSchedule);
//                                    }
//                                    editor.apply();
//                                }
//                            }
//                        }
//                        if (pembatasLayarHidup1 < pembatasKunciLayar) {
//                            if (hour >= basedScheduleFinishHour && minute >= basedScheduleFinishMinute) {
//                                Log.e(TAG, "baseScheduledLockPhone kunci layar telah nonaktif");
//                                editor.putInt(BasedScheduleLock, 0);
//                                editor.putLong(TimerBasedSchedule, 0);
//                                if (timerBasedSchedule > pembatasKunciLayarBasedTime && kunciLayarBasedTime == 1) {
//                                    editor.putInt(KunciLayar, 0);
//                                    editor.putLong(TimerKunciLayar, timerBasedSchedule);
//                                    editor.putLong(TimerLayarHidup, 0);
//                                }
//                                if (timerBasedSchedule < pembatasKunciLayarBasedTime && kunciLayarBasedTime == 1) {
//                                    editor.putLong(TimerKunciLayar, timerBasedSchedule);
//                                }
//                                editor.apply();
//                            }
//
//                        }
//                        timerBasedSchedule++;
//                        Log.e(TAG, "run: timerBasedSchedule" + timerBasedSchedule + "basedScheduledLock : " + basedScheduleLock);
//                    }
//                    if (getCurrentApp || powerOff) {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                editor.putLong(TimerBasedSchedule, timerBasedSchedule);
//                                editor.apply();
//                            }
//                        }, 1500);
//                    }
//                    if (hour == basedScheduleFinishHour && minute >= basedScheduleFinishMinute) {
//                        Log.e(TAG, "baseScheduledLockPhone kunci layar telah nonaktif");
//                        editor.putInt(BasedScheduleLock, 0);
//                        editor.apply();
//                    }
//                    Log.e(TAG, "jam " + jam.get(Calendar.HOUR_OF_DAY) + ":" + jam.get(Calendar.MINUTE) + ":" + jam.get(Calendar.SECOND) + " timerBasedSchedule : " + timerBasedSchedule + " basedScheduleLock : " + basedScheduleLock);
//                    handler.postDelayed(this, 1000);
//                }
//            }
//        });
//    }

    public static void oneClickLockPhone(final Context context) {
        final SharedPreferences sp = context.getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final Handler handler = new Handler();
        editor.putInt(HadRestart, 0);
        editor.apply();
        handler.post(new Runnable() {
            //            long timerOneClickLock = sp.getLong(TimerOneClickLock, 0);
            final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

            @Override
            public void run() {
                final Calendar cal = Calendar.getInstance();
                String dateLockPhone2 = sp.getString(DateLockPhone2, "");
                int oneClickLock = sp.getInt(OneClickLock, 0);
                long oneClickFinishHour = sp.getLong(OneClickHourFinish, 0);
                long oneClickFinishMinute = sp.getLong(OneClickMinuteFinish, 0);
                long oneClickFinishSecond = sp.getLong(OneClickSecondFinish, 0);
                long oneClickFinishYear = sp.getLong(OneClickYear, 0);
                long oneClickFinishDayOfYear = sp.getLong(OneClickDayOfYear, 0);
                long oneClickFinishYearDate = (oneClickFinishYear * 1000) + oneClickFinishDayOfYear;

                long yearNow = cal.get(Calendar.YEAR);
                long dayOfYearNow = cal.get(Calendar.DAY_OF_YEAR);
                long yearDateNow = (yearNow * 1000) + dayOfYearNow;
                if (getCurrentApp && oneClickLock == 1) {
                    if (oneClickFinishMinute >= 60) {
                        oneClickFinishMinute = oneClickFinishMinute - 60;
                        oneClickFinishHour = oneClickFinishHour + 1;
                    }
                    if (oneClickFinishHour >= 24) {
                        if (!dateLockPhone2.equals(dateFormat.format(System.currentTimeMillis()))) {
                            oneClickFinishYearDate = yearDateNow;
                            oneClickFinishHour = oneClickFinishHour - 24;
                        }
                    }
                    long oneClickFinish = (oneClickFinishHour * 3600) + (oneClickFinishMinute * 60) + (oneClickFinishSecond);
                    long jamSekarang = (cal.get(Calendar.HOUR_OF_DAY) * 3600) + (cal.get(Calendar.MINUTE) * 60) + (cal.get(Calendar.SECOND));
                    Log.i(TAG, "lockTimeFinish " + oneClickFinish + " ::: " + oneClickFinishHour + ":" + oneClickFinishMinute + ":" + oneClickFinishSecond);
                    Log.i(TAG, "jamSekarang " + jamSekarang + " ::: " + (cal.get(Calendar.HOUR_OF_DAY)) + ":" + (cal.get(Calendar.MINUTE)) + ":" + (cal.get(Calendar.SECOND)));
                    if (((jamSekarang > oneClickFinish) && yearDateNow == oneClickFinishYearDate) || ((jamSekarang < oneClickFinish) && yearDateNow > oneClickFinishYearDate)) {
                        Log.w(TAG, "run: jamSekarang > oneClickFinish) && yearDateNow == oneClickFinishYearDate) || ((jamSekarang < oneClickFinish) && yearDateNow > oneClickFinishYearDate");
                        editor.putInt(OneClickLock, 0);
                        editor.apply();

                    } else {
                        Handler handler1 = new Handler();
                        Toast.makeText(context, "Kunci berdasarkan satu klik berjalan sampai " + oneClickFinishHour + ":" + oneClickFinishMinute + ":" + oneClickFinishSecond, Toast.LENGTH_SHORT).show();
                        getCurrentApp = false;
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Objects.requireNonNull(devicePolicyManager).lockNow();
                            }
                        }, 3000);
                    }
                }
//                if (!getCurrentApp && oneClickLock == 1 && !lockScreen) {
//                    if (timerOneClickLock < oneClickTimer) {
//                        timerOneClickLock++;
//                    }
//                    if (timerOneClickLock >= oneClickTimer) {
////                        editor.putLong(TimerLayarHidup, 0);
//                        editor.putLong(TimerOneClickLock, 0);
//                        editor.putInt(OneClickLock, 0);
//                        editor.apply();
//                    }
//                    Log.e(TAG, "run: oneClickLock : " + oneClickLock + " timerOneClickLock" + timerOneClickLock + " batasOneClickLock : " + oneClickTimer +
//                            "\n hadRestart : " + sp.getInt(HadRestart, 0));
//
//                }
//                if (getCurrentApp && oneClickLock == 1 || lockScreen && oneClickLock == 1)
//                if (oneClickLock == 1) {
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Objects.requireNonNull(devicePolicyManager).lockNow();
//                        }
//                    }, 3000);
//                }
                if (powerOff) {
                    Log.d(TAG, "run: ");
//                    editor.putLong(OneClickTimer, oneClickTimer);
//                    editor.putLong(TimerOneClickLock, timerOneClickLock);
                    editor.putInt(HadRestart, 1);
                    editor.apply();
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void openNotificationUsage(Context context, long seconds) {
        notificationManager = NotificationManagerCompat.from(context);

        String title = "UsageAdvisor - Waktu Penggunaan Mencapai : " + Helper.getDuration(seconds * 1000);
        String message = "";
        int colorselection = 0;

        if (seconds < 3600) {
            colorselection = ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary);
            message = "<b>\u2022 Tingkat durasi penggunaan :</b> Sangat aman <br> <b>\u2022 Rentang durasi penggunaan :</b> 0 menit - 1 jam";
        }
        if (seconds >= 3600) {
            colorselection = ContextCompat.getColor(context.getApplicationContext(), R.color.hijau);
            message = "<b>\u2022 Tingkat durasi penggunaan</b> : Aman <br> <b>\u2022 Rentang durasi penggunaan :</b> 1 jam - 2,5 jam ";
        }
        if (seconds >= 2.5 * 3600) {
            colorselection = ContextCompat.getColor(context.getApplicationContext(), R.color.kuning);
            message = "<b>\u2022 Tingkat durasi penggunaan :</b> Masih batas wajar <br> <b>\u2022 Rentang durasi penggunaan :</b> 2,5 jam - 4 jam ";
        }
        if (seconds >= 4 * 3600) {
            colorselection = ContextCompat.getColor(context.getApplicationContext(), R.color.red);
            message = "<b>\u2022 Tingkat durasi penggunaan :</b> Berbahaya <br> <b>\u2022 Rentang durasi penggunaan :</b> Lebih dari 4 jam ";
        }
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setColor(colorselection)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(message))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setDefaults(DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(2, notification);
    }

    private void updateLockSettingGrup(Context context) {
        Log.d(TAG, "updateLockSettingGrup: ");
        SharedPreferences sp = context.getSharedPreferences(VALUEID1, 0);
        SharedPreferences sp1 = context.getSharedPreferences(VALUEID2, 0);
        final SharedPreferences.Editor editor = sp1.edit();
                final DocumentReference docRef = firestore.collection("Groups").document(sp.getString(SharePref_id_group, ""));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long bsfh = Objects.requireNonNull(documentSnapshot.getLong("basedScheduleFinishHour"));
                    long bsfm = Objects.requireNonNull(documentSnapshot.getLong("basedScheduleFinishMinute"));
                    long bssh = Objects.requireNonNull(documentSnapshot.getLong("basedScheduleStartHour"));
                    long bssm = Objects.requireNonNull(documentSnapshot.getLong("basedScheduleStartMinute"));
                    long btfh = Objects.requireNonNull(documentSnapshot.getLong("basedTimeFinishHour"));
                    long btfm = Objects.requireNonNull(documentSnapshot.getLong("basedTimeFinishMinute"));
                    long btsh = Objects.requireNonNull(documentSnapshot.getLong("basedTimeStartHour"));
                    long btsm = Objects.requireNonNull(documentSnapshot.getLong("basedTimeStartMinute"));
                    long gbsl;
                    long gbtl;
                    long radio = Objects.requireNonNull(documentSnapshot.getLong("radio"));
                    String sbs = documentSnapshot.getString("switchBasedSchedule");
                    String sbt = documentSnapshot.getString("switchBasedTime");
                    long btsg = btsh + btsm;
                    long btfg = btfh + btfm;
                    long bssg = bssh + bssm;
                    long bsfg = bsfh + bsfm;

                    //algoritma menyesuaikan gbtl gbsl
                    if ((btsg > 0 && btfg > 0) && Objects.requireNonNull(sbt).equals("Aktif")) {
                        gbtl = 1;
                        docRef.update("grupBasedScheduleLock", 1);
                    } else {
                        gbtl = 0;
                        docRef.update("grupBasedScheduleLock", 0);
                    }
                    if (bssg != bsfg && Objects.requireNonNull(sbs).equals("Aktif")) {
                        gbsl = 1;
                        docRef.update("grupBasedScheduleLock", 1);
                    } else {
                        gbsl = 0;
                        docRef.update("grupBasedScheduleLock", 0);
                    }

                    editor.putInt(BasedScheduleFinishHourGrup, (int) bsfh);
                    editor.putInt(BasedScheduleFinishMinuteGrup, (int) bsfm);
                    editor.putInt(BasedScheduleStartHourGrup, (int) bssh);
                    editor.putInt(BasedScheduleStartMinuteGrup, (int) bssm);
                    editor.putInt(BasedTimeFinishHourGrup, (int) btfh);
                    editor.putInt(BasedTimeFinishMinuteGrup, (int) btfm);
                    editor.putInt(BasedTimeStartHourGrup, (int) btsh);
                    editor.putInt(BasedTimeStartMinuteGrup, (int) btsm);
                    editor.putInt(GrupBasedScheduleLock, (int) gbsl);
                    editor.putInt(GrupBasedTimeLock, (int) gbtl);
                    editor.putInt(RadioTimeBaseStateGrup, (int) radio);
                    editor.putString(SwitchBasedScheduleGrup, sbs);
                    editor.putString(SwitchBasedTimeGrup, sbt);
                    editor.apply();
                } else {
                    Log.e(TAG, "gagal updateLockSettingGrup");
                }
            }
        });
    }
}