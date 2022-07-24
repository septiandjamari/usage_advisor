package com.djamari.usageadvisor.otherActivity.Agenda;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.Agenda.DailyAgendaDatabase;
import com.djamari.usageadvisor.database.Agenda.DailyAgendaModel;
import com.djamari.usageadvisor.otherActivity.DialogModal.TimePickerFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;


public class AddEditDailyAgendaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextInputEditText agendaTimePicker;
    private TextInputEditText inputNamaAgenda;
    private TextView validasiHari;
    private DailyAgendaDatabase db = new DailyAgendaDatabase(AddEditDailyAgendaActivity.this);
    private int posisiRepeat, nilaiRepeat, switchAktif;


    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateDetail = new SimpleDateFormat("MdyyyHmmss");
    private int hours = 0, minutes = 0;
    EditText agendaInputRepeat;
    private String uniqueId = dateDetail.format(System.currentTimeMillis());

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_agenda_add_edit);
        MaterialToolbar toolbar = findViewById(R.id.agenda_harian_toolbar);

        Intent i = getIntent();
        final String AddOrEditState = i.getStringExtra("AddOrEditState");
//==================================================================================================
        agendaTimePicker = findViewById(R.id.tambah_jam_agenda_harian);
        inputNamaAgenda = findViewById(R.id.tambah_nama_agenda_harian);
        agendaInputRepeat = findViewById(R.id.agenda_input_repeat);

        final CheckBox hari_senin = findViewById(R.id.hari_senin);
        final CheckBox hari_selasa = findViewById(R.id.hari_selasa);
        final CheckBox hari_rabu = findViewById(R.id.hari_rabu);
        final CheckBox hari_kamis = findViewById(R.id.hari_kamis);
        final CheckBox hari_jumat = findViewById(R.id.hari_jumat);
        final CheckBox hari_sabtu = findViewById(R.id.hari_sabtu);
        final CheckBox hari_minggu = findViewById(R.id.hari_minggu);

        SwitchMaterial agendaSwitchHidupkan = findViewById(R.id.agenda_switch_hidupkan);
        final TextView agendaTextviewHidupkan = findViewById(R.id.agenda_textView_hidupkan);

        MaterialButton agendaRepeatKurang = findViewById(R.id.agenda_repeat_kurang);
        MaterialButton agendaRepeatTambah = findViewById(R.id.agenda_repeat_tambah);
        validasiHari = findViewById(R.id.validasi_hari);


