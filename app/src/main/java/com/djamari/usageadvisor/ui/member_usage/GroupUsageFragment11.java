package com.djamari.usageadvisor.ui.member_usage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.firebaseModel.Setter_group_user;
import com.djamari.usageadvisor.firebaseModel.Setter_user_more;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class GroupUsageFragment11 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SharedPreferences sp = requireActivity().getApplicationContext().getSharedPreferences(VALUEID1, 0);
        View root = inflater.inflate(R.layout.fragment_member_usage11, container, false);
        final LinearLayout layout_bottom = requireActivity().findViewById(R.id.layout_bottom);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);

        final TextInputEditText masukkan_id_user = root.findViewById(R.id.masukkan_id_user);
        MaterialButton proses_add_user = root.findViewById(R.id.proses_add_user);
        MaterialButton batal_add_user = root.findViewById(R.id.batal_add_user);

        proses_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input_id_user = masukkan_id_user.getText().toString();
                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("ID_Username").document(input_id_user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.e("addUser", "onSuccess: " + input_id_user);
                            final String userID = documentSnapshot.getString("userID");
                            final DocumentReference docRef = firestore.collection("Users")
                                    .document(Objects.requireNonNull(userID));
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String documentSnapshot1 = documentSnapshot.getId();
                                        final String group_id = sp.getString(SharePref_id_group, "");
                                        Log.e("addUser", "onSuccess1: " + userID + "---" + group_id);
                                        final String id_group = documentSnapshot.getString("id_group");
                                        final String akses_pengguna = "Member";
                                        String username = documentSnapshot.getString("username");
                                        if (id_group.equals("")) {
                                            final Setter_group_user setter_group_user = new Setter_group_user(input_id_user, username, akses_pengguna);
                                            firestore.collection("Groups")
                                                    .document(group_id)
                                                    .collection("Users")
                                                    .document(documentSnapshot1)
                                                    .set(setter_group_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    docRef.update("akses_pengguna", akses_pengguna);
                                                    docRef.update("id_group", group_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment10()).commit();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(requireActivity().getApplicationContext(), "Terjadi kesalahan saat mengupdate data pengguna : " + e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(requireActivity().getApplicationContext(), "Terjadi kesalahan saat penambahan pengguna ke database grup : " + e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(requireActivity().getApplicationContext(), "User yang anda masukkan sudah terhubung dengan grup!!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(requireActivity().getApplicationContext(), "Id yang anda masukkan telah benar tetapi tidak bisa menemukan user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Id yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        batal_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment10()).commit();
                layout_bottom.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

}
