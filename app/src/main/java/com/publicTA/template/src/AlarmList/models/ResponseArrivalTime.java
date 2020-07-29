package com.publicTA.template.src.AlarmList.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseArrivalTime {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<arrivalTime> result;

    public class arrivalTime {
        @SerializedName("trName")
        private String trName;

        @SerializedName("stName")
        private String stName;

        @SerializedName("type")
        private int type;

        @SerializedName("nextStName")
        private String nextStName;

        @SerializedName("leftTime")
        private int leftTime;

        @SerializedName("secondLeftTime")
        private int secondLeftTime;

        @SerializedName("subwayType")
        private int subwayType;

        @SerializedName("location")
        private int location;

        @SerializedName("secondLocation")
        private int secondLocation;

        public String getTrName() {
            return trName;
        }

        public String getStName() {
            return stName;
        }

        public int getType() {
            return type;
        }

        public String getNextStName() {
            return nextStName;
        }

        public int getLeftTime() {
            return leftTime;
        }

        public int getSecondLeftTime() {
            return secondLeftTime;
        }

        public int getSubwayType() {
            return subwayType;
        }

        public int getLocation() {
            return location;
        }

        public int getSecondLocation() {
            return secondLocation;
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<arrivalTime> getResult() {
        return result;
    }


}