//==================================================================================================
        if (Objects.requireNonNull(AddOrEditState).equals("Add")) {
            toolbar.setTitle("Tambah Agenda");
            Toast.makeText(this, "Tambah Agenda", Toast.LENGTH_SHORT).show();

        } else if (AddOrEditState.equals("Edit")) {

            String editUniqueId = i.getStringExtra("uniqueId");
            uniqueId = editUniqueId;
            toolbar.setTitle("Edit Agenda");
            Toast.makeText(this, "Edit Agenda", Toast.LENGTH_SHORT).show();
            //======================================================================================
            String editNamaAgenda = "";
            List<String> listHari = new ArrayList<>();
            int editHour = 0, editMinute = 0, editRepeat = 0, editSwitch = 0;
            List<DailyAgendaModel> getAgendaDetail = db.getAgendaDetail(editUniqueId);
            for (DailyAgendaModel dam : getAgendaDetail) {
                editNamaAgenda = dam.getNamaAgenda();
                listHari.add(dam.getHariString());
                editHour = dam.getHour();
                editMinute = dam.getMinute();
                editRepeat = dam.getRepeat();
                editSwitch = dam.getSwicthAktif();
            }
            inputNamaAgenda.setText(editNamaAgenda);
            String agendaTime;
            if (editHour < 10 && editMinute < 10) {
                agendaTime = "0" + editHour + ":0" + editMinute;
            } else if (editHour < 10 && editMinute > 10) {
                agendaTime = "0" + editHour + ":" + editMinute;
            } else if (editHour > 10 && editMinute < 10) {
                agendaTime = editHour + ":0" + editMinute;
            } else {
                agendaTime = editHour + ":" + editMinute;
            }
            if (editRepeat == 0) {
                posisiRepeat = 0;
            } else if (editRepeat == 2) {
                posisiRepeat = 1;
            } else if (editRepeat == 5) {
                posisiRepeat = 2;
            } else if (editRepeat == 10) {
                posisiRepeat = 3;
            } else if (editRepeat == 15) {
                posisiRepeat = 4;
            } else if (editRepeat == 30) {
                posisiRepeat = 5;
            }

            hours = editHour;
            minutes = editMinute;
            repeatValue(posisiRepeat);
            agendaInputRepeat.setText(String.valueOf(nilaiRepeat));
            agendaTimePicker.setText(agendaTime);

            if (editSwitch == 0) {
                agendaTextviewHidupkan.setText("Pengingat Tidak Aktif");
                agendaSwitchHidupkan.setChecked(false);

            } else if (editSwitch == 1) {
                agendaTextviewHidupkan.setText("Pengingat Aktif");
                agendaSwitchHidupkan.setChecked(true);
            }

            if (listHari.contains("Senin")) {
                hari_senin.setChecked(true);
            }
            if (listHari.contains("Selasa")) {
                hari_selasa.setChecked(true);
            }
            if (listHari.contains("Rabu")) {
                hari_rabu.setChecked(true);
            }
            if (listHari.contains("Kamis")) {
                hari_kamis.setChecked(true);
            }
            if (listHari.contains("Jumat")) {
                hari_jumat.setChecked(true);
            }
            if (listHari.contains("Sabtu")) {
                hari_sabtu.setChecked(true);
            }
            if (listHari.contains("Minggu")) {
                hari_minggu.setChecked(true);
            }

        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (agendaSwitchHidupkan.isChecked()) {
            switchAktif = 1;
        } else if (!agendaSwitchHidupkan.isChecked()) {
            switchAktif = 0;
        }

        agendaSwitchHidupkan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agendaTextviewHidupkan.setText("Pengingat Aktif");
                    switchAktif = 1;
                } else {
                    agendaTextviewHidupkan.setText("Pengingat Tidak Aktif");
                    switchAktif = 0;
                }
            }
        });
        agendaTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        final DailyAgendaModel[] dailyAgendaModel = new DailyAgendaModel[1];
        dailyAgendaModel[0] = new DailyAgendaModel(-1, "", "", "", -1, -1, -1, -1, -1);
        final String[] hariString = new String[1];
        hariString[0] = "";
        final int[] hariInt = new int[1];

        agendaRepeatTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posisiRepeat < 5) {
                    posisiRepeat += 1;
                    repeatValue(posisiRepeat);
                    agendaInputRepeat.setText(String.valueOf(nilaiRepeat));
                }
            }
        });
        agendaRepeatKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posisiRepeat > 0) {
                    posisiRepeat -= 1;
                    repeatValue(posisiRepeat);
                    agendaInputRepeat.setText(String.valueOf(nilaiRepeat));
                }
            }
        });

        MaterialButton fab = findViewById(R.id.saveAgenda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validasi
                String validasiNamaAgenda = Objects.requireNonNull(inputNamaAgenda.getText()).toString();
                String validasiJamAgenda = Objects.requireNonNull(agendaTimePicker.getText()).toString();
                hariString[0] = "";
                dailyAgendaModel[0] = new DailyAgendaModel(-1, "", "", "", -1, -1, -1, -1, -1);
                String namaAgenda = Objects.requireNonNull(inputNamaAgenda.getText()).toString();
                List<String> listHari = new ArrayList<>();

                if (validasiNamaAgenda.isEmpty()) {
                    inputNamaAgenda.setError("Mohon isi nama agenda");
                }
                if (validasiJamAgenda.isEmpty()) {
                    agendaTimePicker.setError("Mohon isi jam");
                }
                if (hari_senin.isChecked()) {
                    hariString[0] = "Senin";
                    listHari.add(hariString[0]);
                }
                if (hari_selasa.isChecked()) {
                    hariString[0] = "Selasa";
                    listHari.add(hariString[0]);
                }
                if (hari_rabu.isChecked()) {
                    hariString[0] = "Rabu";
                    listHari.add(hariString[0]);
                }
                if (hari_kamis.isChecked()) {
                    hariString[0] = "Kamis";
                    listHari.add(hariString[0]);
                }
                if (hari_jumat.isChecked()) {
                    hariString[0] = "Jumat";
                    listHari.add(hariString[0]);
                }
                if (hari_sabtu.isChecked()) {
                    hariString[0] = "Sabtu";
                    listHari.add(hariString[0]);
                }
                if (hari_minggu.isChecked()) {
                    hariString[0] = "Minggu";
                    listHari.add(hariString[0]);
                }

                if (hariString[0].isEmpty()) {
                    Toast.makeText(AddEditDailyAgendaActivity.this, "Mohon pilih hari", Toast.LENGTH_SHORT).show();
                    validasiHari.setVisibility(View.VISIBLE);
                } else if (!validasiNamaAgenda.isEmpty() && !validasiJamAgenda.isEmpty()) {
                    if (AddOrEditState.equals("Edit")) {
                        db.hapusAgenda(uniqueId);
                    }
                    for (String hariList : listHari) {
                        switch (hariList) {
                            case "Senin":
                                hariInt[0] = 1;
                                break;
                            case "Selasa":
                                hariInt[0] = 2;
                                break;
                            case "Rabu":
                                hariInt[0] = 3;
                                break;
                            case "Kamis":
                                hariInt[0] = 4;
                                break;
                            case "Jumat":
                                hariInt[0] = 5;
                                break;
                            case "Sabtu":
                                hariInt[0] = 6;
                                break;
                            case "Minggu":
                                hariInt[0] = 7;
                                break;
                        }
                        dailyAgendaModel[0] = new DailyAgendaModel(-1, uniqueId, namaAgenda, hariList, hours, minutes, hariInt[0], nilaiRepeat, switchAktif);
                        db.addRecordAgenda(dailyAgendaModel[0]);
                    }
                    finish();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editor.putString("refreshStatus", dateDetail.format(System.currentTimeMillis()));
                            editor.apply();
                        }
                    }, 200);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(VALUEID, 0);
        final SharedPreferences.Editor editor = sp.edit();
        finish();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editor.putString("refreshStatus", dateDetail.format(System.currentTimeMillis()));
                editor.apply();
            }
        }, 200);
        Toast.makeText(AddEditDailyAgendaActivity.this, "Agenda belum disimpan", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hours = hourOfDay;
        minutes = minute;
        String agendaTime;
        agendaTimePicker = findViewById(R.id.tambah_jam_agenda_harian);
        if (hours < 10 && minutes < 10) {
            agendaTime = "0" + hours + ":0" + minutes;
        } else if (hours < 10 && minutes > 10) {
            agendaTime = "0" + hours + ":" + minutes;
        } else if (hours > 10 && minutes < 10) {
            agendaTime = hours + ":0" + minutes;
        } else {
            agendaTime = hours + ":" + minutes;
        }
        agendaTimePicker.setText(agendaTime);
    }

    private void repeatValue(int posisi) {
        if (posisi == 0) {
            nilaiRepeat = 0;
        }
        if (posisi == 1) {
            nilaiRepeat = 2;
        }
        if (posisi == 2) {
            nilaiRepeat = 5;
        }
        if (posisi == 3) {
            nilaiRepeat = 10;
        }
        if (posisi == 4) {
            nilaiRepeat = 15;
        }
        if (posisi == 5) {
            nilaiRepeat = 30;
        }
    }
}