package com.publicTA.template.src.AddAlarm.Path.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseBus;

import java.util.ArrayList;

public class SearchBusAdapter extends BaseAdapter {
    ArrayList<ResponseBus.Transit> mList = new ArrayList<>();

    private Context context;
    private int layout;
    private LayoutInflater inf;
    private ViewHolder viewHolder;

    public SearchBusAdapter(ArrayList<ResponseBus.Transit> mList, Context context, int layout) {
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
    public ResponseBus.Transit getItem(int position) {
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
            viewHolder.busName = convertView.findViewById(R.id.search_bus_tv_name);
            viewHolder.transitDirection = convertView.findViewById(R.id.search_bus_direction);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.busName.setText(mList.get(position).getName());
        if(mList.get(position).getNextName() == null) { // note bustype이 0 일때는 지하철
            viewHolder.transitDirection.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.transitDirection.setVisibility(View.VISIBLE);
            viewHolder.transitDirection.setText("방향 → " + mList.get(position).getNextName());
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView busName;
        public TextView transitDirection;

    }
}
