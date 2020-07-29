package com.publicTA.template.src.testNotification.service;

import android.util.Log;

import com.publicTA.template.src.testNotification.interfaces.BroadcastDRetrofitInterface;
import com.publicTA.template.src.testNotification.interfaces.BroadcastDView;
import com.publicTA.template.src.testNotification.models.ResponseLastTime;
import com.publicTA.template.src.testNotification.models.ResponseLeftTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.publicTA.template.src.ApplicationClass.getRetrofit;

public class BroadcastDService {
    private final BroadcastDView mBroadcastDView;

    public BroadcastDService(final BroadcastDView broadcastDView) {
        this.mBroadcastDView = broadcastDView;
    }

    public void getLeftTime(int stId, int trId, int nextStId) {
        final BroadcastDRetrofitInterface broadcastDRetrofitInterface = getRetrofit().create(BroadcastDRetrofitInterface.class);
        broadcastDRetrofitInterface.getAlarmInfo(stId, trId, nextStId).enqueue(new Callback<ResponseLeftTime>() {
            @Override
            public void onResponse(Call<ResponseLeftTime> call, Response<ResponseLeftTime> response) {
                final ResponseLeftTime responseLeftTime = response.body();
                if (responseLeftTime == null) {
                    mBroadcastDView.getLeftTimeFailure(null);
                    Log.e("[Log.e] LeftTime", "failure");
                    return;
                }
                if(responseLeftTime.isSuccess()) {
                    mBroadcastDView.getLeftTimeSuccess(responseLeftTime.getResult());
                    Log.e("[Log.e] LeftTime", "success");
                }
                else {
                    mBroadcastDView.getLeftTimeFailure(null);
                    Log.e("[Log.e] LeftTime", "failure");
                }
            }

            @Override
            public void onFailure(Call<ResponseLeftTime> call, Throwable t) {
                mBroadcastDView.getLeftTimeFailure(null);
                Log.e("[Log.e] LeftTime", "failure " + t);
            }
        });
    }

    public void getLastTime(int stId, int trId, int nextStId) {
        final BroadcastDRetrofitInterface broadcastDRetrofitInterface = getRetrofit().create(BroadcastDRetrofitInterface.class);
        broadcastDRetrofitInterface.getLastInfo(stId, trId, nextStId).enqueue(new Callback<ResponseLastTime>() {
            @Override
            public void onResponse(Call<ResponseLastTime> call, Response<ResponseLastTime> response) {
                final ResponseLastTime responseLastTime = response.body();
                if(responseLastTime == null) {
                    mBroadcastDView.getLastTimeFailure(null);
                    Log.e("[Log.e] LastTime", "failure");
                    return;
                }
                if(responseLastTime.isSuccess()) {
                    mBroadcastDView.getLastTimeSuccess(responseLastTime.getResult());
                    Log.e("[Log.e] LastTime", "success");
                }
                else {
                    mBroadcastDView.getLastTimeFailure(null);
                    Log.e("[Log.e] LastTime", "failure");
                }
            }

            @Override
            public void onFailure(Call<ResponseLastTime> call, Throwable t) {
                mBroadcastDView.getLastTimeFailure(null);
                Log.e("[Log.e] LastTime", "failure" + t.getLocalizedMessage());
            }
        });
    }
}
