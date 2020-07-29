package com.publicTA.template.src.testNotification.interfaces;

import com.publicTA.template.src.AlarmList.models.ResponseArrivalTime;
import com.publicTA.template.src.testNotification.models.ResponseLastTime;
import com.publicTA.template.src.testNotification.models.ResponseLeftTime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BroadcastDRetrofitInterface {
    @GET("/transit/arrivalInfo")
    Call<ResponseLeftTime> getAlarmInfo(
            @Query("stId") int stId,
            @Query("trId") int trId,
            @Query("nextStId") int nextStId
    );

    @GET("/transit/last")
    Call<ResponseLastTime> getLastInfo(
            @Query("stId") int stId,
            @Query("trId") int trId,
            @Query("nextStId") int nextStId
    );
}
