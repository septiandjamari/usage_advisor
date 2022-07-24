package com.djamari.usageadvisor.otherActivity.DialogModal;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djamari.usageadvisor.R;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;


public class LockPhoneScheduleBasedFinish extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogScheduleFinish);
        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        int hour = (int) sp.getLong("basedScheduleFinishHour", 0);
        int minute = (int) sp.getLong("basedScheduleFinishMinute", 0);
        final TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String basedScheduleFinishString;
                MaterialButton basedScheduleFinish = requireActivity().findViewById(R.id.basedScheduleFinish);
                if (hourOfDay < 10 && minute < 10) {
                    basedScheduleFinishString = "0" + hourOfDay + ":0" + minute;
                } else if (hourOfDay < 10 && minute > 10) {
                    basedScheduleFinishString = "0" + hourOfDay + ":" + minute;
                } else if (hourOfDay > 10 && minute < 10) {
                    basedScheduleFinishString = hourOfDay + ":0" + minute;
                } else {
                    basedScheduleFinishString = hourOfDay + ":" + minute;
                }
                basedScheduleFinish.setText(basedScheduleFinishString);
                editor.putLong("basedScheduleFinishHour", hourOfDay);
                editor.putLong("basedScheduleFinishMinute", minute);
                editor.apply();
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
