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

public class GrupLockPhoneTimeBasedFinish extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogTimeStart);
        final EditText btfh = requireActivity().findViewById(R.id.btfh);
        final EditText btfm = requireActivity().findViewById(R.id.btfm);
        int hour = Integer.parseInt(btfh.getText().toString());
        int minute = Integer.parseInt(btfm.getText().toString());
        final TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), R.style.AppThemeHolo, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long basedTimeFinishMillis = ((hourOfDay * 3600) + (minute * 60)) * 1000;
                String basedTimeFinishString = Helper.getDuration(basedTimeFinishMillis);
                MaterialButton basedTimeFinishGrup = requireActivity().findViewById(R.id.basedTimeFinishGrup);

                if (hourOfDay > 0 || minute > 0) {
                    basedTimeFinishGrup.setText(basedTimeFinishString);
                } else {
                    basedTimeFinishGrup.setText("Belum di atur");
                }
                btfh.setText(String.valueOf(hourOfDay));
                btfm.setText(String.valueOf(minute));
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
