package com.publicTA.template.src.AlarmList.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.testNotification.AlarmSet;
import com.suke.widget.SwitchButton;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    private static final String ALARMLIST = "alarmList";

    ArrayList<AlarmItem> mList = new ArrayList<>();
    Context context;
    int layout;
    int mOffColor = 0;
    int mOnColor = 0;
    int mCardOffColor = 0;
    int mCardOnColor = 0;

    boolean visibleDelete = false;

    public AlarmListAdapter(ArrayList<AlarmItem> mList, Context context, int layout) {
        this.mList = mList;
        this.context = context;
        this.layout = layout;
        mOffColor = ContextCompat.getColor(this.context, R.color.colorBlack);
        mOnColor = ContextCompat.getColor(this.context, R.color.colorWhite);
        mCardOffColor = ContextCompat.getColor(this.context, R.color.colorWhite);
        mCardOnColor = ContextCompat.getColor(this.context, R.color.colorYellow);
    }

    @NonNull
    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmListAdapter.ViewHolder holder, final int position) {
        final AlarmItem item = mList.get(position);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        holder.mRecyclerViewPathList.setLayoutManager(layoutManager);
        //note child list 변수들
        ArrayList<PathItem> mPathItemList = item.getPathItemArrayList();
        AlarmPathAdapter mPathAdapter = new AlarmPathAdapter(context, mPathItemList);
        holder.mRecyclerViewPathList.setAdapter(mPathAdapter);
        mPathAdapter.notifyDataSetChanged();

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < mList.size(); i++) {
                    new AlarmSet(context).AlarmCancel(i, 1);
                }
                mList.remove(position);
                saveAlarmList(mList);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mList.size());
                for(int i = 0; i < mList.size(); i++) {
                    if(mList.get(i).isOnOff()) {
                        new AlarmSet(context).Alarm(i, 1);
                    }
                }
            }
        });

        holder.sw_onOFF.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    item.setOnOff(true);
                    saveAlarmList(mList);
                    holder.tv_start_ampm.setTextColor(mOnColor);
                    holder.tv_start_time.setTextColor(mOnColor);
                    holder.tv_end_ampm.setTextColor(mOnColor);
                    holder.tv_end_time.setTextColor(mOnColor);
                    holder.frontCard.setCardBackgroundColor(mCardOnColor);
                    new AlarmSet(context).Alarm(position, 1);
                }
                else {
                    item.setOnOff(false);
                    saveAlarmList(mList);
                    holder.tv_start_ampm.setTextColor(mOffColor);
                    holder.tv_start_time.setTextColor(mOffColor);
                    holder.tv_end_ampm.setTextColor(mOffColor);
                    holder.tv_end_time.setTextColor(mOffColor);
                    holder.frontCard.setCardBackgroundColor(mCardOffColor);
                    new AlarmSet(context).AlarmCancel(position,1);
                }
            }
        });

        if(item.isOnOff()) {
            holder.sw_onOFF.setChecked(true);
            holder.tv_start_ampm.setTextColor(mOnColor);
            holder.tv_start_time.setTextColor(mOnColor);
            holder.tv_end_ampm.setTextColor(mOnColor);
            holder.tv_end_time.setTextColor(mOnColor);
            holder.frontCard.setCardBackgroundColor(mCardOnColor);
            new AlarmSet(context).Alarm(position, 1);
        }
        else {
            holder.sw_onOFF.setChecked(false);
            holder.tv_start_ampm.setTextColor(mOffColor);
            holder.tv_end_time.setTextColor(mOffColor);
            holder.tv_end_ampm.setTextColor(mOffColor);
            holder.tv_end_time.setTextColor(mOffColor);
            holder.frontCard.setCardBackgroundColor(mCardOffColor);
            new AlarmSet(context).AlarmCancel(position, 1);
        }
        //note -------------------------시작 오전 오후
        if(item.getStartAMPMType() == 1) {
            holder.tv_start_ampm.setText("AM");
        }
        else {
            holder.tv_start_ampm.setText("PM");
        }

        //note ------------------------시작 시간
        String startHours, startMinute;
        if(item.getStartHours() >= 0 && item.getStartHours() <= 9) {
            startHours = "0" + item.getStartHours();
        }
        else {
            startHours = String.valueOf(item.getStartHours());
        }
        if(item.getStartMinutes() >= 0 && item.getStartMinutes() <= 9) {
            startMinute = "0" + item.getStartMinutes();
        }
        else {
            startMinute = String.valueOf(item.getStartMinutes());
        }
        String startTime = String.valueOf(startHours + ":" + startMinute);
        holder.tv_start_time.setText(startTime);

        // note-------------끝 오전 오후
        if(item.getEndAMPMType() == 1) {
            holder.tv_end_ampm.setText("AM");
        }
        else {
            holder.tv_end_ampm.setText("PM");
        }

        //note ----------------끝 시간
        String endHours, endMinute;
        if(item.getEndHours() >= 0 && item.getEndHours() <= 9) {
            endHours = "0" + item.getEndHours();
        }
        else {
            endHours = String.valueOf(item.getEndHours());
        }
        if(item.getEndMinutes() >= 0 && item.getEndMinutes() <= 9) {
            endMinute = "0" + item.getEndMinutes();
        }
        else {
            endMinute = String.valueOf(item.getEndMinutes());
        }
        String endTime = String.valueOf(endHours + ":" + endMinute);
        holder.tv_end_time.setText(endTime);

        //note -------------------------레이블
        holder.tv_lable.setText("#"+item.getLabel());

        //note -----------------------반복주기
        if(item.getDayCycle().length() == 7 || item.getDayCycle().length() == 0) {
            holder.tv_cycle.setText("매일");
        } else {
            holder.tv_cycle.setText(item.getDayCycle());
        }
        //note -----------------------------사운드
        holder.tv_sound.setText("진동");
        int cnt = item.getPathItemArrayList().size()-1;
        holder.tv_count.setText("환승" + cnt);

        if(!visibleDelete) {
            holder.btnDelete.setVisibility(View.GONE);
        }
        else {
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.slr_anim);
            holder.btnDelete.startAnimation(animation);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_start_ampm, tv_start_time, tv_end_ampm, tv_end_time, tv_lable, tv_cycle, tv_sound, tv_count;
        com.suke.widget.SwitchButton sw_onOFF;
        CardView frontCard;
        ImageButton btnDelete;
        RecyclerView mRecyclerViewPathList;
        net.cachapa.expandablelayout.ExpandableLayout mExpandable;
        boolean isExpanded = false;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_start_ampm = itemView.findViewById(R.id.alarm_list_item_tv_start_am_pm);
            tv_start_time = itemView.findViewById(R.id.alarm_list_item_tv_start_time);
            tv_end_ampm = itemView.findViewById(R.id.alarm_list_item_tv_end_am_pm);
            tv_end_time = itemView.findViewById(R.id.alarm_list_item_tv_end_time);
            tv_lable = itemView.findViewById(R.id.alarm_list_item_tv_label);
            tv_cycle = itemView.findViewById(R.id.alarm_list_item_tv_cycle);
            sw_onOFF = itemView.findViewById(R.id.alarm_list_item_on_off);
            tv_sound = itemView.findViewById(R.id.alarm_list_item_tv_sound);
            frontCard = itemView.findViewById(R.id.alarm_list_item_card);
            tv_count = itemView.findViewById(R.id.alarm_list_item_tv_count);
            btnDelete = itemView.findViewById(R.id.alarm_list_item_btn_delete);
            mRecyclerViewPathList = itemView.findViewById(R.id.alarm_list_item_path_list);
            mExpandable = itemView.findViewById(R.id.alarm_list_item_expandable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isExpanded) {
                        mExpandable.expand();
                        isExpanded = !isExpanded;
                    }
                    else {
                        mExpandable.collapse();
                        isExpanded = !isExpanded;
                    }
                }
            });
        }
    }

    public void visibleChange() {
        visibleDelete = !visibleDelete;
    }

    private void saveAlarmList(ArrayList<AlarmItem> alarmItemArrayList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmItemArrayList);
        editor.putString(ALARMLIST, json);
        editor.apply();
    }

    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(ALARMLIST, "");
        ArrayList<AlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<AlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        return alarmItemArrayList;
    }
}
