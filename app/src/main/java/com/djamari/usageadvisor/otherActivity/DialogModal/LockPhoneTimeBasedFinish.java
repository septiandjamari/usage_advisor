package com.djamari.usageadvisor.otherActivity.DialogModal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;


public class LockPhoneTimeBasedFinish extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogTimeStart);
        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        int hour = (int) sp.getLong("basedTimeFinishHour", 0);
        int minute = (int) sp.getLong("basedTimeFinishMinute", 0);
        final TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), R.style.AppThemeHolo, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long basedTimeFinishMillis = ((hourOfDay * 3600) + (minute * 60)) * 1000;
                String basedTimeFinishString = Helper.getDuration(basedTimeFinishMillis);
                MaterialButton basedTimeFinish = requireActivity().findViewById(R.id.basedTimeFinish);
                if (hourOfDay > 0 || minute > 0) {
                    basedTimeFinish.setText(basedTimeFinishString);
                } else {
                    basedTimeFinish.setText("Belum di atur");
                }
                editor.putLong("basedTimeFinishHour", hourOfDay);
                editor.putLong("basedTimeFinishMinute", minute);
                editor.apply();
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
