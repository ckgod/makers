package com.publicTA.template.src.AddAlarm.Path.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.FragmentAddPath;

import java.util.ArrayList;

public class PathListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_FOOTER = 2;
    private final int TYPE_ITEM = 3;

    private ArrayList<PathItem> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public PathListAdapter(Context context, ArrayList<PathItem> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder = null;
        View view;
        if(viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_item_footer,parent,false);
            holder = new FooterViewHolder(view);
        }
        else if(viewType == TYPE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_item,parent,false);
            holder = new ItemViewHolder(view);
        }
        //View view = LayoutInflater.from(context).inflate(R.layout.path_item,parent,false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        }
        else if(holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
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
            }
            else {
                itemViewHolder.transitType.setText("지하철");
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == itemList.size()) {
            return TYPE_FOOTER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView station;
        public TextView startStation;
        public TextView endStation;
        public TextView transitType;
        public TextView transitNum;


        public ItemViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.path_item_tv_station);
            startStation = itemView.findViewById(R.id.path_item_tv_start_station);
            endStation = itemView.findViewById(R.id.path_item_tv_end_station);
            transitType = itemView.findViewById(R.id.path_item_tv_transit_type);
            transitNum = itemView.findViewById(R.id.path_item_tv_transit_num);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout bottomSheet;
        FooterViewHolder(final View footerView) {
            super(footerView);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if(position > 4) {
                            Toast.makeText(context,"경로는 최대 5개까지 지정 가능합니다.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FragmentAddPath fragmentAddPath = new FragmentAddPath(1);
                        fragmentAddPath.mContext = context;
                        fragmentAddPath.show(((FragmentActivity)context).getSupportFragmentManager(),"BottomSheet");
                    }
                }
            });
        }
    }

    public void LogE(String msg) {
        Log.e("Log[Error] : ", msg);
    }
}
