package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UserList;

public class SetterMemberList {
    public String UserId;
    public String username;
    public int durasi_layar;
    public int buka_kunci;
    public int interaksi_aplikasi;
    public String terakhir_aktif;

    public SetterMemberList(String UserId, String username, int durasi_layar, int buka_kunci, int interaksi_aplikasi, String terakhir_aktif) {
        this.UserId = UserId;
        this.username = username;
        this.durasi_layar = durasi_layar;
        this.buka_kunci = buka_kunci;
        this.interaksi_aplikasi = interaksi_aplikasi;
        this.terakhir_aktif = terakhir_aktif;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDurasi_layar() {
        return durasi_layar;
    }

    public void setDurasi_layar(int durasi_layar) {
        this.durasi_layar = durasi_layar;
    }

    public int getBuka_kunci() {
        return buka_kunci;
    }

    public void setBuka_kunci(int buka_kunci) {
        this.buka_kunci = buka_kunci;
    }

    public int getInteraksi_aplikasi() {
        return interaksi_aplikasi;
    }

    public void setInteraksi_aplikasi(int interaksi_aplikasi) {
        this.interaksi_aplikasi = interaksi_aplikasi;
    }
}
