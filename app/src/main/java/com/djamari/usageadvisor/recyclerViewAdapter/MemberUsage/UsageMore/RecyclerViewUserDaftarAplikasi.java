package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewUserDaftarAplikasi extends RecyclerView.Adapter<RecyclerViewUserDaftarAplikasi.ViewHolder> {
    private List<SetterUserDaftarAplikasi> listItems;
    public Context context;

    public RecyclerViewUserDaftarAplikasi(List<SetterUserDaftarAplikasi> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_usage_member_app_list, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SetterUserDaftarAplikasi listItem = listItems.get(position);
        holder.no.setText(listItem.no + ". ");
        holder.namaAplikasi.setText(listItem.namaAplikasi);
        holder.terakhirDipakai.setText("Terakhir dipakai : " + listItem.terakhirDipakai);
        holder.durasi.setText(("Durasi Pemakaian : " + Helper.getDuration((long) listItem.durasi)));
        holder.frekuensi.setText("Frekuensi Pemakaian : " + listItem.frekuensi + " kali");
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView no, namaAplikasi, terakhirDipakai, durasi, frekuensi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.recyclerview_nomer_app_list);
            namaAplikasi = itemView.findViewById(R.id.recyclerview_daftar_aplikasi_namaAplikasi);
            terakhirDipakai = itemView.findViewById(R.id.recyclerview_daftar_aplikasi_terakhir_dipakai);
            durasi = itemView.findViewById(R.id.recyclerview_daftar_aplikasi_durasi);
            frekuensi = itemView.findViewById(R.id.recyclerview_daftar_aplikasi_frekuensi);
        }
    }
}
