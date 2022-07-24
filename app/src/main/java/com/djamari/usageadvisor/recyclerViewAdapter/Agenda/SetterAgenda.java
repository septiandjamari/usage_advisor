package com.djamari.usageadvisor.recyclerViewAdapter.Agenda;

public class SetterAgenda {
    private String uniqueId, namaAgenda, namaHari, jam, repeat;
    private int isActive;

    public SetterAgenda(String uniqueId, String namaAgenda, String namaHari, String jam, String repeat, int isActive) {
        this.uniqueId = uniqueId;
        this.namaAgenda = namaAgenda;
        this.namaHari = namaHari;
        this.jam = jam;
        this.repeat = repeat;
        this.isActive = isActive;
    }

    int getIsActive() {
        return isActive;
    }

    String getNamaAgenda() {
        return namaAgenda;
    }


    String getNamaHari() {
        return namaHari;
    }

    String getJam() {
        return jam;
    }

    String getRepeat() {
        return repeat;
    }

    String getUniqueId() {
        return uniqueId;
    }
}
