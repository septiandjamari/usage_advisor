package com.djamari.usageadvisor.ui.lock_phone;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.otherActivity.DialogModal.LockPhoneScheduleBasedFinish;
import com.djamari.usageadvisor.otherActivity.DialogModal.LockPhoneScheduleBasedStart;
import com.djamari.usageadvisor.otherActivity.DialogModal.LockPhoneTimeBasedFinish;
import com.djamari.usageadvisor.otherActivity.DialogModal.LockPhoneTimeBasedStart;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

import static android.view.ViewGroup.OnClickListener;
import static com.djamari.usageadvisor.broadcastReceiver.OverallScreenService.dateFormat;
import static com.djamari.usageadvisor.broadcastReceiver.OverallScreenService.oneClickLockPhone;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedScheduleSwitchState;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeFinishMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartHour;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartHourGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartMinute;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeStartMinuteGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.BasedTimeSwitchState;
import static com.djamari.usageadvisor.helper.SharePref_keys.DateLockPhone2;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedScheduleLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.GrupBasedTimeLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.KunciLayar;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickDayOfYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickHourFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickLock;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickMinuteFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickSecondFinish;
import static com.djamari.usageadvisor.helper.SharePref_keys.OneClickYear;
import static com.djamari.usageadvisor.helper.SharePref_keys.RadioTimeBaseState;
import static com.djamari.usageadvisor.helper.SharePref_keys.RadioTimeBaseStateGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.SwitchBasedTimeGrup;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID2;

public class LockPhoneFragment extends Fragment {
    private boolean kunciOneClick = false;
    private int kuncilayar, grupBasedTimeLock, grupBasedScheduleLock;

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_lock_phone, container, false);
        final SharedPreferences sp = requireActivity().getApplicationContext().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        final SharedPreferences sp1 = requireActivity().getApplicationContext().getSharedPreferences(VALUEID2, 0);

        kuncilayar = sp.getInt(KunciLayar, 0);
        grupBasedTimeLock = sp1.getInt(GrupBasedTimeLock, 0);
        grupBasedScheduleLock = sp1.getInt(GrupBasedScheduleLock, 0);
        int oneClickLock = sp.getInt(OneClickLock, 0);

        MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        FloatingActionButton fabAgenda = requireActivity().findViewById(R.id.button_add_agenda);
        tombolKembalidetailed.setVisibility(View.GONE);
        fabAgenda.setVisibility(View.GONE);

        final MaterialButton basedTimeStart = root.findViewById(R.id.basedTimeStart);
        final MaterialButton basedTimeFinish = root.findViewById(R.id.basedTimeFinish);
        final SwitchMaterial switchBasedTime = root.findViewById(R.id.switchTimeBased);

        final MaterialButton basedScheduleStart = root.findViewById(R.id.basedScheduleStart);
        final MaterialButton basedScheduleFinish = root.findViewById(R.id.basedScheduleFinish);
        final SwitchMaterial switchBasedSchedule = root.findViewById(R.id.switchScheduleBased);

        final RadioGroup radioGroup = root.findViewById(R.id.radiobasedSceduleLockPhone);
        final RadioButton radio1 = root.findViewById(R.id.radiobaseSchedule1);
        final RadioButton radio2 = root.findViewById(R.id.radiobaseSchedule2);

        TextView lock_based_time_admin = root.findViewById(R.id.lock_based_time_admin);
        TextView lock_based_schedule_admin = root.findViewById(R.id.lock_based_schedule_admin);

        if (grupBasedTimeLock == 1 || kuncilayar == 1) {
            basedTimeStart.setEnabled(false);
            basedTimeFinish.setEnabled(false);
            switchBasedTime.setEnabled(false);
            radio1.setEnabled(false);
            radio2.setEnabled(false);
            if (grupBasedTimeLock == 1) {
                switchBasedTime.setChecked(true);
                lock_based_time_admin.setVisibility(View.VISIBLE);
            }
        } else {
            basedTimeStart.setEnabled(true);
            basedTimeFinish.setEnabled(true);
            switchBasedTime.setEnabled(true);
            radio1.setEnabled(true);
            radio2.setEnabled(true);
            lock_based_time_admin.setVisibility(View.GONE);
        }

        if (grupBasedScheduleLock == 1 || kuncilayar == 1) {
            basedScheduleStart.setEnabled(false);
            basedScheduleFinish.setEnabled(false);
            switchBasedSchedule.setEnabled(false);
            if (grupBasedScheduleLock == 1) {
                switchBasedSchedule.setChecked(true);
                lock_based_schedule_admin.setVisibility(View.VISIBLE);
            }
        } else {
            basedScheduleStart.setEnabled(true);
            basedScheduleFinish.setEnabled(true);
            switchBasedSchedule.setEnabled(true);
            lock_based_schedule_admin.setVisibility(View.GONE);
        }

        final MaterialButton kunci15menit = root.findViewById(R.id.kunci15menit);
        final MaterialButton kunci30menit = root.findViewById(R.id.kunci30Menit);
        final MaterialButton kunci1jam = root.findViewById(R.id.kunci1jam);
        final MaterialButton kunci2jam = root.findViewById(R.id.kunci2jam);

        if (oneClickLock == 1) {
            kunci15menit.setEnabled(false);
            kunci30menit.setEnabled(false);
            kunci1jam.setEnabled(false);
            kunci2jam.setEnabled(false);
        }


