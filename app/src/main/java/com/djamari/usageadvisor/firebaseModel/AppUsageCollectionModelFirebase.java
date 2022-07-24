package com.djamari.usageadvisor.firebaseModel;

public class AppUsageCollectionModelFirebase {
    private String nama_aplikasi, firstTimeStamp, lastTimeStamp, durasiPemakaian;

    public AppUsageCollectionModelFirebase(String nama_aplikasi, String firstTimeStamp, String lastTimeStamp, String durasiPemakaian){
        this.nama_aplikasi = nama_aplikasi;
        this.firstTimeStamp = firstTimeStamp;
        this.lastTimeStamp = lastTimeStamp;
        this.durasiPemakaian = durasiPemakaian;
    }

    public String getNama_aplikasi() {
        return nama_aplikasi;
    }

    public void setNama_aplikasi(String nama_aplikasi) {
        this.nama_aplikasi = nama_aplikasi;
    }

    public String getFirstTimeStamp() {
        return firstTimeStamp;
    }

    public void setFirstTimeStamp(String firstTimeStamp) {
        this.firstTimeStamp = firstTimeStamp;
    }

    public String getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(String lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public String getDurasiPemakaian() {
        return durasiPemakaian;
    }

    public void setDurasiPemakaian(String durasiPemakaian) {
        this.durasiPemakaian = durasiPemakaian;
    }
}
