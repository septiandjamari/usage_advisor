package com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.AppCollection;

import android.graphics.drawable.Drawable;

public class SetterAppUsageCollection {

    private Drawable thumbnail;
    private String nameApp, firstTimeStamp, lastTimeStamp, durasiPenggunaan;

    public SetterAppUsageCollection(Drawable thumbnail, String nameApp, String firstTimeStamp, String lastTimeStamp, String durasiPenggunaan) {
        this.thumbnail = thumbnail;
        this.nameApp = nameApp;
        this.firstTimeStamp = firstTimeStamp;
        this.lastTimeStamp = lastTimeStamp;
        this.durasiPenggunaan = durasiPenggunaan;
    }

    Drawable getThumbnail() {
        return thumbnail;
    }

    String getNameApp() {
        return nameApp;
    }

    String getFirstTimeStamp() {
        return firstTimeStamp;
    }

    String getLastTimeStamp() {
        return lastTimeStamp;
    }

    String getDurasiPenggunaan() {
        return durasiPenggunaan;
    }

}
