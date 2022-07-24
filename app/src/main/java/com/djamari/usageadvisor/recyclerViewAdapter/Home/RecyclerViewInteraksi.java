package com.djamari.usageadvisor.recyclerViewAdapter.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djamari.usageadvisor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewInteraksi extends RecyclerView.Adapter<RecyclerViewInteraksi.ViewHolder> {

    private List<SetterInteraksi> listItems;
    public Context context;

    public RecyclerViewInteraksi(List<SetterInteraksi> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewInteraksi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_interaksi_aplikasi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewInteraksi.ViewHolder holder, int position) {
        final SetterInteraksi listItem = listItems.get(position);

        holder.icon.setImageDrawable(listItem.getThumbnail());
        holder.nameApp.setText(listItem.getNameApp());
        holder.durasi.setText(listItem.getDurasiPenggunaan());
        holder.frekuensi.setText(listItem.getFrekuensiPenggunaan());
        holder.terakhirDilihat.setText(listItem.getTerakhir_digunakan());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView nameApp, durasi, frekuensi, terakhirDilihat;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameApp = itemView.findViewById(R.id.namaAplikasiInteraksi);
            icon = itemView.findViewById(R.id.thumbnailInteraksi);
            durasi = itemView.findViewById(R.id.durasiInteraksi);
            frekuensi = itemView.findViewById(R.id.frekuensiInteraksi);
            terakhirDilihat = itemView.findViewById(R.id.terakhirDipakaiInteraksi);

        }
    }
}
