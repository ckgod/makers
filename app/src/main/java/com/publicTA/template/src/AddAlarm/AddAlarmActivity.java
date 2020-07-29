package com.publicTA.template.src.AddAlarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.publicTA.template.R;
import com.publicTA.template.src.BaseActivity;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathListAdapter;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathListDecoration;

import java.util.ArrayList;
import java.util.Calendar;

public class AddAlarmActivity extends BaseActivity {
    int RESULT_OK = 1;
    private RecyclerView mPathListView;
    private PathListAdapter mPathListAdapter;
    ArrayList<PathItem> mPathArrayList = new ArrayList<>();

    TextView mTvMonday,mTvTuesday,mTvWednesday,mTvThursday,mTvFriday,mTvSaturday,mTvSunday;
    boolean[] mDayCheckArray = new boolean[7];
    String[] mDayName = {"월","화","수","목","금","토","일"};

    Spinner mSpSound;
    ArrayList mSoundArray = new ArrayList();
    ArrayAdapter mSpinnerAdapter;

    EditText mEtLabel;

    // AlarmItem 인자들
    static int mStartAMPMType, mStartHours, mStartMinute, mEndAMPMType, mEndHours, mEndMinute;
    boolean mOnOff;
    String mLabel;
    String mDayCycle = "";

    static boolean startTimeSet, endTimeSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        mStartAMPMType = 1;
        mStartHours = 0;
        mStartMinute = 0;
        mEndHours = 0;
        mEndMinute = 0;
        mEndAMPMType = 1;

        startTimeSet = false;
        endTimeSet = false;

        mTvMonday = findViewById(R.id.add_alarm_tv_monday);
        mTvTuesday = findViewById(R.id.add_alarm_tv_tuesday);
        mTvWednesday = findViewById(R.id.add_alarm_tv_wednesday);
        mTvThursday = findViewById(R.id.add_alarm_tv_thursday);
        mTvFriday = findViewById(R.id.add_alarm_tv_friday);
        mTvSaturday = findViewById(R.id.add_alarm_tv_saturday);
        mTvSunday = findViewById(R.id.add_alarm_tv_sunday);

