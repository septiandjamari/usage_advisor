package com.djamari.usageadvisor.ui.personal_usage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.AppUsageCollectionModel;
import com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.AppCollection.RecyclerViewAppUsageCollection;
import com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.AppCollection.SetterAppUsageCollection;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonalUsageDetailedFragment extends Fragment {
    private static final String TAG = PersonalUsageDetailedFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<SetterAppUsageCollection> listItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_personal_usage_detailed, container, false);

        recyclerView = root.findViewById(R.id.recyclerview_detailed_time);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        final MaterialButton tombolKembali = requireActivity().findViewById(R.id.tombol_detail_kembali);
        final FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        fabAgenda.setVisibility(View.GONE);

        tombolKembali.setVisibility(View.VISIBLE);
        tombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new PersonalUsageFragment()).commit();
            }
        });

        loadAppCollection(requireActivity().getApplicationContext());


        return root;
    }

    private void loadAppCollection(Context context) {
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
        List<AppUsageCollectionModel> getAllAppCollection = db.getAllAppCollection();
        for (AppUsageCollectionModel auc : getAllAppCollection) {
            try {
                if (!auc.getNama_aplikasi().equals("NULL") & systemApp.contains(auc.getNama_aplikasi())) {
                    PackageManager packageManager = requireActivity().getPackageManager();
                    Drawable icon;

                    icon = packageManager.getApplicationIcon(auc.getNama_aplikasi());
                    SetterAppUsageCollection listItem = new SetterAppUsageCollection(icon,
                            Helper.getAppNameFromPkgName(requireActivity(), auc.getNama_aplikasi()),
                            "Start : " + auc.getFirst_time_stamp(),
                            " - Finish : " + auc.getLast_time_stamp(),
                            "Durasi : " + auc.getDurasi_pakai());
                    listItems.add(listItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RecyclerViewAppUsageCollection adapter = new RecyclerViewAppUsageCollection(listItems, requireActivity());
        recyclerView.setAdapter(adapter);
    }
}
