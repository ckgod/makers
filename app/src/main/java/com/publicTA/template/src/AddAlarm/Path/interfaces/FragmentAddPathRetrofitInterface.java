package com.publicTA.template.src.AddAlarm.Path.interfaces;

import com.publicTA.template.src.AddAlarm.Path.models.ResponseBus;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseStation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FragmentAddPathRetrofitInterface {
    @GET("/station")
    Call<ResponseStation> getStation(
            @Query("stName") String stName
    );

    @GET("/transit")
    Call<ResponseBus> getBus(
            @Query("stId") String stId
    );
}
