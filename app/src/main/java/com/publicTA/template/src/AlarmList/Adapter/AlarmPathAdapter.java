package com.publicTA.template.src.AlarmList.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;

import java.util.ArrayList;

public class AlarmPathAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PathItem> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public AlarmPathAdapter(Context context, ArrayList<PathItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_path_item,parent,false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if(itemList.size() != 0) {
            PathItem item = itemList.get(position);
            String startStation = item.getStartStation();
            String endStation = item.getDirection();
            String transitNum = item.getTransitName();
            int transitType = item.getTransitType();

            itemViewHolder.station.setText("정류장/역");
            itemViewHolder.startStation.setText(startStation);
            if(item.getTransitType() == 1) {
                itemViewHolder.endStation.setText("→ " + endStation);
            }
            else if(item.getTransitType() == 2) {
                itemViewHolder.endStation.setText("→ " + item.getNextStationName());
            }
            itemViewHolder.transitNum.setText(transitNum);
            if(transitType == 1) {
                itemViewHolder.transitType.setText("버스");
                if(item.getLeftTime() == 0) {
                    itemViewHolder.leftTime.setText("도착정보 없음");
                    itemViewHolder.leftTime.setTextSize(13);
                }
                else {
                    itemViewHolder.leftTime.setText(item.getLeftTime() + "분");
                }
                if(item.getSecondLeftTime() == 0) {
                    itemViewHolder.secondLeftTime.setText("도착정보 없음");
                    itemViewHolder.secondLeftTime.setTextSize(13);
                }
                else {
                    itemViewHolder.secondLeftTime.setText(item.getSecondLeftTime() + "분");
                }
            }
            else {
                itemViewHolder.transitType.setText("지하철");
                if(item.getSubwayType() == 1) {
                    if(item.getLeftTime() == 0) {
                        itemViewHolder.leftTime.setText("도착정보 없음");
                        itemViewHolder.leftTime.setTextSize(13);
                    }
                    else {
                        itemViewHolder.leftTime.setText(item.getLeftTime() + "분");
                    }
                    if(item.getSecondLeftTime() == 0) {
                        itemViewHolder.secondLeftTime.setText("도착정보 없음");
                        itemViewHolder.secondLeftTime.setTextSize(13);
                    }
                    else {
                        itemViewHolder.secondLeftTime.setText(item.getSecondLeftTime() + "분");
                    }
                }
                else if(item.getSubwayType() == 2) {
                    if(item.getLeftStation() == 0) {
                        itemViewHolder.leftTime.setText("도착정보 없음");
                        itemViewHolder.leftTime.setTextSize(13);
                    }
                    else {
                        itemViewHolder.leftTime.setText(item.getLeftStation() + "정거장 전");
                    }
                    if(item.getSecondLeftStation() == 0) {
                        itemViewHolder.secondLeftTime.setText("도착정보 없음");
                        itemViewHolder.secondLeftTime.setTextSize(13);
                    }
                    else {
                        itemViewHolder.secondLeftTime.setText(item.getSecondLeftStation() + "정거장 전");
                    }
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView station;
        public TextView startStation;
        public TextView endStation;
        public TextView transitType;
        public TextView transitNum;
        public TextView leftTime, secondLeftTime;


        public ItemViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.alarm_path_item_tv_station);
            startStation = itemView.findViewById(R.id.alarm_path_item_tv_start_station);
            endStation = itemView.findViewById(R.id.alarm_path_item_tv_end_station);
            transitType = itemView.findViewById(R.id.alarm_path_item_tv_transit_type);
            transitNum = itemView.findViewById(R.id.alarm_path_item_tv_transit_num);
            leftTime = itemView.findViewById(R.id.alarm_path_item_tv_left_time);
            secondLeftTime = itemView.findViewById(R.id.alarm_path_item_tv_second_left_time);
        }
    }


    public void LogE(String msg) {
        Log.e("Log[Error] : ", msg);
    }
}
