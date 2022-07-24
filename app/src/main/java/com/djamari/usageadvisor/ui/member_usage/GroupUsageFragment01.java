package com.djamari.usageadvisor.ui.member_usage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.djamari.usageadvisor.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupUsageFragment01 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member_usage01, container, false);
        final LinearLayout layout_bottom = requireActivity().findViewById(R.id.layout_bottom);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);

        MaterialButton buat_grup_confirm = root.findViewById(R.id.buat_grup_confirm);
        MaterialButton buat_grup_batal = root.findViewById(R.id.buat_grup_batal);

        buat_grup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        buat_grup_batal.setOnClickListener(new View.OnClickListener() {
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
