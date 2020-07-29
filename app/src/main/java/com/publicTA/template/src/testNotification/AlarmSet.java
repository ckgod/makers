package com.publicTA.template.src.testNotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmSet {
    private Context context;
    private static final String ALARMLIST = "alarmList";
    public AlarmSet(Context context) {
        this.context=context;
    }

    public void Alarm(int alarmIdx, int type) {
        if(type == 1) {
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BroadcastD.class);
            intent.putExtra("alarmIdx", alarmIdx);
            intent.putExtra("type", type);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmIdx, intent, PendingIntent.FLAG_NO_CREATE);
            //ArrayList<AlarmItem> alarmItemArrayList = loadAlarmList();

            //note 안드로이드 자체에서 최소 반복시간 1분 지정
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 300000, sender);
            Log.e("[Log] alarm set", "알람 설정 성공 " + alarmIdx);
        }
        else if(type == 2) {
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BroadcastD.class);
            intent.putExtra("alarmIdx", alarmIdx);
            intent.putExtra("type", type);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmIdx + 1000, intent, PendingIntent.FLAG_NO_CREATE);
            //ArrayList<AlarmItem> alarmItemArrayList = loadAlarmList();

            //note 안드로이드 자체에서 최소 반복시간 1분 지정
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 300000, sender);
            Log.e("[Log] alarm set", "알람 설정 성공 " + alarmIdx);
        }

    }

    public void AlarmCancel(int alarmIdx, int type) {
        if(type == 1) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BroadcastD.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmIdx, intent, 0);
            am.cancel(pendingIntent);
        }
        else if(type == 2) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BroadcastD.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmIdx + 1000, intent, 0);
            am.cancel(pendingIntent);
        }

    }

    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(ALARMLIST, "");
        ArrayList<AlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<AlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        return alarmItemArrayList;
    }
}
