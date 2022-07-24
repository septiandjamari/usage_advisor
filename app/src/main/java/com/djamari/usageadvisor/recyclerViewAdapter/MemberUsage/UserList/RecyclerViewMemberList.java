package com.djamari.usageadvisor.recyclerViewAdapter.MemberUsage.UserList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.helper.Helper;
import com.djamari.usageadvisor.otherActivity.MemberUsage.MemberUsage_More;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMemberList extends RecyclerView.Adapter<RecyclerViewMemberList.ViewHolder> {
    public List<SetterMemberList> listItems;
    public Context context;

    public RecyclerViewMemberList(List<SetterMemberList> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_member_list, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SetterMemberList listItem = listItems.get(position);
        holder.card_username.setText(listItem.username);
        holder.terakhir_dilihat.setText("Terakhir dilihat : " + listItem.terakhir_aktif);
        holder.member_durasi_layar.setText("Durasi layar hidup : " + Helper.getDuration(listItem.durasi_layar * 1000));
        holder.member_kunci_layar.setText("Kunci layar dibuka : " + listItem.buka_kunci + " kali");
        holder.member_interaksi_layar.setText("Interaksi Aplikasi : " + listItem.interaksi_aplikasi + " kali");
        holder.userID.setText(listItem.UserId);

        holder.recyclerview_member_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: recyclerview_member_list" + context.toString());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(holder.card_username.getText().toString() + " :");
                alertDialog.setMessage("Lihat lebih detail penggunaan aplikasi dari user :");
                alertDialog.setNegativeButton("KOLEKSI WAKTU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MemberUsage_More.class);
                        intent.putExtra("UsageMoreState", "koleksiAplikasi");
                        intent.putExtra("UsernameMember", holder.card_username.getText().toString());
                        intent.putExtra("UserID", holder.userID.getText().toString());
                        context.startActivity(intent);
                    }
                });
                alertDialog.setPositiveButton("DAFTAR APLIKASI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MemberUsage_More.class);
                        intent.putExtra("UsageMoreState", "daftarAplikasi");
                        intent.putExtra("UsernameMember", holder.card_username.getText().toString());
                        intent.putExtra("UserID", holder.userID.getText().toString());
                        context.startActivity(intent);
                    }
                });
                alertDialog.setNeutralButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout recyclerview_member_list;
        TextView userID, card_username, member_durasi_layar, member_kunci_layar, member_interaksi_layar, terakhir_dilihat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerview_member_list = itemView.findViewById(R.id.recyclerview_member_list);
            userID = itemView.findViewById(R.id.card_member_idUser);
            card_username = itemView.findViewById(R.id.card_member_username);
            member_durasi_layar = itemView.findViewById(R.id.card_member_durasi_layar);
            member_kunci_layar = itemView.findViewById(R.id.card_member_kunci_layar);
            member_interaksi_layar = itemView.findViewById(R.id.card_member_interaksi_layar);
            terakhir_dilihat = itemView.findViewById(R.id.card_member_terakhir_dilihat);
        }
    }
}
