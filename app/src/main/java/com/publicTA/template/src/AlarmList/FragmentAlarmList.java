package com.publicTA.template.src.AlarmList;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.main.MainActivity;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.AlarmList.Adapter.AlarmListAdapter;
import com.publicTA.template.src.AddAlarm.AddAlarmActivity;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.AlarmList.interfaces.FragmentAlarmListView;
import com.publicTA.template.src.AlarmList.models.ResponseArrivalTime;
import com.publicTA.template.src.AlarmList.services.FragmentAlarmListService;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class FragmentAlarmList extends Fragment implements FragmentAlarmListView {
    public AlertDialog mProgressDialog;
    private static final String ALARMLIST = "alarmList";
    int REQUST_CODE = 1;

    private RecyclerView mAlarmListVIew;
    AlarmListAdapter mAdapter;
    ArrayList<AlarmItem> mItemArrayList = new ArrayList<>();

    LinearLayout mTopBar;
    View mTopLine;

    ImageButton mBtnAddAlarm, mBtnModify, mBtnDrawer;
    Button mBtnModifyComplete;

    RelativeLayout background;
    ImageView backImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm_list , container, false);
        mAlarmListVIew = rootView.findViewById(R.id.alarm_list_list_view);
        mBtnAddAlarm = rootView.findViewById(R.id.alarm_list_btn_add_alarm);
        mBtnModify = rootView.findViewById(R.id.alarm_list_btn_modify);
        mTopBar = rootView.findViewById(R.id.alarm_list_ll_top_bar);
        mTopLine = rootView.findViewById(R.id.alarm_list_top_line);
        mBtnDrawer = rootView.findViewById(R.id.alarm_list_btn_nv_view);
        mBtnModifyComplete = rootView.findViewById(R.id.alarm_list_btn_modify_complete);
        background = rootView.findViewById(R.id.alarm_list_background);
        backImg = rootView.findViewById(R.id.alarm_list_none_item_back);
        mTopBar.bringToFront();
        mTopLine.bringToFront();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mAlarmListVIew.setLayoutManager(layoutManager);

        mItemArrayList = loadAlarmList();
        F5(mItemArrayList);

        mAdapter = new AlarmListAdapter(mItemArrayList,getContext(),R.layout.alarm_list_item);
        mAlarmListVIew.setAdapter(mAdapter);

        setButtonTools();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mItemArrayList.size() == 0) {
            backImg.setVisibility(View.VISIBLE);
            background.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorYellow));
        }
        else {
            backImg.setVisibility(View.GONE);
            background.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
        }
        F5(mItemArrayList);
    }

    void setButtonTools () {
        mBtnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUST_CODE); // 추후에 startActivityForResult 로 변경
            }
        });
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.visibleChange();
                mAdapter.notifyDataSetChanged();
                mBtnModifyComplete.setVisibility(View.VISIBLE);
                mBtnModify.setVisibility(View.GONE);
                mBtnAddAlarm.setVisibility(View.GONE);
            }
        });
        mBtnModifyComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.visibleChange();
                mAdapter.notifyDataSetChanged();
                mBtnAddAlarm.setVisibility(View.VISIBLE);
                mBtnModify.setVisibility(View.VISIBLE);
                mBtnModifyComplete.setVisibility(View.GONE);
            }
        });
        mBtnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.openDrawer();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                AlarmItem item = (AlarmItem) data.getSerializableExtra("alarmItem");
                mItemArrayList.add(item);
                saveAlarmList(mItemArrayList);
                F5(mItemArrayList);
                mAdapter.notifyDataSetChanged();
        }
    }

    private void saveAlarmList(ArrayList<AlarmItem> alarmItemArrayList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmItemArrayList);
        editor.putString(ALARMLIST, json);
        editor.apply();
    }

    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(ALARMLIST, "");
        ArrayList<AlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<AlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        Log.e("alarmArrayListSize", alarmItemArrayList.size()+"");
        return alarmItemArrayList;
    }

    public void F5(ArrayList<AlarmItem> alarmItemArrayList) {
        for(int i = 0; i < alarmItemArrayList.size(); i++) {
            String stId = "";
            String trId = "";
            String nextStId = "";
            for(int j = 0; j < alarmItemArrayList.get(i).getPathItemArrayList().size(); j++) {
                PathItem item = alarmItemArrayList.get(i).getPathItemArrayList().get(j);
                if(j == alarmItemArrayList.get(i).getPathItemArrayList().size() - 1) {
                    stId += item.getStationId();
                    trId += item.getTransitId();
                    nextStId += item.getNextStationId();
                }
                else {
                    stId += item.getStationId() + ",";
                    trId += item.getTransitId() + ",";
                    nextStId += item.getNextStationId() + ",";
                }
                Log.e("[Log.e] item id", item.getStationId() + " " + item.getTransitId() + " " + item.getNextStationId());
            }
            tryGetArrivalTime(stId, trId, nextStId, i);
        }
    }

    public void tryGetArrivalTime(String stId, String trId, String nextStId, int alarmIdx) {
        showProgressDialog();
        FragmentAlarmListService service = new FragmentAlarmListService(this);
        //Log.e("tryGetArrivalTime", stId.size() + " " + trId.size() + " " + nextStId.size());
        service.getArrivalTime(stId, trId, nextStId, alarmIdx);
    }

    @Override
    public void getStationSuccess(ArrayList<ResponseArrivalTime.arrivalTime> result, int alarmIdx) {
        for(int i = 0; i < result.size(); i++) {
            Log.e("[Log.e] resultInfo", "result size : " + result.size() + "\n" +
                    "StName : " + result.get(i).getStName() +  "\n" +
                    "trName : " + result.get(i).getTrName() + "\n" +
                    "transitType : " + result.get(i).getType() + "\n" +
                    "leftTime : " + result.get(i).getLeftTime() + "\n" +
                    "secondLeftTime : " + result.get(i).getSecondLeftTime() + "\n" +
                    "leftStation : " + result.get(i).getLocation() + "\n" +
                    "secondLeftStation : " + result.get(i).getSecondLocation() + "\n");
            if(result.get(i).getType() == 1) { // note 버스일때
                Log.e("[Log.e] stName", result.get(i).getStName());
                mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setLeftTime(result.get(i).getLeftTime());
                mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setSecondLeftTime(result.get(i).getSecondLeftTime());
            }
            else if (result.get(i).getType() == 2) { // note 지하철일때
                Log.e("[Log.e] stName", result.get(i).getStName());
                if(result.get(i).getSubwayType() == 1) {
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setSubwayType(1);
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setLeftTime(result.get(i).getLeftTime());
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setSecondLeftTime(result.get(i).getSecondLeftTime());
                }
                else if(result.get(i).getSubwayType() == 2) {
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setSubwayType(2);
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setLeftStation(result.get(i).getLocation());
                    mItemArrayList.get(alarmIdx).getPathItemArrayList().get(i).setSecondLeftStation(result.get(i).getSecondLocation());
                }
            }
        }
        saveAlarmList(mItemArrayList);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    public void getStationFailure(String message) {
        hideProgressDialog();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.customProgressDialog).build();
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
