package com.djamari.usageadvisor.ui.member_usage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.broadcastReceiver.OverallScreenService;
import com.djamari.usageadvisor.firebaseModel.SetterInfoTime;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.otherActivity.DialogModal.GrupLockPhoneScheduleBasedFinish;
import com.djamari.usageadvisor.otherActivity.DialogModal.GrupLockPhoneScheduleBasedStart;
import com.djamari.usageadvisor.otherActivity.DialogModal.GrupLockPhoneTimeBasedFinish;
import com.djamari.usageadvisor.otherActivity.DialogModal.GrupLockPhoneTimeBasedStart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.djamari.usageadvisor.broadcastReceiver.OverallScreenService.dateFormat;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedTimeLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.RadioTimeBaseStateGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.SwitchBasedScheduleGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.SwitchBasedTimeGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID2;

public class GroupUsageFragment12 extends Fragment {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID2, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final SharedPreferences sp1 = requireActivity().getSharedPreferences(VALUEID1, 0);
        final SharedPreferences sp2 = requireActivity().getSharedPreferences(VALUEID, 0);
//        final SharedPreferences.Editor editor = sp.edit();

        final View root = inflater.inflate(R.layout.fragment_member_usage12, container, false);
        final LinearLayout layout_bottom = requireActivity().findViewById(R.id.layout_bottom);
        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        layout_bottom.setVisibility(View.GONE);
        fabAgenda.setVisibility(View.GONE);
        tombolKembalidetailed.setVisibility(View.GONE);
//==================================================================================================

        final MaterialButton basedTimeStartGrup = root.findViewById(R.id.basedTimeStartGrup);
        final MaterialButton basedTimeFinishGrup = root.findViewById(R.id.basedTimeFinishGrup);
        final SwitchMaterial switchBasedTimeGrup = root.findViewById(R.id.switchTimeBasedGrup);

        final MaterialButton basedScheduleStartGrup = root.findViewById(R.id.basedScheduleStartGrup);
        final MaterialButton basedScheduleFinishGrup = root.findViewById(R.id.basedScheduleFinishGrup);
        final SwitchMaterial switchBasedScheduleGrup = root.findViewById(R.id.switchScheduleBasedGrup);

        final RadioGroup radioGroup1 = root.findViewById(R.id.radiobasedSceduleLockPhoneGrup);
        final RadioButton radio1Grup = root.findViewById(R.id.radiobaseSchedule1Grup);
        final RadioButton radio2Grup = root.findViewById(R.id.radiobaseSchedule2Grup);

        int kunciLayar = sp2.getInt(KunciLayar, 0);
        int basedScheduleLock = sp2.getInt(BasedScheduleLock, 0);
        if (kunciLayar == 1) {
            basedTimeStartGrup.setEnabled(false);
            basedTimeFinishGrup.setEnabled(false);
            switchBasedTimeGrup.setEnabled(false);
            radioGroup1.setEnabled(false);
            radio1Grup.setEnabled(false);
            radio2Grup.setEnabled(false);
        }
        if (basedScheduleLock == 1) {
            basedScheduleStartGrup.setEnabled(false);
            basedScheduleFinishGrup.setEnabled(false);
            switchBasedScheduleGrup.setEnabled(false);
        }

        final EditText btsh = root.findViewById(R.id.btsh);
        final EditText btsm = root.findViewById(R.id.btsm);
        final EditText bssh = root.findViewById(R.id.bssh);
        final EditText bssm = root.findViewById(R.id.bssm);
        final EditText btfh = root.findViewById(R.id.btfh);
        final EditText btfm = root.findViewById(R.id.btfm);
        final EditText bsfh = root.findViewById(R.id.bsfh);
        final EditText bsfm = root.findViewById(R.id.bsfm);
        final EditText sbt = root.findViewById(R.id.sbt);
        final EditText sbs = root.findViewById(R.id.sbs);
        final EditText rs = root.findViewById(R.id.radiostate);

        btsh.setText(String.valueOf(sp.getInt(BasedTimeStartHourGrup, 0)));
        btsm.setText(String.valueOf(sp.getInt(BasedTimeStartMinuteGrup, 0)));
        bssh.setText(String.valueOf(sp.getInt(BasedScheduleStartHourGrup, 0)));
        bssm.setText(String.valueOf(sp.getInt(BasedScheduleStartMinuteGrup, 0)));
        btfh.setText(String.valueOf(sp.getInt(BasedTimeFinishHourGrup, 0)));
        btfm.setText(String.valueOf(sp.getInt(BasedTimeFinishMinuteGrup, 0)));
        bsfh.setText(String.valueOf(sp.getInt(BasedScheduleFinishHourGrup, 0)));
        bsfm.setText(String.valueOf(sp.getInt(BasedScheduleFinishMinuteGrup, 0)));
        sbt.setText(sp.getString(SwitchBasedTimeGrup, ""));
        sbs.setText(sp.getString(SwitchBasedScheduleGrup, ""));
        rs.setText(String.valueOf(sp.getInt(RadioTimeBaseStateGrup, 0)));


//==================================================================================================

