package com.djamari.usageadvisor.ui.member_usage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.firebaseModel.Setter_group_user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_akses_pengguna;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class GroupUsageFragment02 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member_usage02, container, false);
        final LinearLayout layout_bottom = requireActivity().findViewById(R.id.layout_bottom);
        final LinearLayout progressBarMemberList = root.findViewById(R.id.progressBarMemberList);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);

        final TextInputEditText input_kode_grup = root.findViewById(R.id.input_kode_grup);
        MaterialButton proses_gabung_grup = root.findViewById(R.id.proses_gabung_grup);
        MaterialButton batal_gabung_grup = root.findViewById(R.id.batal_gabung_grup);

        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);
        final SharedPreferences.Editor editor = sp.edit();

        proses_gabung_grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarMemberList.setVisibility(View.VISIBLE);
                final String kode_grup = Objects.requireNonNull(input_kode_grup.getText()).toString();
                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                final String uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                String id_username = sp.getString(SharePref_id_username, "");
                String username = sp.getString(SharePref_username, "");
                final String akses_pengguna = "Member";
                final Setter_group_user groupMore = new Setter_group_user(id_username, username, akses_pengguna);
                final DocumentReference docRef = firestore.collection("Users").document(uId);
                DocumentReference docRef1 = firestore.collection("Groups").document(kode_grup);
                docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            firestore.collection("Groups")
                                    .document(kode_grup)
                                    .collection("Users")
                                    .document(uId)
                                    .set(groupMore).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    docRef.update("akses_pengguna", akses_pengguna);
                                    docRef.update("id_group", kode_grup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBarMemberList.setVisibility(View.VISIBLE);
                                            editor.putString(SharePref_id_group, kode_grup);
                                            editor.putString(SharePref_akses_pengguna, akses_pengguna);
                                            editor.apply();
                                            Toast.makeText(requireActivity().getApplicationContext(), "Berhasil bergabung ke grup!", Toast.LENGTH_SHORT).show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment10()).commit();
                                                }
                                            }, 1000);
                                        }
                                    });
                                }
                            });

                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Pastikan kode yang anda masukkan benar", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        batal_gabung_grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment00()).commit();
                layout_bottom.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

}
