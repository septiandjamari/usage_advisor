package com.djamari.usageadvisor.recyclerViewAdapter.PersonalUsage.AppCollection;

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

public class RecyclerViewAppUsageCollection extends RecyclerView.Adapter<RecyclerViewAppUsageCollection.ViewHolder> {

    public List<SetterAppUsageCollection> listItems;
    public Context context;

    public RecyclerViewAppUsageCollection(List<SetterAppUsageCollection> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_app_usage_personal_detailed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SetterAppUsageCollection listItem = listItems.get(position);

        holder.icon.setImageDrawable(listItem.getThumbnail());
        holder.nameApp.setText(listItem.getNameApp());
        holder.durasi.setText(listItem.getDurasiPenggunaan());
        holder.fisrtTimeStamp.setText(listItem.getFirstTimeStamp());
        holder.lastTimeStamp.setText(listItem.getLastTimeStamp());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView nameApp, fisrtTimeStamp, lastTimeStamp, durasi;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameApp = itemView.findViewById(R.id.list_item_detailed_text);
            icon = itemView.findViewById(R.id.list_item_detailed_thumbnail);
            durasi = itemView.findViewById(R.id.list_item_detailed_durasi);
            fisrtTimeStamp = itemView.findViewById(R.id.list_item_detailed_first_time_stamp);
            lastTimeStamp = itemView.findViewById(R.id.list_item_detailed_last_time_stamp);
        }
    }
}