        long basedTimeStartHour = Long.parseLong(btsh.getText().toString());
        long basedTimeStartMinute = Long.parseLong(btsm.getText().toString());
        long basedTimeStartMillis = ((basedTimeStartHour * 3600) + (basedTimeStartMinute * 60)) * 1000;
        String basedTimeStartString = Helper.getDuration(basedTimeStartMillis);
        if (basedTimeStartHour > 0 || basedTimeStartMinute > 0) {
            basedTimeStartGrup.setText(basedTimeStartString);
        } else {
            basedTimeStartGrup.setText("Belum Diatur");
        }

        basedTimeStartGrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new GrupLockPhoneTimeBasedStart();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time picker1");
            }
        });

        long basedTimeFinishHour = Long.parseLong(btfh.getText().toString());
        long basedTimeFinishMinute = Long.parseLong(btfm.getText().toString());
        long basedTimeFinishMillis = ((basedTimeFinishHour * 3600) + (basedTimeFinishMinute * 60)) * 1000;
        String basedTimeFinishString = Helper.getDuration(basedTimeFinishMillis);
        if (basedTimeFinishHour > 0 || basedTimeFinishMinute > 0) {
            basedTimeFinishGrup.setText(basedTimeFinishString);
        } else {
            basedTimeFinishGrup.setText("Belum Diatur");
        }

        basedTimeFinishGrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new GrupLockPhoneTimeBasedFinish();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time picker1");
            }
        });
        String basedTimeSwitchState = sp.getString(SwitchBasedTimeGrup, "");
        if (basedTimeSwitchState.equals("")) {
            switchBasedTimeGrup.setChecked(false);
        } else if (basedTimeSwitchState.equals("Aktif")) {
            switchBasedTimeGrup.setChecked(true);
        }
        switchBasedTimeGrup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di Aktifkan'", Toast.LENGTH_LONG).show();
                    sbt.setText("Aktif");
                } else {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di Non-Aktifkan'", Toast.LENGTH_LONG).show();
                    sbt.setText("");
                }
            }
        });
        int radioTimeBaseState = sp.getInt(RadioTimeBaseStateGrup, 0);
        if (radioTimeBaseState == 0) {
            radio1Grup.toggle();
        } else {
            radio2Grup.toggle();
        }
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio1Grup.isChecked()) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di ulang satu kali' dalam 24 jam ", Toast.LENGTH_LONG).show();
                    rs.setText(String.valueOf(0));
                } else if (radio2Grup.isChecked()) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di ulang terus-menerus' dalam 24 jam", Toast.LENGTH_LONG).show();
                    rs.setText(String.valueOf(1));
                }
            }
        });
//==================================================================================================

        long basedScheduleStartHour = Long.parseLong(bssh.getText().toString());
        long basedScheduleStartMinute = Long.parseLong(bssm.getText().toString());
        String basedScheduleStartString;

        if (basedScheduleStartHour < 10 && basedScheduleStartMinute < 10) {
            basedScheduleStartString = "0" + basedScheduleStartHour + ":0" + basedScheduleStartMinute;
        } else if (basedScheduleStartHour < 10 && basedScheduleStartMinute > 10) {
            basedScheduleStartString = "0" + basedScheduleStartHour + ":" + basedScheduleStartMinute;
        } else if (basedScheduleStartHour > 10 && basedScheduleStartMinute < 10) {
            basedScheduleStartString = basedScheduleStartHour + ":0" + basedScheduleStartMinute;
        } else {
            basedScheduleStartString = basedScheduleStartHour + ":" + basedScheduleStartMinute;
        }
        basedScheduleStartGrup.setText(basedScheduleStartString);
        basedScheduleStartGrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new GrupLockPhoneScheduleBasedStart();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time Picker");
            }
        });

        long basedScheduleFinishHour = Long.parseLong(bsfh.getText().toString());
        long basedScheduleFinishMinute = Long.parseLong(bsfm.getText().toString());
        String basedScheduleFinishString;
        if (basedScheduleFinishHour < 10 && basedScheduleFinishMinute < 10) {
            basedScheduleFinishString = "0" + basedScheduleFinishHour + ":0" + basedScheduleFinishMinute;
        } else if (basedScheduleFinishHour < 10 && basedScheduleFinishMinute > 10) {
            basedScheduleFinishString = "0" + basedScheduleFinishHour + ":" + basedScheduleFinishMinute;
        } else if (basedScheduleFinishHour > 10 && basedScheduleFinishMinute < 10) {
            basedScheduleFinishString = basedScheduleFinishHour + ":0" + basedScheduleFinishMinute;
        } else {
            basedScheduleFinishString = basedScheduleFinishHour + ":" + basedScheduleFinishMinute;
        }
        basedScheduleFinishGrup.setText(basedScheduleFinishString);
        basedScheduleFinishGrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new GrupLockPhoneScheduleBasedFinish();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time Picker");
            }
        });
        String basedScheduleSwitchState = sp.getString(SwitchBasedScheduleGrup, "");
        if (basedScheduleSwitchState.equals("")) {
            switchBasedScheduleGrup.setChecked(false);

        } else if (basedScheduleSwitchState.equals("Aktif")) {
            switchBasedScheduleGrup.setChecked(true);
        }
        switchBasedScheduleGrup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan jadwal penguncian 'Di Aktifkan'", Toast.LENGTH_LONG).show();
                    sbs.setText("Aktif");
                } else {
                    Toast.makeText(requireActivity(), "Kunci Layar Berdasarkan jadwal penguncian 'Di Non-Aktifkan'", Toast.LENGTH_LONG).show();
                    sbs.setText("");
                }
            }
        });
