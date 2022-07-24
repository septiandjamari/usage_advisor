package com.djamari.usageadvisor.recyclerViewAdapter.Agenda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.otherActivity.Agenda.AddEditDailyAgendaActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAgenda extends RecyclerView.Adapter<RecyclerViewAgenda.ViewHolder> {
    private List<SetterAgenda> listItems;
    private Context context;


    public RecyclerViewAgenda(List<SetterAgenda> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_agenda_harian, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SetterAgenda listItem = listItems.get(position);

        holder.uniqueId.setText(listItem.getUniqueId());
        holder.namaAgenda.setText(listItem.getNamaAgenda());
        holder.namaHari.setText(listItem.getNamaHari());
        holder.jam.setText(listItem.getJam());
        holder.repeat.setText(listItem.getRepeat());

        holder.recyclerview_agenda_harian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddEditDailyAgendaActivity.class);
                i.putExtra("AddOrEditState", "Edit");
                i.putExtra("uniqueId", listItem.getUniqueId());
                context.startActivity(i);
            }
        });
        if (listItem.getIsActive() == 0) {
            holder.pengingatTidakAktif.setVisibility(View.VISIBLE);
            holder.pengingatAktif.setVisibility(View.GONE);
        } else if (listItem.getIsActive() == 1) {
            holder.pengingatTidakAktif.setVisibility(View.GONE);
            holder.pengingatAktif.setVisibility(View.VISIBLE);
        }

        if (listItem.getRepeat().equals("0 Menit")) {
            holder.repeat.setVisibility(View.GONE);
            holder.repeat0.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout recyclerview_agenda_harian;
        TextView uniqueId, namaAgenda, namaHari, jam, repeat, pengingatTidakAktif, pengingatAktif, repeat0;
        ImageView badgeUlangi;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_agenda_harian = itemView.findViewById(R.id.recyclerview_agenda_harian);
            uniqueId = itemView.findViewById(R.id.agenda_unique_id);
            namaAgenda = itemView.findViewById(R.id.nama_agenda);
            namaHari = itemView.findViewById(R.id.nama_hari_agenda);
            jam = itemView.findViewById(R.id.jam_agenda);
            repeat = itemView.findViewById(R.id.repeat_agenda);
            repeat0 = itemView.findViewById(R.id.repeat_agenda_0);
            pengingatTidakAktif = itemView.findViewById(R.id.keterangan_tidak_aktif_agenda);
            pengingatAktif = itemView.findViewById(R.id.keterangan_aktif_agenda);
            badgeUlangi = itemView.findViewById(R.id.badge_repeat);
        }
    }
}
