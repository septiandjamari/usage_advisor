package com.djamari.usageadvisor.ui.fragment_signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.djamari.usageadvisor.MainActivity;
import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.firebaseModel.Setter_ID_Username;
import com.djamari.usageadvisor.firebaseModel.Setter_user_more;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_akses_pengguna;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_email;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_password;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;


public class Fragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);
        final SharedPreferences.Editor editor = sp.edit();

        final String username_daftar = sp.getString(SharePref_username, "");
        final String email_daftar = sp.getString(SharePref_email, "");
        final String password_daftar = sp.getString(SharePref_password, "");
        final View root = inflater.inflate(R.layout.fragment_signup_2, container, false);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final LinearLayout progressBar = requireActivity().findViewById(R.id.progressBar1);
        MaterialButton role_pribadi = root.findViewById(R.id.role_pribadi);
        MaterialButton role_kelompok = root.findViewById(R.id.role_kelompok);
        MaterialButton kembali_fragment_2 = root.findViewById(R.id.kembali_fragment_2);

        role_pribadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(email_daftar, password_daftar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    String randomIdGroup = "";
                    String akses_pengguna = "Personal";
                    String pre_id_username = sp.getString("sharePref_id_username", "");
                    String id_username = username_daftar + "_" + pre_id_username;

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            final String userId = Objects.requireNonNull(user).getUid();
                            final Setter_user_more userMore = new Setter_user_more(id_username, username_daftar, email_daftar, akses_pengguna, randomIdGroup, 0, 0, 0);
                            Setter_ID_Username setter_id_username = new Setter_ID_Username(userId);
                            firestore.collection("ID_Username")
                                    .document(id_username)
                                    .set(setter_id_username).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firestore.collection("Users")
                                            .document(userId)
                                            .set(userMore).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(requireActivity().getApplicationContext(), "Proses pendaftaran berhasil", Toast.LENGTH_LONG).show();
                                                Toast.makeText(requireActivity().getApplicationContext(), "Anda Telah Login!", Toast.LENGTH_SHORT).show();
                                                editor.putString(SharePref_id_username, id_username);
                                                editor.putString(SharePref_username, username_daftar);
                                                editor.putString(SharePref_id_group, randomIdGroup);
                                                editor.putString(SharePref_akses_pengguna, akses_pengguna);
                                                editor.apply();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }, 1500);
                                            } else {
                                                Toast.makeText(requireActivity().getApplicationContext(), "Proses pendaftaran gagal, mohon coba lagi", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Mungkin email yang anda masukkan sudah terdaftar sebelumnya", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.fragment_signup, new Fragment1()).commit();
                        }
                    }
                });
            }
        });

        role_kelompok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Role untuk kelompok sementara ini di nonaktifkan...", Toast.LENGTH_SHORT).show();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.fragment_signup, new Fragment3()).commit();
            }
        });

        kembali_fragment_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.fragment_signup, new Fragment1()).commit();
            }
        });
        return root;
    }
}
