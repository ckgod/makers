package com.publicTA.template.src.AddAlarm.Path.Service;

import android.util.Log;

import com.publicTA.template.src.AddAlarm.Path.interfaces.FragmentAddPathRetrofitInterface;
import com.publicTA.template.src.AddAlarm.Path.interfaces.FragmentAddPathView;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseBus;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseStation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.publicTA.template.src.ApplicationClass.getRetrofit;

public class FragmentAddPathService {
    public FragmentAddPathView mFragmentAddPathView;

    public FragmentAddPathService(FragmentAddPathView fragmentAddPathView) {
        this.mFragmentAddPathView = fragmentAddPathView;
    }

    public void getStation(String stName) {
        final FragmentAddPathRetrofitInterface fragmentAddPathRetrofitInterface = getRetrofit().create(FragmentAddPathRetrofitInterface.class);
        fragmentAddPathRetrofitInterface.getStation(stName).enqueue(new Callback<ResponseStation>() {
            @Override
            public void onResponse(Call<ResponseStation> call, Response<ResponseStation> response) {
                final ResponseStation responseStation = response.body();
                if(responseStation == null) {
                    mFragmentAddPathView.getStationFailure("네트워크 통신이 원활하지 않습니다.");
                    Log.e("[Log.e] station ", "on fail");
                    return;
                }
                if(responseStation.getCode() == 100) {
                    mFragmentAddPathView.getStationSuccess(responseStation.isSuccess(),responseStation.getCode(),responseStation.getMessage(),responseStation.getStation());
                    Log.e("[Log.e] station ", "정류장 받아오기 성공");
                }
                else {
                    mFragmentAddPathView.getStationFailure("검색 결과가 없습니다.");
                }
            }


            @Override
            public void onFailure(Call<ResponseStation> call, Throwable t) {
                mFragmentAddPathView.getStationFailure("네트워크 통신이 원활하지 않습니다.");
                Log.e("[Log.e] station ", t.getLocalizedMessage());
            }
        });
    }

    public void getBus(String stId) {
        final FragmentAddPathRetrofitInterface fragmentAddPathRetrofitInterface = getRetrofit().create(FragmentAddPathRetrofitInterface.class);
        fragmentAddPathRetrofitInterface.getBus(stId).enqueue(new Callback<ResponseBus>() {
            @Override
            public void onResponse(Call<ResponseBus> call, Response<ResponseBus> response) {
                final ResponseBus responseBus = response.body();
                if(responseBus == null) {
                    Log.e("[Log.e] bus ", "responseBus is null");
                    mFragmentAddPathView.getBusFailure();
                }
                Log.e("[Log.e] bus ", "is Success");
                mFragmentAddPathView.getBusSuccess(responseBus.isSuccess(),responseBus.getCode(),responseBus.getMessage(),responseBus.getResult());
            }

            @Override
            public void onFailure(Call<ResponseBus> call, Throwable t) {
                Log.e("[Log.e] bus ", t.getLocalizedMessage());
                mFragmentAddPathView.getBusFailure();
            }
        });
    }
}
