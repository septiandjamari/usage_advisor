package com.djamari.usageadvisor.database.PersonalUsageDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.djamari.usageadvisor.database.Agenda.DailyAgendaDatabase.CREATE_TABLE_PENGINGAT_AGENDA;
import static com.djamari.usageadvisor.database.Agenda.DailyAgendaDatabase.TABLE_PENGINGAT_AGENDA;

public class PersonalUsageDatabase extends SQLiteOpenHelper {

    // Keterangan Database

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsageAdvisor.db";
    public static final String TABLE_DAFTAR_APLIKASI = "daftarAplikasi";
    public static final String TABLE_APP_COLLECTION = "appUsageCollection";

    // Keterangan Field Khusus Personal Usage

    private static String KEY_ID = "id";
    private static String KEY_NAMA_APLIKASI = "nama_aplikasi";
    private static String KEY_DURASI = "durasi_pakai";
    private static String KEY_FREKUENSI = "frekuensi_pakai";
    private static String KEY_TANGGAL_PAKAI = "tanggal_pakai";
    private static String KEY_TERAKHIR_DIPAKAI = "terakhir_dilihat";
    private static String KEY_FIRST_TIME_STAMP = "first_time_stamp";
    private static String KEY_LAST_TIME_STAMP = "last_time_stamp";
    public static String CREATE_TABLE_DAFTAR_APLIKASI = "CREATE TABLE " + TABLE_DAFTAR_APLIKASI + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAMA_APLIKASI + " TEXT,"
            + KEY_DURASI + " LONG," + KEY_FREKUENSI + " INT," + KEY_TANGGAL_PAKAI
            + " TEXT," + KEY_TERAKHIR_DIPAKAI + " TEXT" + ")";
    public static String CREATE_TABLE_APP_COLLECTION = "CREATE TABLE " + TABLE_APP_COLLECTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAMA_APLIKASI + " TEXT," + KEY_FIRST_TIME_STAMP + " TEXT," + KEY_LAST_TIME_STAMP
            + " TEXT," + KEY_TANGGAL_PAKAI + " TEXT," + KEY_DURASI + " TEXT" + ")";


    public PersonalUsageDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_DAFTAR_APLIKASI);
        db.execSQL(CREATE_TABLE_APP_COLLECTION);
        db.execSQL(CREATE_TABLE_PENGINGAT_AGENDA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAFTAR_APLIKASI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENGINGAT_AGENDA);
        onCreate(db);
    }

    private void addRecordDaftarAplikasi(DaftarAplikasiModel daftarAplikasiModel) {
        SQLiteDatabase db = getWritableDatabase();
        long tambah = 1;

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA_APLIKASI, daftarAplikasiModel.getNama_aplikasi());
        values.put(KEY_DURASI, daftarAplikasiModel.getDurasi_pakai());
        values.put(KEY_FREKUENSI, tambah);
        values.put(KEY_TANGGAL_PAKAI, daftarAplikasiModel.getTanggal_pakai());
        values.put(KEY_TERAKHIR_DIPAKAI, daftarAplikasiModel.getTerakhir_dilihat());

        db.insert(TABLE_DAFTAR_APLIKASI, null, values);
        db.close();
    }


