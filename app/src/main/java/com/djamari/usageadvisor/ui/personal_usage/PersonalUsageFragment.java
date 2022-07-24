package com.djamari.usageadvisor.ui.personal_usage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.DaftarAplikasiModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.DaftarAplikasi.RecyclerViewDaftarAplikasi;
import com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.DaftarAplikasi.SetterDaftarAplikasi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonalUsageFragment extends Fragment {

    private TextView totalTimeView;
    private static final String TAG = Helper.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<SetterDaftarAplikasi> listItems = new ArrayList<>();

//    DaftarAplikasi db = new DaftarAplikasi(getContext());

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onStart() {
        super.onStart();
        if (Helper.getUsageStatsList(requireActivity()).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_personal_usage, container, false);

        final MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        final FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        tombolKembalidetailed.setVisibility(View.GONE);
        fabAgenda.setVisibility(View.GONE);

        totalTimeView = root.findViewById(R.id.waktu_penggunaan_personal);
        recyclerView = root.findViewById(R.id.recyclerview_daftar_aplikasi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        MaterialButton buttonKembali = root.findViewById(R.id.koleksi_penguunaan);
        buttonKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new PersonalUsageDetailedFragment()).commit();
            }
        });

        loadDaftarAplikasi(requireActivity().getApplicationContext());

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void loadDaftarAplikasi(Context context) {
        PackageManager pm = requireActivity().getPackageManager();
        Intent intentCatSys = new Intent(Intent.ACTION_MAIN, null);
        intentCatSys.addCategory(Intent.CATEGORY_LAUNCHER);


        List<ResolveInfo> apps = pm.queryIntentActivities(intentCatSys, 0);
        List<String> systemApp = new ArrayList<>();
        for (ResolveInfo resolveInfo : apps) {
            String packagesName = resolveInfo.activityInfo.packageName;
            Log.d(TAG, "Package Name " + packagesName);
            systemApp.add(packagesName);

        }

        PersonalUsageDatabase db = new PersonalUsageDatabase(context);
        List<DaftarAplikasiModel> getAllDaftarAplikasi = db.getAllDaftarAplikasi();
        long totaltime = 0;
        int frekuensi = 0;
        for (DaftarAplikasiModel dam : getAllDaftarAplikasi) {
            try {
                if (!dam.getNama_aplikasi().equals("NULL") & systemApp.contains(dam.getNama_aplikasi())) {
                    PackageManager packageManager = requireActivity().getPackageManager();
                    Drawable icon;

                    icon = packageManager.getApplicationIcon(dam.getNama_aplikasi());
                    SetterDaftarAplikasi listItem = new SetterDaftarAplikasi(icon,
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
        }
        RecyclerViewDaftarAplikasi adapter = new RecyclerViewDaftarAplikasi(listItems, requireActivity());
        recyclerView.setAdapter(adapter);
        totalTimeView.setText("Total interaksi dengan aplikasi : " + "\n\nDurasi : " + Helper.getDuration(totaltime) + "\nFrekuensi : " + frekuensi);
    }
}
