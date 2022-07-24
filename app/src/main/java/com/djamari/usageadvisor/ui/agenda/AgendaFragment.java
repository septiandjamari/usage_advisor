package com.djamari.usageadvisor.ui.agenda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.djamari.usageadvisor.database.Agenda.DailyAgendaDatabase;
import com.djamari.usageadvisor.database.Agenda.DailyAgendaModel;
import com.djamari.usageadvisor.otherActivity.Agenda.AddEditDailyAgendaActivity;
import com.djamari.usageadvisor.recyclerViewAdapter.Agenda.RecyclerViewAgenda;
import com.djamari.usageadvisor.recyclerViewAdapter.Agenda.SetterAgenda;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID;

public class AgendaFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<SetterAgenda> listItems = new ArrayList<>();
    private boolean deleteStatus = false;
    private String getUniqueId = "";


    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("refreshStatus")) {
                AppBarLayout appBarLayout = requireActivity().findViewById(R.id.mainActionBar);
                appBarLayout.setExpanded(true, true);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim).replace(R.id.nav_host_fragment, new AgendaFragment()).commit();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID, 0);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_agenda, container, false);

        final LinearLayout mLinearLayout = root.findViewById(R.id.exists_data_agenda);
        final DailyAgendaDatabase db = new DailyAgendaDatabase(root.getContext());
        final FloatingActionButton fab = requireActivity().findViewById(R.id.button_add_agenda);

        final MaterialButton tombolKembalidetailed = requireActivity().findViewById(R.id.tombol_detail_kembali);
        tombolKembalidetailed.setVisibility(View.GONE);

        fab.setVisibility(VISIBLE);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), AddEditDailyAgendaActivity.class);
                i.putExtra("AddOrEditState", "Add");
                startActivity(i);
            }
        });

        recyclerView = root.findViewById(R.id.recyclerview_daftar_agenda_harian);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        loadAgendaCollection(requireActivity().getApplicationContext());

//        swipeAction();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                LinearLayout coordinatorLayout = requireActivity().findViewById(R.id.activity_snackbar_coordinator_layout);
                final int position = viewHolder.getAdapterPosition();
                mLinearLayout.setEnabled(false);
                deleteStatus = true;
                getUniqueId = ((TextView) Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.agenda_unique_id)).getText().toString();

                listItems.remove(position);
                adapter.notifyDataSetChanged();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Agenda dihapus dari daftar", Snackbar.LENGTH_SHORT);
                snackbar.setAction("BATALKAN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim).replace(R.id.nav_host_fragment, new AgendaFragment()).commit();
                        deleteStatus = false;
                    }
                }).setDuration(1500).setActionTextColor(Color.YELLOW).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (deleteStatus) {
                            Toast.makeText(getContext(), getUniqueId + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                            db.hapusAgenda(getUniqueId);
                            deleteStatus = false;
                        } else {
                            handler.removeCallbacks(this);
                        }
                    }
                }, 1500);
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (deleteStatus){
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.swipebin);
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#ff3333"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#ff3333"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }
        }).attachToRecyclerView(recyclerView);
        return root;
    }

    private void loadAgendaCollection(Context context) {
        DailyAgendaDatabase db = new DailyAgendaDatabase(context);
        List<DailyAgendaModel> getAgendaUniqueId = db.getAllUniqueId();
        List<String> listUniqueId = new ArrayList<>();
        for (DailyAgendaModel dam : getAgendaUniqueId) {
            if (!listUniqueId.contains(dam.getUniqueId())) {
                listUniqueId.add(dam.getUniqueId());
            }
        }
        //==========================================================================================
        for (String listIdUnique : listUniqueId) {
            Log.d("listItem : ", listIdUnique);
            List<DailyAgendaModel> getAgendaDetail = db.getAgendaDetail(listIdUnique);
            int hour = 0, minute = 0, repeat = 0, isActive = 0;
            String jamAgenda;
            String uniqueId = "";
            String name = "";
            StringBuilder hari = new StringBuilder(64);

            for (DailyAgendaModel dam : getAgendaDetail) {

                uniqueId = dam.getUniqueId();
                name = dam.getNamaAgenda();
                hour = dam.getHour();
                minute = dam.getMinute();
                repeat = dam.getRepeat();
                hari.append(dam.getHariString()).append("  ");
                isActive = dam.getSwicthAktif();
            }
            if (hour < 10 && minute < 10) {
                jamAgenda = "0" + hour + ":0" + minute;
            } else if (hour < 10 && minute > 10) {
                jamAgenda = "0" + hour + ":" + minute;
            } else if (hour > 10 && minute < 10) {
                jamAgenda = hour + ":0" + minute;
            } else {
                jamAgenda = hour + ":" + minute;
            }
            Log.d("testRecycler", "uniqueId : " + uniqueId + " Nama : " + name + ", Hari : " + hari.toString() + ", Jam : " + jamAgenda + ", ulangi : " + repeat + "Menit");
            SetterAgenda listItem = new SetterAgenda(uniqueId, name, hari.toString(), jamAgenda, repeat + " Menit", isActive);
            listItems.add(listItem);
            adapter = new RecyclerViewAgenda(listItems, getActivity());
            recyclerView.setAdapter(adapter);
        }

    }
}
