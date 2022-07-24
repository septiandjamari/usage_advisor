package com.djamari.usageadvisor.ui.member_usage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.djamari.usageadvisor.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class GroupUsageFragment00 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member_usage00, container, false);
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);
        String id_pengguna = sp.getString(SharePref_id_username, "");

        TextView personal_id_pengguna = root.findViewById(R.id.personal_id_pengguna);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);

        MaterialButton personal_buat_grup = root.findViewById(R.id.personal_buat_grup);
        MaterialButton personal_gabung_grup = root.findViewById(R.id.personal_gabung_grup);

        personal_id_pengguna.setText(id_pengguna);
        personal_buat_grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Menu di nonaktifkan untuk keperluan penelitian", Toast.LENGTH_SHORT).show();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment01()).commit();
            }
        });
        personal_gabung_grup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Menu di nonaktifkan untuk keperluan penelitian", Toast.LENGTH_SHORT).show();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment02()).commit();
            }
        });
        return root;
    }

}
