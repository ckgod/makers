package com.publicTA.template.src.currentState;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.currentState.Adapter.CurStateAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentCurState extends Fragment {
    private static final String ALARMLIST = "alarmList";
    //note 지도 변수들-------------------------------------------------------------
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_CALL_PHONE = 2;

    public LocationManager mLocationManager;
    public LocationListener mLocationListener;
    public double longitude; //경도
    public double latitude; //위도
    public double altitude; //고도
    public float accuracy; //정확도
    public String provider; //위치제공자
    public String currentLocation; // 그래서 최종 위치

    MapView mapView;
    ViewGroup mapViewContainer;
    ImageButton mSetCurrentPos;
    //note-------------------------------------------------------------------------

    RecyclerView mRvCurAlarm;
    ArrayList<PathItem> mArrayListPath = new ArrayList<>();
    ArrayList<AlarmItem> allAlarm = new ArrayList<>();
    CurStateAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cur_state , container, false);
        mapViewContainer = (ViewGroup) rootView.findViewById(R.id.current_map_view);
        mSetCurrentPos = rootView.findViewById(R.id.current_btn_set_gps);
        mRvCurAlarm = rootView.findViewById(R.id.current_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        mRvCurAlarm.setLayoutManager(layoutManager);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

                Log.d("test", "onLocationChanged, location:" + location);
                longitude = location.getLongitude(); //경도
                latitude = location.getLatitude();   //위도
                altitude = location.getAltitude();   //고도
                accuracy = location.getAccuracy();    //정확도
                provider = location.getProvider();   //위치제공자
                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                //Network 위치제공자에 의한 위치변화
                //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

//            txtCurrentMoney.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
//                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);

                // 위치 정보를 글로 나타낸다

                // 지도를 움직인다
                setDaumMapCurrentLocation(latitude, longitude);
                //lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mSetCurrentPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDaumMapCurrentLocation(latitude,longitude);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        curF5();
        getLocationPermission();
        initView();
        setLocation();
    }

    public void curF5() {
        mArrayListPath = new ArrayList<>();
        mArrayListPath = checkCurrent();
        Log.e("[Log] curSize", mArrayListPath.size()+"");
        mAdapter = new CurStateAdapter(mArrayListPath,getContext(),R.layout.current_item);

        mRvCurAlarm.setAdapter(mAdapter);
    }

    public ArrayList<PathItem> checkCurrent() {
        long curT = System.currentTimeMillis();
        ArrayList<PathItem> arrayList = new ArrayList<>();
        allAlarm = loadAlarmList();
        for(int i = 0; i < allAlarm.size(); i++) {
            if(allAlarm.get(i).isOnOff()){
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE),
                        allAlarm.get(i).getStartHours(),
                        allAlarm.get(i).getStartMinutes(),
                        0);
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DATE),
                        allAlarm.get(i).getEndHours(),
                        allAlarm.get(i).getEndMinutes(),
                        0);
                long startTime = calendar.getTimeInMillis();
                long endTime = endCalendar.getTimeInMillis();
                if(startTime <= curT && curT <= endTime) {
                    arrayList = allAlarm.get(i).getPathItemArrayList();
                    break;
                }
            }
        }

        Log.e("[Log} curSize", arrayList.size()+"");
        return arrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "destroyed");
        if(mapViewContainer != null){
            mapViewContainer.removeAllViews();
        }
    }

    public void initView() {
        // 지도
        if(mapViewContainer.getChildCount() == 0){
            if(mapView == null){
                mapView = new MapView(getContext());
            }
            mapViewContainer.addView(mapView);
        }
    }

    public void setLocation() {
        // Location 제공자에서 정보를 얻어오기(GPS)
        // 1. Location을 사용하기 위한 권한을 얻어와야한다 AndroidManifest.xml
        //     ACCESS_FINE_LOCATION : NETWORK_PROVIDER, GPS_PROVIDER
        //     ACCESS_COARSE_LOCATION : NETWORK_PROVIDER
        // 2. LocationManager 를 통해서 원하는 제공자의 리스너 등록
        // 3. GPS 는 에뮬레이터에서는 기본적으로 동작하지 않는다
        // 4. 실내에서는 GPS_PROVIDER 를 요청해도 응답이 없다.  특별한 처리를 안하면 아무리 시간이 지나도
        //    응답이 없다.
        //    해결방법은
        //     ① 타이머를 설정하여 GPS_PROVIDER 에서 일정시간 응답이 없는 경우 NETWORK_PROVIDER로 전환
        //     ② 혹은, 둘다 한꺼번헤 호출하여 들어오는 값을 사용하는 방식.
        //출처: http://bitsoul.tistory.com/131 [Happy Programmer~]
        // LocationManager 객체를 얻어온다
        // getPermission();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 위치정보를 얻는다
        getLocation();
    }

    public void getLocation() {
        try {
            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            //txtCurrentPositionInfo.setText("위치정보 미수신중");
            //mLocationManager.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
        } catch (SecurityException ex) {

        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public void setDaumMapCurrentLocation(double latitude, double longitude) {
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(3, true);
        // 중심점 변경 + 줌 레벨 변경
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 9, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        //mapView.zoomOut(true);
        // 마커 생성
        setDaumMapCurrentMarker();
    }
    public void setDaumMapCurrentMarker(){
        mapView.removeAllPOIItems();
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setCustomImageResourceId(R.drawable.yellow_map_marker);
        marker.setCustomImageAutoscale(false);
        marker.setCustomImageAnchor(0.5f, 1.0f);
        //marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }


    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(ALARMLIST, "");
        ArrayList<AlarmItem> alarmItemArrayList = new ArrayList<>();
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<AlarmItem>>() {
            }.getType();
            alarmItemArrayList = gson.fromJson(json, type);
        }
        Log.e("alarmArrayListSize", alarmItemArrayList.size()+"");
        return alarmItemArrayList;
    }

}
