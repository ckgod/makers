package com.publicTA.template.src.testNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.LastBus.Adapter.LastAlarmItem;
import com.publicTA.template.src.main.MainActivity;
import com.publicTA.template.src.testNotification.interfaces.BroadcastDView;
import com.publicTA.template.src.testNotification.models.ResponseLastTime;
import com.publicTA.template.src.testNotification.models.ResponseLeftTime;
import com.publicTA.template.src.testNotification.service.BroadcastDService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class BroadcastD extends BroadcastReceiver implements BroadcastDView {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    ProgressDialog mProgressDialog;


    private static final String ALARMLIST = "alarmList";
    private static final String LAST_ALARM_LIST = "lastAlarmList";
    ArrayList<AlarmItem> alarmItemArrayList = new ArrayList<>();
    ArrayList<LastAlarmItem> lastAlarmItemArrayList = new ArrayList<>();

    Context mContext;
    String mStName, mTrName;
    int mLeftTime;
    int mAlarmIdx;
    int mType;

    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
//        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(R.drawable.com_facebook_button_icon).setTicker("HETT").setWhen(System.currentTimeMillis())
//                .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
//        notificationmanager.notify(1, builder.build());
        mContext = context;
        alarmItemArrayList = loadAlarmList();
        lastAlarmItemArrayList = loadLastList();
        mAlarmIdx = intent.getIntExtra("alarmIdx",0);
        mType = intent.getIntExtra("type",0);
        final String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        if(mType == 1) { // note 그냥 알람
            if(alarmItemArrayList.get(mAlarmIdx) != null && alarmItemArrayList.get(mAlarmIdx).isOnOff()) {
                Log.e("[Log.e] day ", alarmItemArrayList.get(mAlarmIdx).getDayCycle());
                Calendar calendar = Calendar.getInstance();
                String today = week[calendar.get(Calendar.DAY_OF_WEEK)-1];
                Log.e("[Log] today", today.charAt(0) + "");
                String alarmDayOfWeek = alarmItemArrayList.get(mAlarmIdx).getDayCycle();
                boolean weekCheck = false;
                for(int i = 0; i < alarmDayOfWeek.length(); i++) {
                    if(alarmDayOfWeek.charAt(i) == today.charAt(0)) {
                        weekCheck = true;
                        break;
                    }
                }
                Log.e("[Log] weekCheck", weekCheck + "" );
                if(!weekCheck) {
                    return;
                }
                calendar.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE),
                        alarmItemArrayList.get(mAlarmIdx).getStartHours(),
                        alarmItemArrayList.get(mAlarmIdx).getStartMinutes(),
                        0);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DATE),
                        alarmItemArrayList.get(mAlarmIdx).getEndHours(),
                        alarmItemArrayList.get(mAlarmIdx).getEndMinutes(),
                        0);
                long interval = 1000 * 60 * 60  * 24;
                long startTime = calendar.getTimeInMillis();
                long endTime = endCalendar.getTimeInMillis();

                if(startTime < System.currentTimeMillis() && endTime > System.currentTimeMillis() && weekCheck) {
                    tryGetArrivalTime(alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getStationId(),
                            alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getTransitId(),
                            alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getNextStationId());
                    Log.e("[Log.e] id : ", alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getStationId() + " " +
                            alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getTransitId() + " " +
                            alarmItemArrayList.get(mAlarmIdx).getPathItemArrayList().get(0).getNextStationId());
                }
            }
        }
        else if(mType == 2) { // note 막차알람
            if(lastAlarmItemArrayList.get(mAlarmIdx) != null && lastAlarmItemArrayList.get(mAlarmIdx).isOnOff()) {
                Log.e("[Log.e] day ", lastAlarmItemArrayList.get(mAlarmIdx).getDayCycle());
                Calendar calendar = Calendar.getInstance();
                String today = week[calendar.get(Calendar.DAY_OF_WEEK)-1];
                Log.e("[Log] today", today.charAt(0) + "");
                String alarmDayOfWeek = lastAlarmItemArrayList.get(mAlarmIdx).getDayCycle();
                boolean weekCheck = false;
                for(int i = 0; i < alarmDayOfWeek.length(); i++) {
                    if(alarmDayOfWeek.charAt(i) == today.charAt(0)) {
                        weekCheck = true;
                        break;
                    }
                }
                Log.e("[Log] weekCheck", weekCheck + "" );
                if(!weekCheck) {
                    return;
                }
                calendar.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE),
                        21,
                        0,
                        0);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DATE),
                        1,
                        30,
                        0);
                long startTime = calendar.getTimeInMillis();
                long endTime = endCalendar.getTimeInMillis();
                if((startTime < System.currentTimeMillis() || endTime > System.currentTimeMillis()) && weekCheck) {
                    tryGetLastTime(lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getStationId(),
                            lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getTransitId(),
                            lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getNextStationId());
                    Log.e("[Log.e] id : ", lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getStationId() + " " +
                            lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getTransitId() + " " +
                            lastAlarmItemArrayList.get(mAlarmIdx).getPathItem().getNextStationId() + " 막차");
                }
            }
        }



    }

    private void saveAlarmList(ArrayList<AlarmItem> alarmItemArrayList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmItemArrayList);
        editor.putString(ALARMLIST, json);
        editor.apply();
    }

    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
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

    public void tryGetArrivalTime(int stId, int trId, int nextStId) {
        //showProgressDialog();
        BroadcastDService service = new BroadcastDService(this);
        //Log.e("tryGetArrivalTime", stId.size() + " " + trId.size() + " " + nextStId.size());
        service.getLeftTime(stId, trId, nextStId);
    }

    public void tryGetLastTime(int stId, int trId, int nextStId) {
        //showProgressDialog();
        BroadcastDService service = new BroadcastDService(this);
        //Log.e("tryGetArrivalTime", stId.size() + " " + trId.size() + " " + nextStId.size());
        service.getLastTime(stId, trId, nextStId);
    }

    @Override
    public void getLeftTimeSuccess(ResponseLeftTime.LeftTime result) {
        mStName = result.getStName();
        mTrName = result.getTrName();
        mLeftTime = result.getLeftTime();

        if(mLeftTime == 0 && result.getLocation() == 0) {
        }
        else {
            notifyLeftTime(mContext,result);
        }
    }

    @Override
    public void getLeftTimeFailure(String message) {

    }

    @Override
    public void getLastTimeSuccess(ResponseLastTime.LastTime result) {
        mStName = result.getStName();
        mTrName = result.getTrName();
        mLeftTime = result.getLeftTime();
        Log.e("[Log] status", result.getStatus() + "");
        if(result.getStatus() == 1) {
            notifyLastTime(mContext, result);
        }
    }

    @Override
    public void getLastTimeFailure(String message) {

    }

    public void notifyLeftTime(Context context, ResponseLeftTime.LeftTime result) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_logo_ta_alpha_white); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            String channelName = "매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌
            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);    // 노티피케이션 채널을 시스템에 등록
            }
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_ta); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }


        if(result.getType() == 1) {
            builder.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("{Time to watch some cool stuff!}")
                    .setContentTitle(result.getStName())
                    .setContentText(result.getTrName() + "버스가 " + result.getStName() + "에 " + result.getLeftTime() + "분 후 도착합니다.")
                    .setContentInfo("INFO")
                    .setContentIntent(pendingI);
        }
        else if(result.getType() == 2) {
            if(result.getSubwayType() == 1) {
                builder.setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("{Time to watch some cool stuff!}")
                        .setContentTitle(result.getStName())
                        .setContentText(result.getTrName() + "이 " + result.getStName() + "에 " + result.getLeftTime() + "분 후 도착합니다.")
                        .setContentInfo("INFO")
                        .setContentIntent(pendingI);
            }
            else if(result.getSubwayType() == 2) {
                builder.setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("{Time to watch some cool stuff!}")
                        .setContentTitle(result.getStName())
                        .setContentText(result.getTrName() + "이 " + result.getLeftTime() + "정거장 남았습니다.")
                        .setContentInfo("INFO")
                        .setContentIntent(pendingI);
            }
        }


        if (notificationManager != null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "My:Tag");
            wakeLock.acquire(4000);

            // 노티피케이션 동작시킴
            notificationManager.notify(mAlarmIdx, builder.build());
        }
    }


    public void notifyLastTime(Context context, ResponseLastTime.LastTime result) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_logo_ta_alpha_white); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            String channelName = "매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌
            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);    // 노티피케이션 채널을 시스템에 등록
            }
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_ta); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }


        if(result.getType() == 1) {
            builder.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("{Time to watch some cool stuff!}")
                    .setContentTitle(result.getStName() + "(막차)")
                    .setContentText(result.getTrName() + "버스가 " + result.getStName() + "에 " + result.getLeftTime() + "분 후 도착합니다.")
                    .setContentInfo("INFO")
                    .setContentIntent(pendingI);
        }
        else if(result.getType() == 2) {
            if(result.getSubwayType() == 1) {
                builder.setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("{Time to watch some cool stuff!}")
                        .setContentTitle(result.getStName() + "(막차)")
                        .setContentText(result.getTrName() + "이 " + result.getStName() + "에 " + result.getLeftTime() + "분 후 도착합니다.")
                        .setContentInfo("INFO")
                        .setContentIntent(pendingI);
            }
            else if(result.getSubwayType() == 2) {
                builder.setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("{Time to watch some cool stuff!}")
                        .setContentTitle(result.getStName() + "(막차)")
                        .setContentText(result.getTrName() + "이 " + result.getLeftTime() + "정거장 남았습니다.")
                        .setContentInfo("INFO")
                        .setContentIntent(pendingI);
            }
        }


        if (notificationManager != null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "My:Tag");
            wakeLock.acquire(4000);
            // 노티피케이션 동작시킴
            notificationManager.notify(mAlarmIdx+1000, builder.build());
        }
    }

    private ArrayList<LastAlarmItem> loadLastList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(LAST_ALARM_LIST, "");
        ArrayList<LastAlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<LastAlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        Log.e("alarmArrayListSize", alarmItemArrayList.size()+"");
        return alarmItemArrayList;
    }
}


