package com.djamari.usageadvisor.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.DaftarAplikasiModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.recyclerViewAdapter.Home.RecyclerViewInteraksi;
import com.djamari.usageadvisor.recyclerViewAdapter.Home.SetterInteraksi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import static com.djamari.usageadvisor.helper.SharePref_keys.LayarHidup;
import static com.djamari.usageadvisor.helper.SharePref_keys.StatusPerubahanInteraksi;
import static com.djamari.usageadvisor.helper.SharePref_keys.UnlockScreen;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.service.ForegroundService.TAG;

public class HomeFragment extends Fragment {
    private TextView getDuration, getFrequency, tvdurasiInteraksi, tvfrekuensiInteraksi;
    private long layarHidup, unlockScreen;
    private RecyclerView recyclerView;
    private List<SetterInteraksi> listItems = new ArrayList<>();


    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
            switch (key) {
                case LayarHidup:
                    layarHidup = sp.getLong("layarHidup", 0);
                    getDuration.setText(Helper.getDuration(layarHidup * 1000));
                    break;
                case UnlockScreen:
                    unlockScreen = sp.getLong("unlockScreen", 0);
                    getFrequency.setText(unlockScreen + " kali");
                    break;
                case StatusPerubahanInteraksi:
                    listItems.clear();
                    statusInteraksi(requireActivity().getApplicationContext());
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final ImageView imageView = root.findViewById(R.id.imageViewHeroHome);
//        ViewTreeObserver vto = imageView.getViewTreeObserver();
//
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int layoutWidth = imageView.getMeasuredWidth();
//                int layoutHeight = imageView.getMeasuredHeight();
//                View v = root.findViewById(R.id.blankViewHome);
//                ViewGroup.LayoutParams params = v.getLayoutParams();
//                params.height = layoutHeight;
//                params.width = layoutWidth;
//                v.setLayoutParams(params);
//            }
//        });
        final MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        final FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        tombolKembalidetailed.setVisibility(View.GONE);
        fabAgenda.setVisibility(View.GONE);

        getDuration = root.findViewById(R.id.getDuration);
        getFrequency = root.findViewById(R.id.getlockFrequency);
        tvdurasiInteraksi = root.findViewById(R.id.durasiInteraksi);
        tvfrekuensiInteraksi = root.findViewById(R.id.frekuensiInteraksi);
        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);

        layarHidup = sp.getLong(LayarHidup, 0);
        getDuration.setText(Helper.getDuration(layarHidup * 1000));
//        textView.setText(" " + Build.MANUFACTURER + " " + Build.MODEL + "\n" + "Waktu layar : " + "Layar Hidup" + Helper.getDuration(layarHidup * 1000));

        unlockScreen = sp.getLong(UnlockScreen, 0);
        getFrequency.setText(unlockScreen + " kali");

        recyclerView = root.findViewById(R.id.recyclerviewInteraksiAplikasi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        statusInteraksi(requireActivity().getApplicationContext());

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void statusInteraksi(Context context) {
        PackageManager pm = requireActivity().getPackageManager();
        Intent intentCatSys = new Intent(Intent.ACTION_MAIN, null);
        intentCatSys.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = pm.queryIntentActivities(intentCatSys, 0);
        final List<String> systemApp = new ArrayList<>();
        for (ResolveInfo resolveInfo : apps) {
            String packagesName = resolveInfo.activityInfo.packageName;
//            Log.d(TAG, "Package Name " + packagesName);
            systemApp.add(packagesName);

        }

        PersonalUsageDatabase db = new PersonalUsageDatabase(requireActivity().getApplicationContext());
        final List<DaftarAplikasiModel> getAllDaftarAplikasi = db.daftarInteraksiAplikasi();
        long totaltime = 0;
        int frekuensi = 0;
        for (DaftarAplikasiModel dam : getAllDaftarAplikasi) {
            try {
                if (!dam.getNama_aplikasi().equals("NULL") & systemApp.contains(dam.getNama_aplikasi())) {
                    PackageManager packageManager = requireActivity().getPackageManager();
                    Drawable icon;

                    icon = packageManager.getApplicationIcon(dam.getNama_aplikasi());
                    SetterInteraksi listItem = new SetterInteraksi(icon,
                            Helper.getAppNameFromPkgName(requireActivity(), dam.getNama_aplikasi()),
                            "Durasi : " + Helper.getDuration(dam.getDurasi_pakai()),
                            "Frekuensi : " + dam.getFrekuensi_pakai() + " Kali",
                            "Terakhir dipakai : " + dam.getTerakhir_dilihat());
                    listItems.add(listItem);
                    totaltime = totaltime + dam.getDurasi_pakai();
                    frekuensi = frekuensi + dam.getFrekuensi_pakai();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            RecyclerViewInteraksi adapter = new RecyclerViewInteraksi(listItems, context);
            recyclerView.setAdapter(adapter);
        }
        final List<DaftarAplikasiModel> getAllDaftarAplikasi1 = db.getAllDaftarAplikasi();

        long totaltime1 = 0;
        int frekuensi1 = 0;
        for (DaftarAplikasiModel dam : getAllDaftarAplikasi1) {
            try {
                if (!dam.getNama_aplikasi().equals("NULL") & systemApp.contains(dam.getNama_aplikasi())) {
                    totaltime1 = totaltime1 + dam.getDurasi_pakai();
                    frekuensi1 = frekuensi1 + dam.getFrekuensi_pakai();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvdurasiInteraksi.setText(Helper.getDuration(totaltime1));
            tvfrekuensiInteraksi.setText(frekuensi1 + " kali");
        }
    }
}