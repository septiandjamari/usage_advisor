package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore;

import com.google.firebase.firestore.Exclude;

public class SetterUserKoleksiAplikasi {
    public String documentId, nama_aplikasi, durasi, start, finish;
    public int no;

    public SetterUserKoleksiAplikasi(int no, String namaAplikasi, String durasi, String start, String finish) {
        this.no = no;
        this.nama_aplikasi = namaAplikasi;
        this.durasi = durasi;
        this.start = start;
        this.finish = finish;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getNamaAplikasi() {
        return nama_aplikasi;
    }

    public void setNamaAplikasi(String namaAplikasi) {
        this.nama_aplikasi = namaAplikasi;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
