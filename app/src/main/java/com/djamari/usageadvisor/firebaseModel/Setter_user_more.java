package com.djamari.usageadvisor.firebaseModel;

public class Setter_user_more {
    public String id_username;
    public String username;
    public String email;
    public String akses_pengguna;
    public String id_group;
    public int buka_kunci;
    public int durasi_layar;
    public int interaksi_aplikasi;

    public Setter_user_more(String id_username, String username, String email, String akses_pengguna, String id_group, int buka_kunci, int durasi_layar, int interaksi_aplikasi) {
        this.id_username = id_username;
        this.username = username;
        this.email = email;
        this.akses_pengguna = akses_pengguna;
        this.id_group = id_group;
        this.buka_kunci = buka_kunci;
        this.durasi_layar = durasi_layar;
        this.interaksi_aplikasi = interaksi_aplikasi;
    }
}
