package com.djamari.usageadvisor.database.PersonalUsageDatabase;

public class DaftarAplikasiModel {
    private int id;
    private String nama_aplikasi;
    private long durasi_pakai;
    private int frekuensi_pakai;
    private String tanggal_pakai;
    private String terakhir_dilihat;

    DaftarAplikasiModel(){

    }
    public DaftarAplikasiModel(int id, String nama_aplikasi, long durasi_pakai, int frekuensi_pakai,
                               String tanggal_pakai, String terakhir_dilihat){
        this.id = id;
        this.nama_aplikasi = nama_aplikasi;
        this.durasi_pakai = durasi_pakai;
        this.frekuensi_pakai = frekuensi_pakai;
        this.tanggal_pakai = tanggal_pakai;
        this.terakhir_dilihat = terakhir_dilihat;
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

    public long getDurasi_pakai() {
        return durasi_pakai;
    }

    void setDurasi_pakai(long durasi_pakai) {
        this.durasi_pakai = durasi_pakai;
    }

    public int getFrekuensi_pakai() {
        return frekuensi_pakai;
    }

    void setFrekuensi_pakai(int frekuensi_pakai) {
        this.frekuensi_pakai = frekuensi_pakai;
    }

    String getTanggal_pakai() {
        return tanggal_pakai;
    }

    void setTanggal_pakai(String tanggal_pakai) {
        this.tanggal_pakai = tanggal_pakai;
    }

    public String getTerakhir_dilihat() {
        return terakhir_dilihat;
    }

    void setTerakhir_dilihat(String terakhir_dilihat) {
        this.terakhir_dilihat = terakhir_dilihat;
    }
}
