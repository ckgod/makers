package com.publicTA.template.src.LastBus.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.publicTA.template.R;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.testNotification.AlarmSet;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

public class LastAlarmAdapter extends RecyclerView.Adapter<LastAlarmAdapter.ViewHolder> {
    private static final String LAST_ALARM_LIST = "lastAlarmList";

    private ArrayList<LastAlarmItem> mList = new ArrayList<>();
    private Context context;
    int layout;
    int mOffColor = 0;
    int mOnColor = 0;
    int mCardOffColor = 0;
    int mCardOnColor = 0;

    boolean visibleDelete = false;

    public LastAlarmAdapter(ArrayList<LastAlarmItem> mList, Context context, int layout) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.last_bus_alarm_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final LastAlarmItem item = mList.get(position);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < mList.size(); i++) {
                    new AlarmSet(context).AlarmCancel(i, 2);
                }
                mList.remove(position);
                saveAlarmList(mList);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mList.size());
                for(int i = 0; i < mList.size(); i++) {
                    if(mList.get(i).isOnOff()) {
                        new AlarmSet(context).Alarm(i, 2);
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
                    holder.tv_title.setTextColor(mOnColor);
                    holder.tv_station.setTextColor(mOnColor);
                    holder.line.setBackgroundColor(mOnColor);
                    holder.tv_direction.setTextColor(mOnColor);
                    holder.frontCard.setCardBackgroundColor(mCardOnColor);
                    new AlarmSet(context).Alarm(position, 2);
                }
                else {
                    item.setOnOff(false);
                    saveAlarmList(mList);
                    holder.tv_title.setTextColor(mOffColor);
                    holder.tv_station.setTextColor(mOffColor);
                    holder.line.setBackgroundColor(mOffColor);
                    holder.tv_direction.setTextColor(mOffColor);
                    holder.frontCard.setCardBackgroundColor(mCardOffColor);
                    new AlarmSet(context).AlarmCancel(position, 2);
                }
            }
        });

        if(item.isOnOff()) {
            holder.sw_onOFF.setChecked(true);
            holder.tv_title.setTextColor(mOnColor);
            holder.tv_station.setTextColor(mOnColor);
            holder.line.setBackgroundColor(mOnColor);
            holder.tv_direction.setTextColor(mOnColor);
            holder.frontCard.setCardBackgroundColor(mCardOnColor);
            new AlarmSet(context).Alarm(position, 2);
        }
        else {
            holder.sw_onOFF.setChecked(false);
            holder.tv_title.setTextColor(mOffColor);
            holder.tv_station.setTextColor(mOffColor);
            holder.line.setBackgroundColor(mOffColor);
            holder.tv_direction.setTextColor(mOffColor);
            holder.frontCard.setCardBackgroundColor(mCardOffColor);
            new AlarmSet(context).AlarmCancel(position, 2);
        }

        holder.tv_station.setText(item.getPathItem().getStartStation());
        holder.tv_direction.setText("→" + item.getPathItem().getDirection());
        if(item.getPathItem().getTransitType() == 1) {
            holder.tv_transit_type.setText("버스");
        }
        else {
            holder.tv_transit_type.setText("지하철");
        }
        holder.tv_transit_num.setText(item.getPathItem().getTransitName());
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
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_station, tv_direction, tv_transit_num, tv_transit_type, tv_title;
        com.suke.widget.SwitchButton sw_onOFF;
        CardView frontCard;
        ImageButton btnDelete;
        View line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.last_bus_item_tv_station);
            tv_station = itemView.findViewById(R.id.last_bus_item_tv_start_station);
            tv_direction = itemView.findViewById(R.id.last_bus_item_tv_end_station);
            tv_transit_num = itemView.findViewById(R.id.last_bus_item_tv_transit_num);
            tv_transit_type = itemView.findViewById(R.id.last_bus_item_transit_type);
            sw_onOFF = itemView.findViewById(R.id.last_bus_item_on_off);
            frontCard = itemView.findViewById(R.id.last_bus_item_card);
            btnDelete = itemView.findViewById(R.id.last_bus_item_btn_delete);
            line = itemView.findViewById(R.id.last_bus_item_line);
        }
    }

    public void visibleChange() {
        visibleDelete = !visibleDelete;
    }

    private void saveAlarmList(ArrayList<LastAlarmItem> alarmItemArrayList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmItemArrayList);
        editor.putString(LAST_ALARM_LIST, json);
        editor.apply();
    }
}
