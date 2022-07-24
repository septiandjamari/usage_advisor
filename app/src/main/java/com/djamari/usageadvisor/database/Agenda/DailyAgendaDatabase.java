package com.djamari.usageadvisor.database.Agenda;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase.CREATE_TABLE_APP_COLLECTION;
import static com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase.CREATE_TABLE_DAFTAR_APLIKASI;
import static com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase.TABLE_APP_COLLECTION;
import static com.djamari.usageadvisor.database.PersonalUsageDatabase.PersonalUsageDatabase.TABLE_DAFTAR_APLIKASI;

public class DailyAgendaDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsageAdvisor.db";
    public static final String TABLE_PENGINGAT_AGENDA = "pengingatAgenda";

    private static String KEY_ID = "id";//int
    private static String KEY_UNIQUE = "uniqueId";//string
    private static String KEY_NAMA_AGENDA = "namaAgenda";//string
    private static String KEY_HOUR = "hour";//int
    private static String KEY_MINUTES = "minutes";//int
    private static String KEY_HARI_STRING = "hariString";//string
    private static String KEY_HARI_INT = "hariInt";//int
    private static String KEY_REPEAT_ALARM = "repeatAlarm";//int
    private static String KEY_IS_ACTIVE = "isActive";
    public static String CREATE_TABLE_PENGINGAT_AGENDA = "CREATE TABLE " + TABLE_PENGINGAT_AGENDA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_UNIQUE + " TEXT," + KEY_NAMA_AGENDA + " TEXT,"
            + KEY_HOUR + " INTEGER," + KEY_MINUTES + " INTEGER," + KEY_HARI_STRING + " TEXT," + KEY_HARI_INT + " INTEGER,"
            + KEY_REPEAT_ALARM + " INTEGER," + KEY_IS_ACTIVE + " INTEGER" + ")";

    public DailyAgendaDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PENGINGAT_AGENDA);
        db.execSQL(CREATE_TABLE_DAFTAR_APLIKASI);
        db.execSQL(CREATE_TABLE_APP_COLLECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENGINGAT_AGENDA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAFTAR_APLIKASI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COLLECTION);

        onCreate(db);
    }

    public void addRecordAgenda(DailyAgendaModel dailyAgendaModel) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UNIQUE, dailyAgendaModel.getUniqueId());
        values.put(KEY_NAMA_AGENDA, dailyAgendaModel.getNamaAgenda());
        values.put(KEY_HOUR, dailyAgendaModel.getHour());
        values.put(KEY_MINUTES, dailyAgendaModel.getMinute());
        values.put(KEY_HARI_STRING, dailyAgendaModel.getHariString());
        values.put(KEY_HARI_INT, dailyAgendaModel.getHariInt());
        values.put(KEY_REPEAT_ALARM, dailyAgendaModel.getRepeat());
        values.put(KEY_IS_ACTIVE, dailyAgendaModel.getSwicthAktif());

        db.insert(TABLE_PENGINGAT_AGENDA, null, values);
        db.close();
    }

    public List<DailyAgendaModel> getAllUniqueId() {
        List<DailyAgendaModel> listNamaAgenda = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_UNIQUE + " FROM " + TABLE_PENGINGAT_AGENDA + " ORDER BY " + KEY_HOUR + " ASC," + KEY_MINUTES + " ASC";

        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DailyAgendaModel dailyAgendaModel = new DailyAgendaModel();
                dailyAgendaModel.setUniqueId(cursor.getString(0));

                listNamaAgenda.add(dailyAgendaModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return listNamaAgenda;
    }

    public List<DailyAgendaModel> getAgendaDetail(String uniqueid) {
        List<DailyAgendaModel> listAgendaDetail = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PENGINGAT_AGENDA + " WHERE " + KEY_UNIQUE + " = '" + uniqueid + "'";
        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DailyAgendaModel dailyAgendaModel = new DailyAgendaModel();
                dailyAgendaModel.setId(cursor.getInt(0));
                dailyAgendaModel.setUniqueId(cursor.getString(1));
                dailyAgendaModel.setNamaAgenda(cursor.getString(2));
                dailyAgendaModel.setHour(cursor.getInt(3));
                dailyAgendaModel.setMinute(cursor.getInt(4));
                dailyAgendaModel.setHariString(cursor.getString(5));
                dailyAgendaModel.setHariInt(cursor.getInt(6));
                dailyAgendaModel.setRepeat(cursor.getInt(7));
                dailyAgendaModel.setSwicthAktif(cursor.getInt(8));


                listAgendaDetail.add(dailyAgendaModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return listAgendaDetail;
    }

    public void hapusAgenda(String uniqueId) {
        String deleteQuery = "DELETE FROM " + TABLE_PENGINGAT_AGENDA + " WHERE " + KEY_UNIQUE + " ='" + uniqueId + "'";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

}
