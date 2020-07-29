package com.publicTA.template.src.LastBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.AddAlarm.Path.FragmentAddPath;
import com.publicTA.template.src.LastBus.Adapter.LastAlarmItem;

public class AddLastBusActivity extends AppCompatActivity {
    int RESULT_OK = 1;
    TextView mTvMonday,mTvTuesday,mTvWednesday,mTvThursday,mTvFriday,mTvSaturday,mTvSunday;
    boolean[] mDayCheckArray = new boolean[7];
    String[] mDayName = {"월","화","수","목","금","토","일"};
    ImageButton mBtnPlusPath;

    LinearLayout mPathTab2;
    CardView mPathTab1;
    TextView mTvStation, mTvDirection, mTvType, mTvTransitNum;
    PathItem mPathItem;
    String mDayCycle;
    boolean mOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_last_bus);
        mTvMonday = findViewById(R.id.add_last_bus_tv_monday);
        mTvTuesday = findViewById(R.id.add_last_bus_tv_tuesday);
        mTvWednesday = findViewById(R.id.add_last_bus_tv_wednesday);
        mTvThursday = findViewById(R.id.add_last_bus_tv_thursday);
        mTvFriday = findViewById(R.id.add_last_bus_tv_friday);
        mTvSaturday = findViewById(R.id.add_last_bus_tv_saturday);
        mTvSunday = findViewById(R.id.add_last_bus_tv_sunday);
        mBtnPlusPath = findViewById(R.id.add_last_bus_btn_plus_path);
        mPathTab1 = findViewById(R.id.add_last_bus_tab_path_info_1);
        mPathTab2 = findViewById(R.id.add_last_bus_tab_path_info_2);
        mTvStation = findViewById(R.id.last_path_item_tv_start_station);
        mTvDirection = findViewById(R.id.last_path_item_tv_end_station);
        mTvType = findViewById(R.id.last_path_item_tv_transit_type);
        mTvTransitNum = findViewById(R.id.last_path_item_tv_transit_num);
        mPathItem = null;
        mDayCycle = "";
    }

    public void addLastBusBtnSet(View v) {
        switch (v.getId()) {
            case R.id.add_last_bus_btn_back :
                finish();
                break;
            case R.id.add_last_bus_btn_plus_path:
                FragmentAddPath fragmentAddPath = new FragmentAddPath(2);
                fragmentAddPath.mContext = getApplicationContext();
                fragmentAddPath.show(((FragmentActivity)this).getSupportFragmentManager(),"BottomSheet");
                break;
            case R.id.add_last_bus_btn_complete:
                completeAdd();
                break;
            case R.id.add_last_bus_tv_monday:
                checkDay(0);
                break;
            case R.id.add_last_bus_tv_tuesday:
                checkDay(1);
                break;
            case R.id.add_last_bus_tv_wednesday:
                checkDay(2);
                break;
            case R.id.add_last_bus_tv_thursday:
                checkDay(3);
                break;
            case R.id.add_last_bus_tv_friday:
                checkDay(4);
                break;
            case R.id.add_last_bus_tv_saturday:
                checkDay(5);
                break;
            case R.id.add_last_bus_tv_sunday:
                checkDay(6);
                break;
            default:
                break;
        }
    }

    public void resultSet(PathItem pathItem) {
        mBtnPlusPath.setVisibility(View.GONE);
        mPathTab1.setVisibility(View.VISIBLE);
        mPathTab2.setVisibility(View.VISIBLE);
        mTvStation.setText(pathItem.getStartStation());
        mTvDirection.setText(pathItem.getDirection());
        if(pathItem.getTransitType() == 1) {
            mTvType.setText("버스");
        }
        else if(pathItem.getTransitType() == 2) {
            mTvType.setText("지하철");
        }
        mTvTransitNum.setText(pathItem.getTransitName());
        mPathItem = pathItem;
    }

    public void completeAdd() {
        boolean dayCheck = false;
        if(mPathItem == null) {
            Toast.makeText(getApplicationContext(), "경로를 설정해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i = 0; i < 7; i++) {
            if(mDayCheckArray[i] == true) {
                mDayCycle += mDayName[i];
                dayCheck = true;
            }
        }
        if(!dayCheck) {
            Toast.makeText(getApplicationContext(),"요일을 지정해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        LastAlarmItem item = new LastAlarmItem(mPathItem,mDayCycle,true);
        Intent intent = new Intent();
        intent.putExtra("lastBusAlarm", item);
        setResult(this.RESULT_OK, intent);
        finish();
    }


    public void checkDay(int dayIdx) {
        if(dayIdx == 0) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvMonday.setBackgroundResource(R.drawable.yellow_circle);
                mTvMonday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvMonday.setBackgroundResource(R.drawable.white_circle);
                mTvMonday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 1) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvTuesday.setBackgroundResource(R.drawable.yellow_circle);
                mTvTuesday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvTuesday.setBackgroundResource(R.drawable.white_circle);
                mTvTuesday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 2) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvWednesday.setBackgroundResource(R.drawable.yellow_circle);
                mTvWednesday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvWednesday.setBackgroundResource(R.drawable.white_circle);
                mTvWednesday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 3) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvThursday.setBackgroundResource(R.drawable.yellow_circle);
                mTvThursday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvThursday.setBackgroundResource(R.drawable.white_circle);
                mTvThursday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 4) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvFriday.setBackgroundResource(R.drawable.yellow_circle);
                mTvFriday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvFriday.setBackgroundResource(R.drawable.white_circle);
                mTvFriday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 5) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvSaturday.setBackgroundResource(R.drawable.yellow_circle);
                mTvSaturday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvSaturday.setBackgroundResource(R.drawable.white_circle);
                mTvSaturday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
        else if(dayIdx == 6) {
            if(!mDayCheckArray[dayIdx]) {
                mDayCheckArray[dayIdx] = true;
                mTvSunday.setBackgroundResource(R.drawable.yellow_circle);
                mTvSunday.setTextColor(Color.WHITE);
            }
            else {
                mDayCheckArray[dayIdx] = false;
                mTvSunday.setBackgroundResource(R.drawable.white_circle);
                mTvSunday.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorOffGray));
            }
        }
    }
}
