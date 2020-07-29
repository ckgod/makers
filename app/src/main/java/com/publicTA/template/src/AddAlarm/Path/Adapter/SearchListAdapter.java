package com.publicTA.template.src.AddAlarm.Path.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseStation;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {
    ArrayList<ResponseStation.Station> mList = new ArrayList<>();

    private Context context;
    private int layout;
    private LayoutInflater inf;
    private ViewHolder viewHolder;

    public SearchListAdapter(ArrayList<ResponseStation.Station> mList, Context context, int layout) {
        this.mList = mList;
        this.context = context;
        this.layout = layout;
        this.inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ResponseStation.Station getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = inf.inflate(layout, null);

            viewHolder = new ViewHolder();
            viewHolder.label = convertView.findViewById(R.id.station_list_item_tv_name);
            viewHolder.direction = convertView.findViewById(R.id.station_list_item_tv_direction);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.label.setText(mList.get(position).getStName());
        viewHolder.direction.setText("방향 → " + mList.get(position).getStLine());

        return convertView;
    }

    public class ViewHolder {
        public TextView label, busNum, direction;

    }
}
