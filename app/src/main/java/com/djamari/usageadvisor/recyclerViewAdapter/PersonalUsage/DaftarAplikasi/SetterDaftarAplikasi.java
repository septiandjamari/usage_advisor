package com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.DaftarAplikasi;

import android.graphics.drawable.Drawable;

public class SetterDaftarAplikasi {

    private Drawable thumbnail;
    private String nameApp, durasiPenggunaan, frekuensiPenggunaan, terakhir_digunakan;


    public SetterDaftarAplikasi(Drawable thumbnail, String nameApp, String durasiPenggunaan, String frekuensiPenggunaan, String terakhir_digunakan) {
        this.thumbnail = thumbnail;
        this.nameApp = nameApp;
        this.durasiPenggunaan = durasiPenggunaan;
        this.frekuensiPenggunaan = frekuensiPenggunaan;
        this.terakhir_digunakan = terakhir_digunakan;
    }

    Drawable getThumbnail() {
        return thumbnail;
    }

    String getNameApp() {
        return nameApp;
    }

    String getDurasiPenggunaan() {
        return durasiPenggunaan;
    }

    String getFrekuensiPenggunaan() {
        return frekuensiPenggunaan;
    }

    String getTerakhir_digunakan() {
        return terakhir_digunakan;
    }
}
