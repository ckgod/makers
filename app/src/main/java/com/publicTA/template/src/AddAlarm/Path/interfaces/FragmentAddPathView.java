package com.publicTA.template.src.AddAlarm.Path.interfaces;

import com.publicTA.template.src.AddAlarm.Path.models.ResponseBus;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseStation;

import java.util.ArrayList;

public interface FragmentAddPathView {
    void getStationSuccess(boolean isSuccess, int code, String message, ArrayList<ResponseStation.Station> result);

    void getStationFailure(String message);

    void getBusSuccess(boolean isSuccess, int code, String message, ArrayList<ResponseBus.Transit> result);

    void getBusFailure();
}
