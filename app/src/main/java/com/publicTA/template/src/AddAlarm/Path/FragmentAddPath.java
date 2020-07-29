package com.publicTA.template.src.AddAlarm.Path;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.publicTA.template.R;
import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;
import com.publicTA.template.src.AddAlarm.Path.Adapter.SearchBusAdapter;
import com.publicTA.template.src.AddAlarm.Path.Adapter.SearchListAdapter;
import com.publicTA.template.src.AddAlarm.AddAlarmActivity;
import com.publicTA.template.src.AddAlarm.Path.Service.FragmentAddPathService;
import com.publicTA.template.src.AddAlarm.Path.interfaces.FragmentAddPathView;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseBus;
import com.publicTA.template.src.AddAlarm.Path.models.ResponseStation;
import com.publicTA.template.src.LastBus.AddLastBusActivity;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class FragmentAddPath extends BottomSheetDialogFragment implements FragmentAddPathView {

    public AlertDialog mProgressDialog;
    public Context mContext = null;
    private EditText mEtSearchStation;
    private Button mBtnComplete;

    // 리스트뷰 도구들
    private ListView mListViewStation;
    private SearchListAdapter mSearchStationAdapter;
    private ArrayList<ResponseStation.Station> mStationArray = new ArrayList<>();

    String mStation, mBusName, mDirection, mNextStationName;
    int mStationID, mStationType, mTransitId, mNextStationId;

    boolean select;

    private TextView mTvBusTitle, mTvBusName;
    private View mVBusLine;
    private ListView mListViewBus;
    private SearchBusAdapter mSearchBusAdapter;
    private ArrayList<ResponseBus.Transit> mBusArray = new ArrayList<>();

    // note Service
    FragmentAddPathService mFragmentAddPathService;
    PathItem pathItem;
    private int type;

    public FragmentAddPath(int type) {
        this.type = type;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) { // note 바텀시트 세팅
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(),
                R.layout.fragment_add_path, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_path, container,false);
        mEtSearchStation = view.findViewById(R.id.add_path_search_station);
        mBtnComplete = view.findViewById(R.id.add_path_complete_button);
        mFragmentAddPathService = new FragmentAddPathService(this);
        mListViewStation = view.findViewById(R.id.add_path_list_station);
        mTvBusTitle = view.findViewById(R.id.add_path_tv_bus_title);
        mTvBusName = view.findViewById(R.id.add_path_tv_bus_name);
        mVBusLine = view.findViewById(R.id.add_path_bus_line);
        mListViewBus = view.findViewById(R.id.add_path_list_bus);
        select = false;
        pathItem = null;

        // note searchBar 세팅하고 station 정보들 전부 받아오기
        mEtSearchStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tryGetStation(mEtSearchStation.getText().toString());
                }
                return false;
            }
        });

        mListViewStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mStation = mStationArray.get(position).getStName();
                mDirection = mStationArray.get(position).getStLine();
                mStationID = mStationArray.get(position).getStId();
                mStationType = mStationArray.get(position).getStType();

                mEtSearchStation.setText(mStation);
                mStationArray.clear();
                mSearchStationAdapter.notifyDataSetChanged();
                mTvBusTitle.setVisibility(View.VISIBLE);
                mTvBusName.setVisibility(View.VISIBLE);
                mVBusLine.setVisibility(View.VISIBLE);
                mListViewBus.setVisibility(View.VISIBLE);
                tryGetBusList(String.valueOf(mStationID));
            }
        });

        mListViewBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResponseBus.Transit item = mBusArray.get(position);
                mTvBusName.setText(item.getName());
                if(mStationType == 1) {
                    pathItem = new PathItem(item.getTrId(),
                            mStationID,
                            mStationType,
                            mStation,
                            mDirection,
                            item.getName(),
                            0,
                            String.valueOf(item.getBusType())
                            );
                    select = true;
                }
                else if(mStationType == 2){
                    pathItem = new PathItem(item.getTrId(),
                            mStationID,
                            mStationType,
                            mStation,
                            mDirection,
                            item.getName(),
                            item.getNextId(),
                            item.getNextName()
                    );
                    select = true;
                }
                else {
                    Toast.makeText(mContext,"정류장 정보가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult();
            }
        });
        return view;
    }

    public void tryGetStation(String charText) {
        showProgressDialog();
        mFragmentAddPathService.getStation(mEtSearchStation.getText().toString());
    }

    public void tryGetBusList(String stId) {
        showProgressDialog();
        mFragmentAddPathService.getBus(stId);
    }

    @Override // note 여기 바텀시트 세팅
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        View bottomSheet = null;
        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        final View view = getView();
        final View finalBottomSheet = bottomSheet;
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                final CoordinatorLayout.Behavior behavior = params.getBehavior();
                final BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                bottomSheetBehavior.setPeekHeight((int) (view.getMeasuredHeight()));
                bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }
                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // React to dragging events
                    }
                });
                ((View) finalBottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT);

            }
        });
    }

    public void setResult() { // note 경로 추가 버튼 클릭시 예외처리 해야함
        if(!select) {
            Toast.makeText(mContext,"정보를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(this.type == 1) {
                ((AddAlarmActivity)getActivity()).addPathItem(pathItem);
                dismiss();
            }
            else if(this.type == 2) {
                ((AddLastBusActivity)getActivity()).resultSet(pathItem);
                dismiss();
            }
        }
    }

    @Override
    public void getStationSuccess(boolean isSuccess, int code, String message, ArrayList<ResponseStation.Station> result) {
        // note 여기서 station 정보 다 받아와서 어댑터 세팅
        mStationArray = result;
        mSearchStationAdapter = new SearchListAdapter(mStationArray,getContext(),R.layout.search_list_item);
        mListViewStation.setAdapter(mSearchStationAdapter);
        hideProgressDialog();
    }

    @Override
    public void getStationFailure(String message) {
        hideProgressDialog();
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT);
    }

    @Override
    public void getBusSuccess(boolean isSuccess, int code, String message, ArrayList<ResponseBus.Transit> result) {
        mBusArray = result;
        mSearchBusAdapter = new SearchBusAdapter(mBusArray,getContext(),R.layout.search_bus_item);
        mListViewBus.setAdapter(mSearchBusAdapter);
        hideProgressDialog();
    }

    @Override
    public void getBusFailure() {
        hideProgressDialog();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.customProgressDialog).build();
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}