//    QUERY Daftar Aplikasi

    private void updateRecordDaftarAplikasi(DaftarAplikasiModel daftarAplikasiModel) {
        SQLiteDatabase db = getWritableDatabase();
        long tambah = 1;
        String UPDATE_QUERY = "UPDATE " + TABLE_DAFTAR_APLIKASI + " SET " + KEY_DURASI + " = " + KEY_DURASI + " + "
                + daftarAplikasiModel.getDurasi_pakai() + "," + KEY_FREKUENSI + " = " + KEY_FREKUENSI + " + " + tambah
                + "," + KEY_TERAKHIR_DIPAKAI + " = '" + daftarAplikasiModel.getTerakhir_dilihat()
                + "' WHERE " + KEY_NAMA_APLIKASI + " = '" + daftarAplikasiModel.getNama_aplikasi() + "'";
        db.execSQL(UPDATE_QUERY);
        db.close();
    }

    public void checkRecordIfExist(DaftarAplikasiModel daftarAplikasiModel) {
        SQLiteDatabase db = getReadableDatabase();

        String namaAplikasi = daftarAplikasiModel.getNama_aplikasi();
        long durasiPakai = daftarAplikasiModel.getDurasi_pakai();
        int frekunsiPakai = daftarAplikasiModel.getFrekuensi_pakai();
        String tanggalPakai = daftarAplikasiModel.getTanggal_pakai();
        String terakhirDipakai = daftarAplikasiModel.getTerakhir_dilihat();

        String CHECK_RECORD_EXIST = "SELECT * FROM " + TABLE_DAFTAR_APLIKASI + " WHERE " + KEY_NAMA_APLIKASI + "= '" + namaAplikasi + "'";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(CHECK_RECORD_EXIST, null);

        if (cursor.getCount() > 0) {
            DaftarAplikasiModel input = new DaftarAplikasiModel(-1, namaAplikasi, durasiPakai, frekunsiPakai, null, terakhirDipakai);
            updateRecordDaftarAplikasi(input);
        } else {
            DaftarAplikasiModel input = new DaftarAplikasiModel(-1, namaAplikasi, durasiPakai, frekunsiPakai, tanggalPakai, terakhirDipakai);
            addRecordDaftarAplikasi(input);
        }
        db.close();
    }

    public List<DaftarAplikasiModel> getAllDaftarAplikasi() {
        List<DaftarAplikasiModel> listDaftarAplikasi = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_DAFTAR_APLIKASI + " ORDER BY " + KEY_DURASI + " DESC";

        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaftarAplikasiModel daftarAplikasiModel = new DaftarAplikasiModel();
                daftarAplikasiModel.setId(Integer.parseInt(cursor.getString(0)));
                daftarAplikasiModel.setNama_aplikasi(cursor.getString(1));
                daftarAplikasiModel.setDurasi_pakai(Long.parseLong(cursor.getString(2)));
                daftarAplikasiModel.setFrekuensi_pakai(Integer.parseInt(cursor.getString(3)));
                daftarAplikasiModel.setTanggal_pakai(cursor.getString(4));
                daftarAplikasiModel.setTerakhir_dilihat(cursor.getString(5));

                listDaftarAplikasi.add(daftarAplikasiModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return listDaftarAplikasi;
    }

    public List<DaftarAplikasiModel> daftarInteraksiAplikasi() {
        List<DaftarAplikasiModel> listDaftarAplikasi = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_DAFTAR_APLIKASI + " ORDER BY " + KEY_DURASI + " DESC" + " LIMIT 3";

        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaftarAplikasiModel daftarAplikasiModel = new DaftarAplikasiModel();
                daftarAplikasiModel.setId(Integer.parseInt(cursor.getString(0)));
                daftarAplikasiModel.setNama_aplikasi(cursor.getString(1));
                daftarAplikasiModel.setDurasi_pakai(Long.parseLong(cursor.getString(2)));
                daftarAplikasiModel.setFrekuensi_pakai(Integer.parseInt(cursor.getString(3)));
                daftarAplikasiModel.setTanggal_pakai(cursor.getString(4));
                daftarAplikasiModel.setTerakhir_dilihat(cursor.getString(5));

                listDaftarAplikasi.add(daftarAplikasiModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return listDaftarAplikasi;
    }

    public void deleteAllDaftarAplikasiRecord() {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE_RECORD = "DELETE FROM " + TABLE_DAFTAR_APLIKASI;

        db.execSQL(DELETE_RECORD);
        db.close();
    }

//      QUERY App Collection
    public void addRecordAppCollection(AppUsageCollectionModel appUsageCollectionModel) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA_APLIKASI, appUsageCollectionModel.getNama_aplikasi());
        values.put(KEY_FIRST_TIME_STAMP, appUsageCollectionModel.getFirst_time_stamp());
        values.put(KEY_LAST_TIME_STAMP, appUsageCollectionModel.getLast_time_stamp());
        values.put(KEY_TANGGAL_PAKAI, appUsageCollectionModel.getTanggal_pakai());
        values.put(KEY_DURASI, appUsageCollectionModel.getDurasi_pakai());

        db.insert(TABLE_APP_COLLECTION, null, values);
        db.close();
    }

    public List<AppUsageCollectionModel> getAllAppCollection() {
        List<AppUsageCollectionModel> listAppCollection = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_APP_COLLECTION + " ORDER BY " + KEY_ID + " ASC";

        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AppUsageCollectionModel appUsageCollectionModel = new AppUsageCollectionModel();
                appUsageCollectionModel.setId(Integer.parseInt(cursor.getString(0)));
                appUsageCollectionModel.setNama_aplikasi(cursor.getString(1));
                appUsageCollectionModel.setFirst_time_stamp(cursor.getString(2));
                appUsageCollectionModel.setLast_time_stamp(cursor.getString(3));
                appUsageCollectionModel.setTanggal_pakai(cursor.getString(4));
                appUsageCollectionModel.setDurasi_pakai(cursor.getString(5));

                listAppCollection.add(appUsageCollectionModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return listAppCollection;
    }

    public void deleteAllAppCollectionRecord() {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE_RECORD = "DELETE FROM " + TABLE_APP_COLLECTION;

        db.execSQL(DELETE_RECORD);
        db.close();
    }
}