package com.publicTA.template.src.AlarmList.services;

import android.util.Log;

import com.publicTA.template.src.AlarmList.interfaces.FragmentAlarmListRetrofitInterface;
import com.publicTA.template.src.AlarmList.interfaces.FragmentAlarmListView;
import com.publicTA.template.src.AlarmList.models.ResponseArrivalTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.publicTA.template.src.ApplicationClass.getRetrofit;

public class FragmentAlarmListService {
    public FragmentAlarmListView mFragmentAlarmListView;

    public FragmentAlarmListService(FragmentAlarmListView fragmentAlarmListView) {
        this.mFragmentAlarmListView = fragmentAlarmListView;
    }

    public void getArrivalTime(String stId, String trId, String nextStId, final int alarmIdx) {
        Log.e("[Log.e]getArrivalTime", stId + " " + trId + " " + nextStId);

        // note 분명 3개를 보냈는데 ?
        final FragmentAlarmListRetrofitInterface fragmentAlarmListRetrofitInterface = getRetrofit().create(FragmentAlarmListRetrofitInterface.class);
        fragmentAlarmListRetrofitInterface.getArrivalTime(stId, trId, nextStId).enqueue(new Callback<ResponseArrivalTime>() {
            @Override
            public void onResponse(Call<ResponseArrivalTime> call, Response<ResponseArrivalTime> response) {
                final ResponseArrivalTime responseArrivalTime = response.body();
                // note 도대체 왜 1개만 오는거야
                //Log.e("[Log.e] getArrivalTime ", "success , response size : " + response.body().getResult().size());
                if(responseArrivalTime == null) {
                    Log.e("[Log.e] getArrivalTime ", "on fail");
                    mFragmentAlarmListView.getStationFailure("실패");
                }
                if (responseArrivalTime.isSuccess()) {
                    Log.e("[Log.e] getArrivalTime ", "success , result size : " + responseArrivalTime.getResult().size());
                    mFragmentAlarmListView.getStationSuccess(responseArrivalTime.getResult(), alarmIdx);
                }
                else {
                    Log.e("[Log.e] getArrivalTime ", "on fail");
                    mFragmentAlarmListView.getStationFailure("실패");
                }
            }

            @Override
            public void onFailure(Call<ResponseArrivalTime> call, Throwable t) {
                Log.e("[Log.e] getArrivalTime ", "on fail" + t);
                mFragmentAlarmListView.getStationFailure("실패");
            }
        });
    }

}
