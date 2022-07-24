package com.djamari.usageadvisor.ui.member_usage;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UserList.RecyclerViewMemberList;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UserList.SetterMemberList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_akses_pengguna;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;


public class GroupUsageFragment10 extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sp1 = requireActivity().getSharedPreferences(VALUEID, 0);
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);
        String akses_pengguna = sp.getString("sharePref_akses_pengguna", "");
        String id_grup = sp.getString(SharePref_id_group, "");
        String id_user = sp.getString(SharePref_id_username, "");

        final View root = inflater.inflate(R.layout.fragment_member_usage10, container, false);
        final LinearLayout layout_bottom = requireActivity().findViewById(R.id.layout_bottom);
        final LinearLayout progressBarMemberList = root.findViewById(R.id.progressBarMemberList);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        TextView Id_grup = root.findViewById(R.id.id_grup);
        TextView Akses_pengguna = root.findViewById(R.id.akses_pengguna);

        progressBarMemberList.setVisibility(View.VISIBLE);
        Id_grup.setText("ID Grup : " + id_grup);
        Akses_pengguna.setText("ID User : " + id_user + "(" + sp.getString(SharePref_akses_pengguna, "") + ")");
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);

        final MaterialButton member_usage_add_member = root.findViewById(R.id.member_usage_add_member);
        final MaterialButton member_usage_lock_setting = root.findViewById(R.id.member_usage_lock_setting);
        member_usage_add_member.setEnabled(false);
        member_usage_lock_setting.setEnabled(false);


        if (!akses_pengguna.equals("Admin")) {
            member_usage_add_member.setVisibility(View.GONE);
            member_usage_lock_setting.setVisibility(View.GONE);
        }
        member_usage_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment11()).commit();
            }
        });
        member_usage_lock_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment12()).commit();
            }
        });
        int basedTimeLock = sp.getInt(KunciLayar, 0);
        int basedScheduleLock = sp.getInt(BasedScheduleLock, 0);
//        if ((basedScheduleLock != 1 || basedTimeLock != 1)) {
//            Log.i("TAG", "returned: " + id_grup);
//            CollectionReference collRef = firestore.collection("Groups").document(id_grup).collection("Users");
//            collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                final List<SetterMemberList> listItems = new ArrayList<>();
//
//                List<String> list_user_id = new ArrayList<>();
//
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                            Log.d("GroupUsage", document.getId() + "=>" + document.getData());
//                            list_user_id.add(document.getId());
//                        }
//                        for (String listUser : list_user_id) {
//                            Log.e("GroupUsage", listUser);
//                            final RecyclerView recyclerView = root.findViewById(R.id.recyclerview_member_list);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
//                            firestore.collection("Users").document(listUser)
//                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        Log.e("GroupUsage", "onSuccess: Berhasil");
//                                        String userID = documentSnapshot.getId();
//                                        Log.d("TAG", "useList : "+userID);
//                                        String username = documentSnapshot.getString("username");
//                                        int durasi_layar = Objects.requireNonNull(documentSnapshot.getLong("durasi_layar")).intValue();
//                                        int buka_kunci = Objects.requireNonNull(documentSnapshot.getLong("buka_kunci")).intValue();
//                                        int interaksi_aplikasi = Objects.requireNonNull(documentSnapshot.getLong("interaksi_aplikasi")).intValue();
//                                        String terakhir_aktif = documentSnapshot.getString("terakhir_aktif");
//
//                                        SetterMemberList listItem = new SetterMemberList(userID, username, durasi_layar, buka_kunci, interaksi_aplikasi, terakhir_aktif);
//                                        listItems.add(listItem);
//                                        RecyclerViewMemberList adapter = new RecyclerViewMemberList(listItems, requireActivity());
//                                        recyclerView.setAdapter(adapter);
//                                        member_usage_add_member.setEnabled(true);
//                                        member_usage_lock_setting.setEnabled(true);
//                                        progressBarMemberList.setVisibility(View.GONE);
//                                        layout_bottom.setVisibility(View.VISIBLE);
//                                    } else {
//                                        member_usage_add_member.setEnabled(true);
//                                        member_usage_lock_setting.setEnabled(true);
//                                        progressBarMemberList.setVisibility(View.GONE);
//                                        layout_bottom.setVisibility(View.VISIBLE);
//                                        Toast.makeText(requireActivity().getApplicationContext(), "Gagal Memuat grup", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//        }
        return root;
    }
}
