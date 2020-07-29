package com.publicTA.template.src.FindRoad;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.publicTA.template.R;

import static android.app.Activity.RESULT_OK;

public class FragmentFindRoad extends Fragment {
    private WebView mWvBrowser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_road , container, false);
        mWvBrowser = rootView.findViewById(R.id.address_web_view);
        mWvBrowser.setWebViewClient(new WebViewClient());
        mWvBrowser.getSettings().setJavaScriptEnabled(true);
       // mWvBrowser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        mWvBrowser.loadUrl("https://m.map.kakao.com/actions/routeView");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocationPermission();

    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            }
        }
    }

    public boolean webBackPressed() {
        if (mWvBrowser.getOriginalUrl().equalsIgnoreCase("https://m.map.kakao.com/actions/routeView")) {
            return true;
        }else if(mWvBrowser.canGoBack()){
            mWvBrowser.goBack();
            return false;
        }else{
            return true;
        }
    }


    private void GPSSetting() {
        ContentResolver res = getActivity().getContentResolver();

        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if(!gpsEnabled) {
            new AlertDialog.Builder(getContext()).setTitle("GPS 설정")
                    .setMessage("GPS를 사용하시겠습니까?")
                    .setPositiveButton("사용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("거절", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

//    class MyJavaScriptInterface
//    {
//        @JavascriptInterface
//        @SuppressWarnings("unused")
//        public void processDATA(String data) {
//            //System.out.println("data = " + data);
//            String result = "";
//            String[] tempList = data.split(" ");
//            if (tempList.length >= 1) {
//                result += tempList[0];
//                result += " ";
//            }
//            if (tempList.length >= 2) {
//                result += tempList[1];
//                result += " ";
//            }
//            if (tempList.length >= 3) {
//                result += tempList[2];
//            }
//            Bundle extra = new Bundle();
//            Intent intent = new Intent();
//            extra.putString("data", result);
//            intent.putExtras(extra);
//            getActivity().setResult(RESULT_OK, intent);
//            getActivity().finish();
//        }
//    }
}
