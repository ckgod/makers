package com.publicTA.template.src.currentState.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;

import java.util.ArrayList;

public class CurStateAdapter extends RecyclerView.Adapter<CurStateAdapter.ViewHolder>{
    private static final String ALARMLIST = "alarmList";

    ArrayList<PathItem> mList = new ArrayList<>();
    Context context;
    int layout;

    public CurStateAdapter(ArrayList<PathItem> mList, Context context, int layout) {
        this.mList = mList;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public CurStateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.current_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PathItem item = mList.get(position);
        holder.transitName.setText(item.getTransitName());
        if(item.getTransitType() == 1) {
            if(item.getLeftTime() == 0) {
                holder.leftTime.setText("도착정보없음");
                holder.leftTime.setTextSize(14);
            }
            else {
                holder.leftTime.setText(item.getLeftTime() + "분");
            }
            if(item.getSecondLeftTime() == 0) {
                holder.secondLeftTime.setText("도착정보없음");
            }
            else {
                holder.secondLeftTime.setText(item.getSecondLeftTime() + "분");
            }
        }
        else {
            if(item.getSubwayType() == 1) {
                if(item.getLeftTime() == 0) {
                    holder.leftTime.setText("도착정보없음");
                    holder.leftTime.setTextSize(14);
                }
                else {
                    holder.leftTime.setText(item.getLeftTime() + "분");
                }
                if(item.getSecondLeftTime() == 0) {
                    holder.secondLeftTime.setText("도착정보없음");
                }
                else {
                    holder.secondLeftTime.setText(item.getSecondLeftTime() + "분");
                }
            }
            else if(item.getSubwayType() == 2) {
                if(item.getLeftStation() == 0) {
                    holder.leftTime.setText("도착정보없음");
                    holder.leftTime.setTextSize(14);
                }
                else {
                    holder.leftTime.setText(item.getLeftStation() + "정거장");
                    holder.leftTime.setTextSize(20);
                }
                if(item.getSecondLeftStation() == 0) {
                    holder.secondLeftTime.setText("도착정보없음");
                    holder.secondLeftTime.setTextSize(14);
                }
                else {
                    holder.secondLeftTime.setText(item.getSecondLeftStation() + "정거장");
                    holder.secondLeftTime.setTextSize(20);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView transitName;
        private TextView leftTime;
        private TextView secondLeftTime;


        public ViewHolder(View itemView) {
            super(itemView);
            transitName = itemView.findViewById(R.id.cur_item_transit_name);
            leftTime = itemView.findViewById(R.id.cur_item_last_time);
            secondLeftTime = itemView.findViewById(R.id.cur_item_second_last_time);
        }
    }

}
