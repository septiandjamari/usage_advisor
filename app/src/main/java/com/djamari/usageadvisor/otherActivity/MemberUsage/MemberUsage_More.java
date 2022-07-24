package com.djamari.usageadvisor.otherActivity.MemberUsage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore.RecyclerViewUserDaftarAplikasi;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore.RecyclerViewUserKoleksiAplikasi;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore.SetterUserDaftarAplikasi;
import com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore.SetterUserKoleksiAplikasi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.djamari.usageadvisor.broadcastReceiver.OverallScreenService.dateFormat;

public class MemberUsage_More extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collRef;
    private DocumentReference docRef;
    RecyclerView recyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_usage_more);

        MaterialToolbar toolbar = findViewById(R.id.member_usage_more_toolbar);
        TextView title = findViewById(R.id.member_usage_more_title);
        Intent i = getIntent();
        String usageMoreState = i.getStringExtra("UsageMoreState");
        final String usernameMember = i.getStringExtra("UsernameMember");
        final String userID = i.getStringExtra("UserID");
        Log.i("TAG", "usageMoreState : " + usageMoreState);
        Log.i("TAG", "usernameMember : " + usernameMember);
        Log.i("TAG", "usernameMember : " + userID);
        toolbar.setTitle(usernameMember);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<SetterUserKoleksiAplikasi> listItems = new ArrayList<>();
        final List<SetterUserDaftarAplikasi> listItems1 = new ArrayList<>();
        recyclerView = findViewById(R.id.member_usage_more_recycler_appColection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        assert usageMoreState != null;
        if (usageMoreState.equals("koleksiAplikasi")) {
            title.setText("Koleksi Waktu :");
            docRef = firestore.collection("AppUsage").document(userID).collection("AppCollection").document(dateFormat.format(System.currentTimeMillis()));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        collRef = firestore.collection("AppUsage").document(userID).collection("AppCollection").document(dateFormat.format(System.currentTimeMillis())).collection("jam_pakai");
                        collRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int no = 0;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Log.d("GroupUsage", documentSnapshot.getId() + "=>" + documentSnapshot.getData());
                                    String namaAplikasi = documentSnapshot.getString("nama_aplikasi");
                                    String durasi = documentSnapshot.getString("durasiPemakaian");
                                    String start = documentSnapshot.getString("firstTimeStamp");
                                    String finish = documentSnapshot.getString("lastTimeStamp");
                                    if (!namaAplikasi.equals("NULL")) {
                                        no++;
                                        SetterUserKoleksiAplikasi listItem = new SetterUserKoleksiAplikasi(no, namaAplikasi, durasi, start, finish);
                                        listItems.add(listItem);
                                    }
                                    RecyclerViewUserKoleksiAplikasi adapter = new RecyclerViewUserKoleksiAplikasi(listItems, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak ada satupun data 'Koleksi Aplikasi' untuk : " + usernameMember, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        } else if (usageMoreState.equals("daftarAplikasi")) {
            title.setText("Daftar Aplikasi :");
            docRef = firestore.collection("AppUsage").document(userID).collection("DaftarAplikasi").document(dateFormat.format(System.currentTimeMillis()));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        collRef = firestore.collection("AppUsage").document(userID).collection("DaftarAplikasi").document(dateFormat.format(System.currentTimeMillis())).collection("AppName");
                        collRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int no = 0;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String namaAplikasi = documentSnapshot.getId();
                                    Log.d("TAG", namaAplikasi + "=>" + documentSnapshot.getData());
                                    String terakhir_dipakai = documentSnapshot.getString("terakhir_dipakai");
                                    long durasi_pakai = documentSnapshot.getLong("durasi_pakai");
                                    long frekuensi_pakai = documentSnapshot.getLong("frekuensi_pakai");
                                    if (!namaAplikasi.equals("NULL")) {
                                        no++;
                                        SetterUserDaftarAplikasi listItem = new SetterUserDaftarAplikasi(no, namaAplikasi, terakhir_dipakai, durasi_pakai, frekuensi_pakai);
                                        listItems1.add(listItem);
                                    }
                                    RecyclerViewUserDaftarAplikasi adapter = new RecyclerViewUserDaftarAplikasi(listItems1, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak ada satupun data 'Daftar Aplikasi' untuk : " + usernameMember, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
