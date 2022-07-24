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


public class LockPhoneTimeBasedStart extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_title, null);
        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialogTimeFinish);
        final SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        int hour = (int) sp.getLong("basedTimeStartHour",0);
        int minute = (int) sp.getLong("basedTimeStartMinute", 0);

        final TimePickerDialog mTimePicker=new TimePickerDialog(getActivity(), R.style.AppThemeHolo , new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                long basedTimeStartMillis = ((hourOfDay * 3600) + (minute * 60)) * 1000;
                String basedTimeStartString = Helper.getDuration(basedTimeStartMillis);
                MaterialButton basedTimeStart = requireActivity().findViewById(R.id.basedTimeStart);
                if (hourOfDay > 0 || minute > 0) {
                    basedTimeStart.setText(basedTimeStartString);
                } else {
                    basedTimeStart.setText("Belum Diatur");
                }
                editor.putLong("basedTimeStartHour", hourOfDay);
                editor.putLong("basedTimeStartMinute", minute);
                editor.apply();
            }
        }, hour, minute, true);
        mTimePicker.setCustomTitle(v);
        return mTimePicker;
    }
}