//==================================================================================================
        MaterialButton batal_member_usage_lock_setting = root.findViewById(R.id.batal_member_usage_lock_setting);
        batal_member_usage_lock_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment10()).commit();
                layout_bottom.setVisibility(View.VISIBLE);
            }
        });
        MaterialButton simpan_member_usage_lock_setting = root.findViewById(R.id.simpan_member_usage_lock_setting);

        simpan_member_usage_lock_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sbt1 = sbt.getText().toString();
                final String sbs1 = sbs.getText().toString();
                final int btsh1 = Integer.parseInt(btsh.getText().toString());
                final int btsm1 = Integer.parseInt(btsm.getText().toString());
                final int btfh1 = Integer.parseInt(btfh.getText().toString());
                final int btfm1 = Integer.parseInt(btfm.getText().toString());
                final int bssh1 = Integer.parseInt(bssh.getText().toString());
                final int bssm1 = Integer.parseInt(bssm.getText().toString());
                final int bsfh1 = Integer.parseInt(bsfh.getText().toString());
                final int bsfm1 = Integer.parseInt(bsfm.getText().toString());
                final int rs1 = Integer.parseInt(rs.getText().toString());
                int btsg = btsh1 + btsm1;
                int btfg = btfh1 + btfm1;
                int bssg = bssh1 + bssm1;
                int bsfg = bsfh1 + bsfm1;
                int gbtl;
                int gbsl;
                if ((btsg > 0 && btfg > 0) && sbt1.equals("Aktif")) {
                    gbtl = 1;
                } else {
                    gbtl = 0;
                }
                if (bssg != bsfg && sbs1.equals("Aktif")) {
                    gbsl = 1;
                } else {
                    gbsl = 0;
                }
                final int gbtl1 = gbtl;
                final int gbsl1 = gbsl;

                SetterInfoTime setterInfoTime =
                        new SetterInfoTime(dateFormat.format(System.currentTimeMillis())
                                , sbt1, sbs1, btsh1, btsm1, btfh1, btfm1, bssh1, bssm1, bsfh1, bsfm1, rs1, gbtl1, gbsl1);

                DocumentReference docRef = firestore.collection("Groups").document(sp1.getString(SharePref_id_group, ""));
                docRef.set(setterInfoTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Berhasil disimpan!", Toast.LENGTH_LONG).show();
                            editor.putString(SwitchBasedTimeGrup, sbt1);
                            editor.putString(SwitchBasedScheduleGrup, sbs1);
                            editor.putInt(BasedTimeStartHourGrup, btsh1);
                            editor.putInt(BasedTimeStartMinuteGrup, btsm1);
                            editor.putInt(BasedTimeFinishHourGrup, btfh1);
                            editor.putInt(BasedTimeFinishMinuteGrup, btfm1);
                            editor.putInt(BasedScheduleStartHourGrup, bssh1);
                            editor.putInt(BasedScheduleStartMinuteGrup, bssm1);
                            editor.putInt(BasedScheduleFinishHourGrup, bsfh1);
                            editor.putInt(BasedScheduleFinishMinuteGrup, bsfm1);
                            editor.putInt(RadioTimeBaseStateGrup, rs1);
                            editor.putInt(GrupBasedTimeLock, gbtl1);
                            editor.putInt(GrupBasedScheduleLock, gbsl1);
                            editor.apply();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.nav_host_fragment, new GroupUsageFragment10()).commit();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Gagal disimpan!", Toast.LENGTH_LONG).show();
                        }
                        layout_bottom.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        return root;
    }

}
