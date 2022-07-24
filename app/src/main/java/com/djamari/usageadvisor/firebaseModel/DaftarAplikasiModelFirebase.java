package com.djamari.usageadvisor.firebaseModel;

public class DaftarAplikasiModelFirebase {
    private String terakhir_dipakai;
    private long durasi_pakai;
    private int frekuensi_pakai;

    public DaftarAplikasiModelFirebase(String terakhir_dipakai, long durasi_pakai, int frekuensi_pakai){
        this.terakhir_dipakai = terakhir_dipakai;
        this.durasi_pakai = durasi_pakai;
        this.frekuensi_pakai = frekuensi_pakai;
    }

    public String getTerakhir_dipakai() {
        return terakhir_dipakai;
    }

    public void setTerakhir_dipakai(String terakhir_dipakai) {
        this.terakhir_dipakai = terakhir_dipakai;
    }

    public long getDurasi_pakai() {
        return durasi_pakai;
    }

    public void setDurasi_pakai(long durasi_pakai) {
        this.durasi_pakai = durasi_pakai;
    }

    public int getFrekuensi_pakai() {
        return frekuensi_pakai;
    }

    public void setFrekuensi_pakai(int frekuensi_pakai) {
        this.frekuensi_pakai = frekuensi_pakai;
    }
}
