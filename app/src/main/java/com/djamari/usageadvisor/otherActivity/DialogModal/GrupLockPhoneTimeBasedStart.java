package com.djamari.usageadvisor.otherActivity.DialogModal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GrupLockPhoneTimeBasedStart extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogTimeFinish);
        final EditText btsh = requireActivity().findViewById(R.id.btsh);
        final EditText btsm = requireActivity().findViewById(R.id.btsm);
        int hour = Integer.parseInt(btsh.getText().toString());
        int minute = Integer.parseInt(btsm.getText().toString());

        final TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), R.style.AppThemeHolo, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                long basedTimeStartMillis = ((hourOfDay * 3600) + (minute * 60)) * 1000;
                String basedTimeStartString = Helper.getDuration(basedTimeStartMillis);
                MaterialButton basedTimeStartGrup = requireActivity().findViewById(R.id.basedTimeStartGrup);

                if (hourOfDay > 0 || minute > 0) {
                    basedTimeStartGrup.setText(basedTimeStartString);
                } else {
                    basedTimeStartGrup.setText("Belum Diatur");
                }
                btsh.setText(String.valueOf(hourOfDay));
                btsm.setText(String.valueOf(minute));
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
