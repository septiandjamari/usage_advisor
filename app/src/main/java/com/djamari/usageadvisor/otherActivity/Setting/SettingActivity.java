package com.djamari.usageadvisor.otherActivity.Setting;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.djamari.usageadvisor.MainActivity;
import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.service.AdminReceiver;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    public static final int RESULT_DEVICE_ADMINISTARTOR = 11;
    public static final int RESULT_APP_USAGE = 12;
    private static final String TAG = "SettingActivity";
    private static final Intent[] AUTO_START_INTENTS = {
            new Intent().setComponent(new ComponentName("com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
                    Uri.parse("mobilemanager://function/entry/AutoStart"))
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        MaterialToolbar toolbar = findViewById(R.id.settingActivityToolbar);
        toolbar.setTitle("Pengaturan");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(DEVICE_POLICY_SERVICE);
        Log.e(TAG, "onCreate: " + getApplicationContext().toString());
        final ComponentName compName = new ComponentName(this, AdminReceiver.class);

        final boolean deviceAdminActive = Objects.requireNonNull(devicePolicyManager).isAdminActive(compName);

        TextView aksesPenggunaanOn = findViewById(R.id.aksesPenggunaanOn);
        TextView aksesPenggunaanOff = findViewById(R.id.aksesPenggunaanOff);
        TextView administratorPerangkatOn = findViewById(R.id.administratorPerangkatOn);
        TextView administratorPerangkatOff = findViewById(R.id.administratorPerangkatOff);
        MaterialButton pemulaianOtomatis = findViewById(R.id.pemulaianOtomatis);

        MaterialButton infoAppUsage = findViewById(R.id.infoAppUsage);
        MaterialButton infoAutoStart = findViewById(R.id.infoAutoStart);
        MaterialButton infoDeviceAdministrator = findViewById(R.id.infoDeviceAdministrator);

        MaterialButton actionAppUsage = findViewById(R.id.actionAppUsage);
        MaterialButton actionAutoStart = findViewById(R.id.actionAutoStart);
        MaterialButton actionDeviceAdministrator = findViewById(R.id.actionDeviceAdministrator);

        if (Helper.getUsageStatsList(getApplicationContext()).isEmpty()) {
            aksesPenggunaanOn.setVisibility(View.GONE);
            aksesPenggunaanOff.setVisibility(View.VISIBLE);
        } else {
            aksesPenggunaanOn.setVisibility(View.VISIBLE);
            aksesPenggunaanOff.setVisibility(View.GONE);
        }

        if (deviceAdminActive) {
            administratorPerangkatOn.setVisibility(View.VISIBLE);
            administratorPerangkatOff.setVisibility(View.GONE);
        } else {
            administratorPerangkatOn.setVisibility(View.GONE);
            administratorPerangkatOff.setVisibility(View.VISIBLE);
        }

        pemulaianOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        infoAppUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog
            }
        });

        infoAutoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog
            }
        });

        infoDeviceAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog
            }
        });

        actionAppUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, RESULT_APP_USAGE);
            }
        });

        actionAutoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (Intent intent : AUTO_START_INTENTS) {
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            startActivity(intent);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        actionDeviceAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Tidak perlu mengaktikan administrasi perangkat...silahkan lewati saja", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
//                startActivityForResult(intent, RESULT_DEVICE_ADMINISTARTOR);
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        TextView aksesPenggunaanOn = findViewById(R.id.aksesPenggunaanOn);
        TextView aksesPenggunaanOff = findViewById(R.id.aksesPenggunaanOff);
        TextView administratorPerangkatOn = findViewById(R.id.administratorPerangkatOn);
        TextView administratorPerangkatOff = findViewById(R.id.administratorPerangkatOff);
        if (requestCode == RESULT_DEVICE_ADMINISTARTOR) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Anda telah mengaktikan fitur Administrator Perangkat", Toast.LENGTH_SHORT).show();
                administratorPerangkatOn.setVisibility(View.VISIBLE);
                administratorPerangkatOff.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Terjadi masalah ketika mengkatifkan fitur Administrator Perangkat", Toast.LENGTH_SHORT).show();
                administratorPerangkatOn.setVisibility(View.GONE);
                administratorPerangkatOff.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == RESULT_APP_USAGE) {
            if (!Helper.getUsageStatsList(getApplicationContext()).isEmpty()) {
                Toast.makeText(this, "Anda Telah Mengaktifkan fitur Akses Penggunaan", Toast.LENGTH_SHORT).show();
                aksesPenggunaanOn.setVisibility(View.VISIBLE);
                aksesPenggunaanOff.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Terjadi masalah ketika mengkatifkan fitur Akses Penggunaan", Toast.LENGTH_SHORT).show();
                aksesPenggunaanOn.setVisibility(View.GONE);
                aksesPenggunaanOff.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}