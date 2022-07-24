package com.djamari.usageadvisor.otherActivity.DialogModal;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djamari.usageadvisor.R;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID2;

public class GrupLockPhoneScheduleBasedStart extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogScheduleStart);
        final EditText et_bssh = requireActivity().findViewById(R.id.bssh);
        final EditText et_bssm = requireActivity().findViewById(R.id.bssm);

        final EditText et_bsfh = requireActivity().findViewById(R.id.bsfh);
        final EditText et_bsfm = requireActivity().findViewById(R.id.bsfm);
        int hour = Integer.parseInt(et_bssh.getText().toString());
        int minute = Integer.parseInt(et_bssm.getText().toString());
        final TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                MaterialButton basedScheduleStartGrup = requireActivity().findViewById(R.id.basedScheduleStartGrup);
                MaterialButton basedScheduleFinishGrup = requireActivity().findViewById(R.id.basedScheduleFinishGrup);
                String bsfh, bsfm;

                if (hourOfDay < 10) {
                    bsfh = "0" + hourOfDay;
                } else {
                    bsfh = String.valueOf(hourOfDay);
                }
                if (minute < 10) {
                    bsfm = "0" + minute;
                } else {
                    bsfm = String.valueOf(minute);
                }
                basedScheduleStartGrup.setText(bsfh + ":" + bsfm);
                basedScheduleFinishGrup.setText(bsfh + ":" + bsfm);
                et_bssh.setText(String.valueOf(hourOfDay));
                et_bssm.setText(String.valueOf(minute));

                et_bsfh.setText(String.valueOf(hourOfDay));
                et_bsfm.setText(String.valueOf(minute));
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
