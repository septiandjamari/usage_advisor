package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UsageMore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djamari.usageadvisor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewUserKoleksiAplikasi extends RecyclerView.Adapter<RecyclerViewUserKoleksiAplikasi.ViewHolder> {
    private List<SetterUserKoleksiAplikasi> listItems;
    public Context context;

    public RecyclerViewUserKoleksiAplikasi(List<SetterUserKoleksiAplikasi> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_usage_member_app_collection, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SetterUserKoleksiAplikasi listItem = listItems.get(position);
        holder.no.setText(listItem.no + ". ");
        holder.namaAplikasi.setText(listItem.nama_aplikasi);
        holder.durasi.setText("Durasi : " + listItem.durasi);
        holder.startFinish.setText("Start : " + listItem.start + " - Finish : " + listItem.finish);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView no, namaAplikasi, durasi, startFinish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.recyclerview_nomer_app_collection);
            namaAplikasi = itemView.findViewById(R.id.recyclerview_koleksi_aplikasi_namaAplikasi);
            durasi = itemView.findViewById(R.id.recyclerview_koleksi_aplikasi_durasi);
            startFinish = itemView.findViewById(R.id.recyclerview_koleksi_aplikasi_startFinish);
        }
    }
}
