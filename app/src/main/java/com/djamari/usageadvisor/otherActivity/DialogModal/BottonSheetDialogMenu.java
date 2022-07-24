package com.djamari.usageadvisor.otherActivity.DialogModal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class BottonSheetDialogMenu extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet, container, false);
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);


        TextView tvUsername = root.findViewById(R.id.username_bottom_sheet);
        MaterialButton close = root.findViewById(R.id.button_bottom_sheet_close);
        MaterialButton signout = root.findViewById(R.id.drawer_sign_out);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        tvUsername.setText(sp.getString("sharePref_username", ""));
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent refresh = Objects.requireNonNull(requireActivity()).getIntent();
                requireActivity().finish();
                startActivity(refresh);
                Toast.makeText(requireActivity(), "Proses Sign Out Berhasil", Toast.LENGTH_LONG).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Bottom sheet closed");
                dismiss();
            }
        });


        return root;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
