package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore;

public class SetterUserDaftarAplikasi {
    public String namaAplikasi, terakhirDipakai;
    public long durasi, frekuensi;
    public int no;

    public SetterUserDaftarAplikasi(int no,String namaAplikasi, String terakhirDipakai, long durasi, long frekuensi) {
        this.no = no;
        this.namaAplikasi = namaAplikasi;
        this.terakhirDipakai = terakhirDipakai;
        this.durasi = durasi;
        this.frekuensi = frekuensi;
    }

    public String getNamaAplikasi() {
        return namaAplikasi;
    }

    public void setNamaAplikasi(String namaAplikasi) {
        this.namaAplikasi = namaAplikasi;
    }

    public String getTerakhirDipakai() {
        return terakhirDipakai;
    }

    public void setTerakhirDipakai(String terakhirDipakai) {
        this.terakhirDipakai = terakhirDipakai;
    }

    public long getDurasi() {
        return durasi;
    }

    public void setDurasi(long durasi) {
        this.durasi = durasi;
    }

    public long getFrekuensi() {
        return frekuensi;
    }

    public void setFrekuensi(long frekuensi) {
        this.frekuensi = frekuensi;
    }
}
