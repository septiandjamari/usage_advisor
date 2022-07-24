package com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.DaftarAplikasi;

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

public class RecyclerViewDaftarAplikasi extends RecyclerView.Adapter<RecyclerViewDaftarAplikasi.ViewHolder> {

    private List<SetterDaftarAplikasi> listItems;
    private Context context;

    public RecyclerViewDaftarAplikasi(List<SetterDaftarAplikasi> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewDaftarAplikasi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_app_usage_personal, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDaftarAplikasi.ViewHolder holder, int position) {
        final SetterDaftarAplikasi listItem = listItems.get(position);

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

            nameApp = itemView.findViewById(R.id.list_item_text);
            icon = itemView.findViewById(R.id.list_item_thumbnail);
            durasi = itemView.findViewById(R.id.list_item_durasi);
            frekuensi = itemView.findViewById(R.id.list_item_frekuensi);
            terakhirDilihat = itemView.findViewById(R.id.list_item_terakhir_dipakai);

        }
    }
}
