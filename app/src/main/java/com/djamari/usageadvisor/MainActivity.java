package com.djamari.usageadvisor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.djamari.usageadvisor.otherActivity.DialogModal.BottonSheetDialogMenu;
import com.djamari.usageadvisor.otherActivity.Setting.SettingActivity;
import com.djamari.usageadvisor.service.ForegroundService;
import com.djamari.usageadvisor.ui.agenda.AgendaFragment;
import com.djamari.usageadvisor.ui.home.HomeFragment;
import com.djamari.usageadvisor.ui.lock_phone.LockPhoneFragment;
import com.djamari.usageadvisor.ui.member_usage.GroupUsageFragment00;
import com.djamari.usageadvisor.ui.member_usage.GroupUsageFragment10;
import com.djamari.usageadvisor.ui.personal_usage.PersonalUsageFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_akses_pengguna;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottonSheetDialogMenu.BottomSheetListener {

    @SuppressLint("SimpleDateFormat")
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //    private DrawerLayout drawerLayout;
    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("SimpleDateFormat")


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(VALUEID, 0);
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Silahkan Login", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        int kunciLayar, basedScheduleLock, oneClickLock;
        kunciLayar = sp.getInt(KunciLayar, 0);
        basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
        oneClickLock = sp.getInt(OneClickLock, 0);
        if (kunciLayar == 1 || basedScheduleLock == 1 || oneClickLock == 1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        MaterialButton bottomSheetCall = findViewById(R.id.button_bottom_sheet);

        bottomSheetCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottonSheetDialogMenu bottomSheet = new BottonSheetDialogMenu();
                bottomSheet.show(getSupportFragmentManager(), "bottomSheetDialogMenu");
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navlistener);

        String input = "Mohon jangan blokir bilah notifikasi ini";
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String input = "Mohon jangan blokir bilah notifikasi ini";
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
//
//        Intent broadcastIntent = new Intent(this, ServiceStarter.class);
//        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Tekan tombol BACK sekali lagi untuk keluar ", Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences(VALUEID1, 0);
                    String aksesPengguna = sp.getString(SharePref_akses_pengguna, "");
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_todo:
                            selectedFragment = new AgendaFragment();
                            break;
                        case R.id.navigation_personal_usage:
                            selectedFragment = new PersonalUsageFragment();
                            break;
                        case R.id.navigation_member_usage:
                            if (aksesPengguna.equals("Personal")) {
                                selectedFragment = new GroupUsageFragment00();
                            } else {
                                selectedFragment = new GroupUsageFragment10();
                            }
                            break;
                        case R.id.navigation_locking_phone:
                            selectedFragment = new LockPhoneFragment();
                            break;
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, Objects.requireNonNull(selectedFragment)).commit();

                    item.setChecked(true);
                    setTitle(item.getTitle());
//                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            };

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