        mSpSound = findViewById(R.id.add_alarm_spinner_sound);
        mSoundArray.add("진동");
        mSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.sound_item,mSoundArray);
        mSpSound.setAdapter(mSpinnerAdapter);

        mEtLabel = findViewById(R.id.add_alarm_et_label);

        //setDayList(); -> expandable layout
        setPathList();
    }


    private void setPathList() {
        mPathListView = findViewById(R.id.add_alarm_rv_path);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mPathListView.setLayoutManager(layoutManager);

       // mPathArrayList.add(new PathItem(1,"인하대후문","주안역",1,"511"));
        //mPathArrayList.add(new PathItem(2,"주안역","구로역",2,"1호선"));

        mPathArrayList = new ArrayList<>();
        mPathListAdapter = new PathListAdapter(this, mPathArrayList, onClickPathItem);
        mPathListView.setAdapter(mPathListAdapter);

        PathListDecoration decoration = new PathListDecoration();
        mPathListView.addItemDecoration(decoration);
    }

    // 경로들 클릭 이벤트 리스너
    private View.OnClickListener onClickPathItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void addAlarmSetButton(View v) {
        switch (v.getId()) {
            case R.id.add_alarm_btn_back :
                finish();
                break;
            case R.id.add_alarm_btn_complete:
                completeSetAlarm();
                break;
            case R.id.add_alarm_start_time:
                showTimePicker(1);
                break;
            case R.id.add_alarm_end_time:
                showTimePicker(2);
                break;
            case R.id.add_alarm_tv_monday:
                checkDay(0);
                break;
            case R.id.add_alarm_tv_tuesday:
                checkDay(1);
                break;
            case R.id.add_alarm_tv_wednesday:
                checkDay(2);
                break;
            case R.id.add_alarm_tv_thursday:
                checkDay(3);
                break;
            case R.id.add_alarm_tv_friday:
                checkDay(4);
                break;
            case R.id.add_alarm_tv_saturday:
                checkDay(5);
                break;
            case R.id.add_alarm_tv_sunday:
                checkDay(6);
                break;
            default:
                break;
        }
    }

    public void showTimePicker(int t) {
        AlarmTimePicker dialog = new AlarmTimePicker(t);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.TaTimePicker);
        dialog.show(getSupportFragmentManager(),"what");
    }

    public static class AlarmTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        int type;

        AlarmTimePicker(int type) {
            this.type = type;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.e("type : ", type+" ");
            if(type == 1) {
                if(hourOfDay < 12) {
                    mStartAMPMType = 1;
                } else {
                    mStartAMPMType = 2;
                }
                TextView startHour = getActivity().findViewById(R.id.add_alarm_start_time_hour);
                TextView startMinute = getActivity().findViewById(R.id.add_alarm_start_time_minute);
                String hours, minutes;
                if(hourOfDay >= 0 && hourOfDay <= 9) {
                    hours = "0" + hourOfDay;
                }
                else {
                    hours = String.valueOf(hourOfDay);
                }
                if(minute >= 0 && minute <= 9) {
                    minutes = "0" + minute;
                }
                else {
                    minutes = String.valueOf(minute);
                }
                startHour.setText(hours);
                mStartHours = hourOfDay;
                startMinute.setText(minutes);
                mStartMinute = minute;
                Log.e("mStartAMPMType : ", mStartAMPMType+" ");
                startTimeSet = true;
            }
            else if(type == 2) {
                if(hourOfDay < 12) {
                    mEndAMPMType = 1;
                } else {
                    mEndAMPMType = 2;
                }
                TextView endHour = getActivity().findViewById(R.id.add_alarm_end_time_hour);
                TextView endMinute = getActivity().findViewById(R.id.add_alarm_end_time_minute);
                String hours, minutes;
                if(hourOfDay >= 0 && hourOfDay <= 9) {
                    hours = "0" + hourOfDay;
                }
                else {
                    hours = String.valueOf(hourOfDay);
                }
                if(minute >= 0 && minute <= 9) {
                    minutes = "0" + minute;
                }
                else {
                    minutes = String.valueOf(minute);
                }
                endHour.setText(hours);
                mEndHours = hourOfDay;
                endMinute.setText(minutes);
                mEndMinute = minute;
                Log.e("mEndAMPMType : ", mEndAMPMType+" ");
                endTimeSet = true;
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar mCalendar = Calendar.getInstance();
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int min = mCalendar.get(Calendar.MINUTE);
            TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                    getContext(),this,hour,min, DateFormat.is24HourFormat(getContext())
            );
            return mTimePickerDialog;
        }
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

    public void addPathItem(PathItem pathItem){
        //Toast.makeText(this, "testFun()", Toast.LENGTH_SHORT).show();
        mPathArrayList.add(pathItem);
        mPathListAdapter.notifyDataSetChanged();
    }

    public void completeSetAlarm() {
        boolean dayCheck = false;
        // 여기서 반복요일 수정해야함
        if(mPathArrayList.size() == 0) {
            Toast.makeText(getApplicationContext(),"경로를 1개 이상 설정해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!startTimeSet && !endTimeSet) {
            Toast.makeText(getApplicationContext(),"시간을 설정해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mStartHours > mEndHours) {
            Toast.makeText(getApplicationContext(),"종료시간이 시작시간보다 빠릅니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(mStartHours == mEndHours) {
            if(mStartMinute >= mEndMinute) {
                Toast.makeText(getApplicationContext(),"시간설정을 제대로 해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        for(int i = 0; i < 7; i++) {
            if(mDayCheckArray[i] == true) {
                mDayCycle += mDayName[i];
                dayCheck = true;
            }
        }
        if(!dayCheck) {
            Toast.makeText(getApplicationContext(),"날짜를 선택 해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        mOnOff = true;
        mLabel = mEtLabel.getText().toString();

        Intent intent = new Intent();
        Log.e("type start, end : " ,"" + mStartAMPMType+ " " + mEndAMPMType);
        AlarmItem alarmItem = new AlarmItem(mPathArrayList, mStartAMPMType, mStartHours, mStartMinute, mEndAMPMType, mEndHours, mEndMinute ,mDayCycle, mOnOff, mLabel);
        intent.putExtra("alarmItem", alarmItem);
        setResult(this.RESULT_OK,intent);
        finish();
    }
}
