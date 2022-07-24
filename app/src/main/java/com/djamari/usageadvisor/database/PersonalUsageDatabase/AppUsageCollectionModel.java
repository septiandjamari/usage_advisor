package com.djamari.usageadvisor.database.PersonalUsageDatabase;

public class AppUsageCollectionModel {
    private int id;
    private String nama_aplikasi;
    private String first_time_stamp;
    private String last_time_stamp;
    private String tanggal_pakai;
    private String durasi_pakai;

    AppUsageCollectionModel() {

    }

    public AppUsageCollectionModel(int id, String nama_aplikasi, String first_time_stamp,
                                   String last_time_stamp, String tanggal_pakai, String durasi_pakai) {
        this.id = id;
        this.nama_aplikasi = nama_aplikasi;
        this.first_time_stamp = first_time_stamp;
        this.last_time_stamp = last_time_stamp;
        this.tanggal_pakai = tanggal_pakai;
        this.durasi_pakai = durasi_pakai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNama_aplikasi() {
        return nama_aplikasi;
    }

    void setNama_aplikasi(String nama_aplikasi) {
        this.nama_aplikasi = nama_aplikasi;
    }

    public String getFirst_time_stamp() {
        return first_time_stamp;
    }

    void setFirst_time_stamp(String first_time_stamp) {
        this.first_time_stamp = first_time_stamp;
    }

    public String getLast_time_stamp() {
        return last_time_stamp;
    }

    void setLast_time_stamp(String last_time_stamp) {
        this.last_time_stamp = last_time_stamp;
    }

    String getTanggal_pakai() {
        return tanggal_pakai;
    }

    void setTanggal_pakai(String tanggal_pakai) {
        this.tanggal_pakai = tanggal_pakai;
    }

    public String getDurasi_pakai() {
        return durasi_pakai;
    }

    void setDurasi_pakai(String durasi_pakai) {
        this.durasi_pakai = durasi_pakai;
    }
}
