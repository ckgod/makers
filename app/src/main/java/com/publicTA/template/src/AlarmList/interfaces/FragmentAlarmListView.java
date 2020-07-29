package com.publicTA.template.src.AlarmList.interfaces;

import com.publicTA.template.src.AlarmList.models.ResponseArrivalTime;

import java.util.ArrayList;

public interface FragmentAlarmListView {
    void getStationSuccess(ArrayList<ResponseArrivalTime.arrivalTime> result, int alarmIdx);

    void getStationFailure(String message);
}