//==================================================================================================
        long basedTimeStartHour, basedTimeStartMinute, basedTimeFinishHour, basedTimeFinishMinute;
        String basedTimeSwitchState;
        int radioTimeBaseState;
        if (grupBasedTimeLock == 1) {
            basedTimeStartHour = sp1.getInt(BasedTimeStartHourGrup, 0);
            basedTimeStartMinute = sp1.getInt(BasedTimeStartMinuteGrup, 0);
            basedTimeFinishHour = sp1.getInt(BasedTimeFinishHourGrup, 0);
            basedTimeFinishMinute = sp1.getInt(BasedTimeFinishMinuteGrup, 0);
            basedTimeSwitchState = sp1.getString(SwitchBasedTimeGrup, "");
            radioTimeBaseState = sp1.getInt(RadioTimeBaseStateGrup, 0);
        } else {
            basedTimeStartHour = sp.getLong(BasedTimeStartHour, 0);
            basedTimeStartMinute = sp.getLong(BasedTimeStartMinute, 0);
            basedTimeFinishHour = sp.getLong(BasedTimeFinishHour, 0);
            basedTimeFinishMinute = sp.getLong(BasedTimeFinishMinute, 0);
            basedTimeSwitchState = sp.getString(BasedTimeSwitchState, "");
            radioTimeBaseState = sp.getInt(RadioTimeBaseState, 0);
        }

        long basedTimeStartMillis = ((basedTimeStartHour * 3600) + (basedTimeStartMinute * 60)) * 1000;
        String basedTimeStartString = Helper.getDuration(basedTimeStartMillis);
        if (basedTimeStartHour > 0 || basedTimeStartMinute > 0) {
            basedTimeStart.setText(basedTimeStartString);
        } else {
            basedTimeStart.setText("Belum Diatur");
        }

        basedTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new LockPhoneTimeBasedStart();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time picker1");
            }
        });

        long basedTimeFinishMillis = ((basedTimeFinishHour * 3600) + (basedTimeFinishMinute * 60)) * 1000;
        String basedTimeFinishString = Helper.getDuration(basedTimeFinishMillis);
        if (basedTimeFinishHour > 0 || basedTimeFinishMinute > 0) {
            basedTimeFinish.setText(basedTimeFinishString);
        } else {
            basedTimeFinish.setText("Belum Diatur");
        }

        basedTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new LockPhoneTimeBasedFinish();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time picker1");
            }
        });

        if (basedTimeSwitchState.equals("")) {
            switchBasedTime.setChecked(false);
        } else if (basedTimeSwitchState.equals("Aktif")) {
            switchBasedTime.setChecked(true);
        }
        switchBasedTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchBasedTime.setChecked(true);
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di Aktifkan'", Toast.LENGTH_LONG).show();
                    editor.putString(BasedTimeSwitchState, "Aktif");
                } else {
                    switchBasedTime.setChecked(false);
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di Non-Aktifkan'", Toast.LENGTH_LONG).show();
                    editor.putString(BasedTimeSwitchState, "");
                }
                editor.apply();
            }
        });


        if (radioTimeBaseState == 0) {
            radio1.toggle();
        } else {
            radio2.toggle();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio1.isChecked()) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di ulang satu kali' dalam 24 jam ", Toast.LENGTH_LONG).show();
                    editor.putInt(RadioTimeBaseState, 0);
                    editor.apply();
                } else if (radio2.isChecked()) {
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan waktu penggunaan 'Di ulang terus-menerus' dalam 24 jam", Toast.LENGTH_LONG).show();
                    editor.putInt(RadioTimeBaseState, 1);
                    editor.apply();
                }
            }
        });
