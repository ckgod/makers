package com.publicTA.template.src.LastBus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.LastBus.Adapter.LastAlarmAdapter;
import com.publicTA.template.src.LastBus.Adapter.LastAlarmItem;
import com.publicTA.template.src.main.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FragmentLastBus extends Fragment {
    public ProgressDialog mProgressDialog;
    private static final String LAST_ALARM_LIST = "lastAlarmList";
    int REQUST_CODE = 1;

    ImageButton mBtnAddLastBus, mBtnModify, mBtnDrawer;
    Button mBtnModifyComplete;


    private RecyclerView mListViewAlarm;
    private LastAlarmAdapter lastAlarmAdapter;
    private ArrayList<LastAlarmItem> ArrayListAlarm = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_last_bus , container, false);
        mBtnAddLastBus = rootView.findViewById(R.id.last_bus_btn_add_alarm);
        mBtnModify = rootView.findViewById(R.id.last_bust_btn_modify);
        mBtnDrawer = rootView.findViewById(R.id.last_bus_btn_nv_view);
        mBtnModifyComplete = rootView.findViewById(R.id.last_bus_btn_modify_complete);
        mListViewAlarm = rootView.findViewById(R.id.last_bust_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mListViewAlarm.setLayoutManager(layoutManager);

        ArrayListAlarm = loadAlarmList();
        lastAlarmAdapter = new LastAlarmAdapter(ArrayListAlarm, getContext(),R.layout.last_bus_alarm_item);
        mListViewAlarm.setAdapter(lastAlarmAdapter);

        setButtonTools();
        return rootView;
    }

    public void setButtonTools() {
        mBtnAddLastBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddLastBusActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUST_CODE); // 추후에 startActivityForResult 로 변경
            }
        });
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAlarmAdapter.visibleChange();
                lastAlarmAdapter.notifyDataSetChanged();
                mBtnModifyComplete.setVisibility(View.VISIBLE);
                mBtnModify.setVisibility(View.GONE);
                mBtnAddLastBus.setVisibility(View.GONE);
            }
        });
        mBtnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.openDrawer();
            }
        });
        mBtnModifyComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAlarmAdapter.visibleChange();
                lastAlarmAdapter.notifyDataSetChanged();
                mBtnAddLastBus.setVisibility(View.VISIBLE);
                mBtnModify.setVisibility(View.VISIBLE);
                mBtnModifyComplete.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                LastAlarmItem item = (LastAlarmItem) data.getSerializableExtra("lastBusAlarm");
                ArrayListAlarm.add(item);
                saveAlarmList(ArrayListAlarm);
                lastAlarmAdapter.notifyDataSetChanged();
        }
    }

    private void saveAlarmList(ArrayList<LastAlarmItem> alarmItemArrayList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmItemArrayList);
        editor.putString(LAST_ALARM_LIST, json);
        editor.apply();
    }

    private ArrayList<LastAlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(LAST_ALARM_LIST, "");
        ArrayList<LastAlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<LastAlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        Log.e("alarmArrayListSize", alarmItemArrayList.size()+"");
        return alarmItemArrayList;
    }
}
