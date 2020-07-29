package com.publicTA.template.src.AlarmList.interfaces;

import com.publicTA.template.src.AlarmList.models.ResponseArrivalTime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FragmentAlarmListRetrofitInterface {
    @GET("/transit/arrivalTime")
    Call<ResponseArrivalTime> getArrivalTime(
            @Query(value = "stId", encoded = true) String stId,
            @Query(value = "trId", encoded = true) String trId,
            @Query(value = "nextStId", encoded = true) String nextStId
    );
}