//==================================================================================================
        long basedScheduleStartHour, basedScheduleStartMinute, basedScheduleFinishHour, basedScheduleFinishMinute;
        if (grupBasedScheduleLock == 1) {
            basedScheduleStartHour = sp1.getInt(BasedScheduleStartHourGrup, 0);
            basedScheduleStartMinute = sp1.getInt(BasedScheduleStartMinuteGrup, 0);
            basedScheduleFinishHour = sp1.getInt(BasedScheduleFinishHourGrup, 0);
            basedScheduleFinishMinute = sp1.getInt(BasedScheduleFinishMinuteGrup, 0);
        } else {
            basedScheduleStartHour = sp.getLong(BasedScheduleStartHour, 0);
            basedScheduleStartMinute = sp.getLong(BasedScheduleStartMinute, 0);
            basedScheduleFinishHour = sp.getLong(BasedScheduleFinishHour, 0);
            basedScheduleFinishMinute = sp.getLong(BasedScheduleFinishMinute, 0);
        }

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
        basedScheduleStart.setText(basedScheduleStartString);
        basedScheduleStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new LockPhoneScheduleBasedStart();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time Picker");
            }
        });


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
        basedScheduleFinish.setText(basedScheduleFinishString);
        basedScheduleFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new LockPhoneScheduleBasedFinish();
                timepicker.show(requireActivity().getSupportFragmentManager(), "time Picker");
            }
        });
        String basedScheduleSwitchState = sp.getString(BasedScheduleSwitchState, "");
        if (basedScheduleSwitchState.equals("")) {
            switchBasedSchedule.setChecked(false);

        } else if (basedScheduleSwitchState.equals("Aktif")) {
            switchBasedSchedule.setChecked(true);
        }
        switchBasedSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchBasedSchedule.setChecked(true);
                    Toast.makeText(requireActivity(), "Kunci layar berdasarkan jadwal penguncian 'Di Aktifkan'", Toast.LENGTH_LONG).show();
                    editor.putString(BasedScheduleSwitchState, "Aktif");
                } else {
                    switchBasedSchedule.setChecked(false);
                    Toast.makeText(requireActivity(), "Kunci Layar Berdasarkan jadwal penguncian 'Di Non-Aktifkan'", Toast.LENGTH_LONG).show();
                    editor.putString(BasedScheduleSwitchState, "");
                }
                editor.apply();
            }
        });
