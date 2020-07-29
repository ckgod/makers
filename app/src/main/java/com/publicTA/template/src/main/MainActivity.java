package com.publicTA.template.src.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicTA.template.R;
import com.publicTA.template.src.AlarmList.Adapter.AlarmItem;
import com.publicTA.template.src.BaseActivity;
import com.publicTA.template.src.AlarmList.FragmentAlarmList;
import com.publicTA.template.src.currentState.FragmentCurState;
import com.publicTA.template.src.FindRoad.FragmentFindRoad;
import com.publicTA.template.src.LastBus.FragmentLastBus;
import com.publicTA.template.src.main.interfaces.MainActivityView;
import com.publicTA.template.src.testNotification.AlarmSet;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainActivityView {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    private static final String ALARMLIST = "alarmList";
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private FragmentAlarmList mFragmentAlarmList = new FragmentAlarmList();
    private FragmentFindRoad mFragmentFindRoad;
    private FragmentLastBus mFragmentLastBus;
    private FragmentCurState mFragmentCurState;
    private DrawerLayout mDrawrLayout;

    ImageButton mBtnCurrent;
    boolean onCurrentState = false;

    ArrayList<AlarmItem> arrayList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private int prevFragmentIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // note hihi!!!
        //new AlarmSet(getApplicationContext()).Alarm();

        mDrawrLayout = findViewById(R.id.main_drawer);
        mDrawrLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_frameLayout, mFragmentAlarmList).commit();

        bottomNavigationView = findViewById(R.id.main_bottom_nav_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        mBtnCurrent = findViewById(R.id.main_btn_current);
        onCurrentState = false;

        mBtnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFragmentIdx = 3;
                if(mFragmentCurState == null) {
                    mFragmentCurState = new FragmentCurState();
                    mFragmentManager.beginTransaction().add(R.id.main_frameLayout, mFragmentCurState).commit();
                    mBtnCurrent.setImageResource(R.drawable.logo_action_true);
                    bottomNavigationView.setSelectedItemId(R.id.menu_temp);
                    return;
                }
                if(mFragmentCurState != null) {
                    mFragmentManager.beginTransaction().show(mFragmentCurState).commit();
                    mFragmentCurState.curF5();
                    mBtnCurrent.setImageResource(R.drawable.logo_action_true);
                    bottomNavigationView.setSelectedItemId(R.id.menu_temp);
                }
                if(mFragmentAlarmList != null) {
                    mFragmentManager.beginTransaction().hide(mFragmentAlarmList).commit();
                }
                if(mFragmentFindRoad != null) {
                    mFragmentManager.beginTransaction().hide(mFragmentFindRoad).commit();
                }
                if(mFragmentLastBus != null) {
                    mFragmentManager.beginTransaction().hide(mFragmentLastBus).commit();
                }
            }
        });

    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_alarm_list:
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.menu_alarm_list_selector);
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.menu_last_but_selector);
                    prevFragmentIdx = 0;
                    if (mFragmentAlarmList == null) {
                        mFragmentAlarmList = new FragmentAlarmList();
                        mFragmentManager.beginTransaction().add(R.id.main_frameLayout, mFragmentAlarmList).commit();
                    }
                    if (mFragmentAlarmList != null) {
                        mFragmentManager.beginTransaction().show(mFragmentAlarmList).commit();
                        arrayList = loadAlarmList();
                        mFragmentAlarmList.F5(arrayList);
                    }
                    if (mFragmentFindRoad != null)
                        mFragmentManager.beginTransaction().hide(mFragmentFindRoad).commit();
                    if (mFragmentLastBus != null)
                        mFragmentManager.beginTransaction().hide(mFragmentLastBus).commit();
                    if (mFragmentCurState != null) {
                        mFragmentManager.beginTransaction().hide(mFragmentCurState).commit();
                        mBtnCurrent.setImageResource(R.drawable.ic_btn_fab3);
                    }
                    break;
                case R.id.menu_item_find_road:
                    menuItem.setCheckable(false);
                    showCustomToast("준비중 입니다.");
                    if(prevFragmentIdx == 0) {
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_bottom_menu_first_true);
                    }
                    else if(prevFragmentIdx == 2) {
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_bottom_menu_third_true);
                    }
                    else if(prevFragmentIdx == 3) {
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        mBtnCurrent.setImageResource(R.drawable.logo_action_true);
                        bottomNavigationView.setSelectedItemId(R.id.menu_temp);
                    }
//                    if (mFragmentFindRoad == null) {
//                        mFragmentFindRoad = new FragmentFindRoad();
//                        mFragmentManager.beginTransaction().add(R.id.main_frameLayout, mFragmentFindRoad).commit();
//                    }
//                    if (mFragmentAlarmList != null)
//                        mFragmentManager.beginTransaction().hide(mFragmentAlarmList).commit();
//                    if (mFragmentFindRoad != null)
//                        mFragmentManager.beginTransaction().show(mFragmentFindRoad).commit();
//                    if (mFragmentLastBus != null)
//                        mFragmentManager.beginTransaction().hide(mFragmentLastBus).commit();
//                    if (mFragmentCurState != null) {
//                        mFragmentManager.beginTransaction().hide(mFragmentCurState).commit();
//                        mBtnCurrent.setImageResource(R.drawable.ic_btn_fab3);
//                    }
                    break;
                case R.id.menu_item_last_bus:
                    prevFragmentIdx = 2;
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.menu_alarm_list_selector);
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.menu_last_but_selector);
                    if (mFragmentLastBus == null) {
                        mFragmentLastBus = new FragmentLastBus();
                        mFragmentManager.beginTransaction().add(R.id.main_frameLayout, mFragmentLastBus).commit();
                    }
                    if (mFragmentAlarmList != null)
                        mFragmentManager.beginTransaction().hide(mFragmentAlarmList).commit();
                    if (mFragmentFindRoad != null)
                        mFragmentManager.beginTransaction().hide(mFragmentFindRoad).commit();
                    if (mFragmentLastBus != null)
                        mFragmentManager.beginTransaction().show(mFragmentLastBus).commit();
                    if (mFragmentCurState != null) {
                        mFragmentManager.beginTransaction().hide(mFragmentCurState).commit();
                        mBtnCurrent.setImageResource(R.drawable.ic_btn_fab3);
                    }
                    break;
            }
            return true;
        }
    }

    public void openDrawer() {
        mDrawrLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;


        if(mDrawrLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawrLayout.closeDrawer(Gravity.LEFT);
        }
//        else if(mFragmentFindRoad != null && !mFragmentFindRoad.webBackPressed()) { // note 길찾기 웹뷰 오류너무많음
//        }
        else if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            showCustomToast("뒤로 가기 버튼을 한번 더 누르면 종료됩니다.");
        }
    }

    private ArrayList<AlarmItem> loadAlarmList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

    @Override
    public void validateSuccess(String text) {
        hideProgressDialog();
    }

    @Override
    public void validateFailure(@Nullable String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    public void customOnClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
