package com.djamari.usageadvisor.database.Agenda;

public class DailyAgendaModel {
    private int id, swicthAktif;
    private String uniqueId, namaAgenda, hariString;
    private int hour, minute, hariInt, repeat;

    public DailyAgendaModel(int id, String uniqueId, String namaAgenda, String hariString,
                            int hour, int minute, int hariInt, int repeat, int swicthAktif){
        this.id = id;
        this.uniqueId = uniqueId;
        this.namaAgenda = namaAgenda;
        this.hariString = hariString;
        this.hour = hour;
        this.minute = minute;
        this.hariInt = hariInt;
        this.repeat = repeat;
        this.swicthAktif = swicthAktif;

    }

    public int getSwicthAktif() {
        return swicthAktif;
    }

    void setSwicthAktif(int swicthAktif) {
        this.swicthAktif = swicthAktif;
    }

    DailyAgendaModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getNamaAgenda() {
        return namaAgenda;
    }

    void setNamaAgenda(String namaAgenda) {
        this.namaAgenda = namaAgenda;
    }

    public String getHariString() {
        return hariString;
    }

    void setHariString(String hariString) {
        this.hariString = hariString;
    }

    public int getHour() {
        return hour;
    }

    void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    void setMinute(int minute) {
        this.minute = minute;
    }

    int getHariInt() {
        return hariInt;
    }

    void setHariInt(int hariInt) {
        this.hariInt = hariInt;
    }

    public int getRepeat() {
        return repeat;
    }

    void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