//==================================================================================================
        final Calendar jam = Calendar.getInstance();
        final LinearLayout snackbarLayout = requireActivity().findViewById(R.id.activity_snackbar_coordinator_layout);
        kunci15menit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final long oneClickHourFinish = jam.get(Calendar.HOUR_OF_DAY);
                final long oneClickMinuteFinish = jam.get(Calendar.MINUTE);
                final long oneClickSecondFinish = jam.get(Calendar.SECOND);
                final long year = jam.get(Calendar.YEAR);
                final long dayOfYear = jam.get(Calendar.DAY_OF_YEAR);
                kunciOneClick = true;
                buttonDisabled();
                Snackbar snackbar = Snackbar.make(snackbarLayout, "Handphone akan dikunci selama 15 menit", Snackbar.LENGTH_LONG);
                snackbar.setAction("BATALKAN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kunciOneClick = false;
                        buttonEnabled();
                    }
                }).setDuration(3000).setActionTextColor(Color.YELLOW).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (kunciOneClick) {
                            editor.putLong(OneClickHourFinish, oneClickHourFinish);
                            editor.putLong(OneClickMinuteFinish, oneClickMinuteFinish + 15);
                            editor.putLong(OneClickSecondFinish, oneClickSecondFinish);
                            editor.putLong(OneClickYear, year);
                            editor.putLong(OneClickDayOfYear, dayOfYear);
                            editor.putString(DateLockPhone2, dateFormat.format(System.currentTimeMillis()));
                            editor.putInt(OneClickLock, 1);
                            editor.apply();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    oneClickLockPhone(requireActivity().getApplicationContext());
                                }
                            }, 3000);
                            kunciOneClick = false;
                        } else {
                            handler.removeCallbacks(this);
                        }
                        buttonEnabled();
                    }
                }, 2000);
            }
        });
        kunci30menit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final long oneClickHourFinish = jam.get(Calendar.HOUR_OF_DAY);
                final long oneClickMinuteFinish = jam.get(Calendar.MINUTE);
                final long oneClickSecondFinish = jam.get(Calendar.SECOND);
                final long year = jam.get(Calendar.YEAR);
                final long dayOfYear = jam.get(Calendar.DAY_OF_YEAR);
                kunciOneClick = true;
                buttonDisabled();
                Snackbar snackbar = Snackbar.make(snackbarLayout, "Handphone akan dikunci selama 30 menit", Snackbar.LENGTH_LONG);
                snackbar.setAction("BATALKAN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kunciOneClick = false;
                        buttonEnabled();
                    }
                }).setDuration(3000).setActionTextColor(Color.YELLOW).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (kunciOneClick) {
                            editor.putLong(OneClickHourFinish, oneClickHourFinish);
                            editor.putLong(OneClickMinuteFinish, oneClickMinuteFinish + 30);
                            editor.putLong(OneClickSecondFinish, oneClickSecondFinish);
                            editor.putLong(OneClickYear, year);
                            editor.putLong(OneClickDayOfYear, dayOfYear);
                            editor.putString(DateLockPhone2, dateFormat.format(System.currentTimeMillis()));
                            editor.putInt(OneClickLock, 1);
                            editor.apply();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    oneClickLockPhone(requireActivity().getApplicationContext());
                                }
                            }, 3000);

                            kunciOneClick = false;
                        } else {
                            handler.removeCallbacks(this);
                        }
                        buttonEnabled();
                    }
                }, 2000);
            }
        });
        kunci1jam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final long oneClickHourFinish = jam.get(Calendar.HOUR_OF_DAY);
                final long oneClickMinuteFinish = jam.get(Calendar.MINUTE);
                final long oneClickSecondFinish = jam.get(Calendar.SECOND);
                final long year = jam.get(Calendar.YEAR);
                final long dayOfYear = jam.get(Calendar.DAY_OF_YEAR);
                kunciOneClick = true;
                buttonDisabled();
                Snackbar snackbar = Snackbar.make(snackbarLayout, "Handphone akan dikunci selama 1 jam", Snackbar.LENGTH_LONG);
                snackbar.setAction("BATALKAN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kunciOneClick = false;
                        buttonEnabled();
                    }
                }).setDuration(3000).setActionTextColor(Color.YELLOW).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (kunciOneClick) {
                            editor.putLong(OneClickHourFinish, oneClickHourFinish + 1);
                            editor.putLong(OneClickMinuteFinish, oneClickMinuteFinish);
                            editor.putLong(OneClickSecondFinish, oneClickSecondFinish);
                            editor.putInt(OneClickLock, 1);
                            editor.putLong(OneClickYear, year);
                            editor.putLong(OneClickDayOfYear, dayOfYear);
                            editor.putString(DateLockPhone2, dateFormat.format(System.currentTimeMillis()));
                            editor.apply();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    oneClickLockPhone(requireActivity().getApplicationContext());
                                }
                            }, 3000);
                            kunciOneClick = false;
                        } else {
                            handler.removeCallbacks(this);
                        }
                        buttonEnabled();
                    }
                }, 2000);
            }
        });
        kunci2jam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final long oneClickHourFinish = jam.get(Calendar.HOUR_OF_DAY);
                final long oneClickMinuteFinish = jam.get(Calendar.MINUTE);
                final long oneClickSecondFinish = jam.get(Calendar.SECOND);
                final long year = jam.get(Calendar.YEAR);
                final long dayOfYear = jam.get(Calendar.DAY_OF_YEAR);
                kunciOneClick = true;
                buttonDisabled();
                Snackbar snackbar = Snackbar.make(snackbarLayout, "Handphone akan dikunci selama 2 jam", Snackbar.LENGTH_LONG);
                snackbar.setAction("BATALKAN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kunciOneClick = false;
                        buttonEnabled();
                    }
                }).setDuration(3000).setActionTextColor(Color.YELLOW).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (kunciOneClick) {
                            editor.putLong(OneClickHourFinish, oneClickHourFinish + 2);
                            editor.putLong(OneClickMinuteFinish, oneClickMinuteFinish);
                            editor.putLong(OneClickSecondFinish, oneClickSecondFinish);
                            editor.putInt(OneClickLock, 1);
                            editor.putLong(OneClickYear, year);
                            editor.putLong(OneClickDayOfYear, dayOfYear);
                            editor.putString(DateLockPhone2, dateFormat.format(System.currentTimeMillis()));
                            editor.apply();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    oneClickLockPhone(requireActivity().getApplicationContext());
                                }
                            }, 3000);

                            kunciOneClick = false;
                        } else {
                            handler.removeCallbacks(this);
                        }
                        buttonEnabled();
                    }
                }, 2000);
            }
        });

        return root;
    }

    private void buttonDisabled() {
        MaterialButton basedTimeStart = requireActivity().findViewById(R.id.basedTimeStart);
        MaterialButton basedTimeFinish = requireActivity().findViewById(R.id.basedTimeFinish);
        SwitchMaterial switchBasedTime = requireActivity().findViewById(R.id.switchTimeBased);

        MaterialButton basedScheduleStart = requireActivity().findViewById(R.id.basedScheduleStart);
        MaterialButton basedScheduleFinish = requireActivity().findViewById(R.id.basedScheduleFinish);
        SwitchMaterial switchBasedSchedule = requireActivity().findViewById(R.id.switchScheduleBased);

        MaterialButton kunci15menit = requireActivity().findViewById(R.id.kunci15menit);
        MaterialButton kunci30menit = requireActivity().findViewById(R.id.kunci30Menit);
        MaterialButton kunci1jam = requireActivity().findViewById(R.id.kunci1jam);
        MaterialButton kunci2jam = requireActivity().findViewById(R.id.kunci2jam);

        RadioButton radio1 = requireActivity().findViewById(R.id.radiobaseSchedule1);
        RadioButton radio2 = requireActivity().findViewById(R.id.radiobaseSchedule2);
        basedTimeStart.setEnabled(false);
        basedTimeFinish.setEnabled(false);
        switchBasedTime.setEnabled(false);

        basedScheduleStart.setEnabled(false);
        basedScheduleFinish.setEnabled(false);
        switchBasedSchedule.setEnabled(false);
        kunci15menit.setEnabled(false);
        kunci30menit.setEnabled(false);
        kunci1jam.setEnabled(false);
        kunci2jam.setEnabled(false);

        radio1.setEnabled(false);
        radio2.setEnabled(false);
    }

    private void buttonEnabled() {
        MaterialButton basedTimeStart = requireActivity().findViewById(R.id.basedTimeStart);
        MaterialButton basedTimeFinish = requireActivity().findViewById(R.id.basedTimeFinish);
        SwitchMaterial switchBasedTime = requireActivity().findViewById(R.id.switchTimeBased);

        MaterialButton basedScheduleStart = requireActivity().findViewById(R.id.basedScheduleStart);
        MaterialButton basedScheduleFinish = requireActivity().findViewById(R.id.basedScheduleFinish);
        SwitchMaterial switchBasedSchedule = requireActivity().findViewById(R.id.switchScheduleBased);

        MaterialButton kunci15menit = requireActivity().findViewById(R.id.kunci15menit);
        MaterialButton kunci30menit = requireActivity().findViewById(R.id.kunci30Menit);
        MaterialButton kunci1jam = requireActivity().findViewById(R.id.kunci1jam);
        MaterialButton kunci2jam = requireActivity().findViewById(R.id.kunci2jam);

        RadioButton radio1 = requireActivity().findViewById(R.id.radiobaseSchedule1);
        RadioButton radio2 = requireActivity().findViewById(R.id.radiobaseSchedule2);

        if (grupBasedTimeLock == 1 || kuncilayar == 1) {
            basedTimeStart.setEnabled(false);
            basedTimeFinish.setEnabled(false);
            switchBasedTime.setEnabled(false);
            radio1.setEnabled(false);
            radio2.setEnabled(false);
        } else {
            basedTimeStart.setEnabled(true);
            basedTimeFinish.setEnabled(true);
            switchBasedTime.setEnabled(true);
            radio1.setEnabled(true);
            radio2.setEnabled(true);
        }

        if (grupBasedScheduleLock == 1 || kuncilayar == 1) {
            basedScheduleStart.setEnabled(false);
            basedScheduleFinish.setEnabled(false);
            switchBasedSchedule.setEnabled(false);
        } else {
            basedScheduleStart.setEnabled(true);
            basedScheduleFinish.setEnabled(true);
            switchBasedSchedule.setEnabled(true);
        }

        kunci15menit.setEnabled(true);
        kunci30menit.setEnabled(true);
        kunci1jam.setEnabled(true);
        kunci2jam.setEnabled(true);

    }
}
