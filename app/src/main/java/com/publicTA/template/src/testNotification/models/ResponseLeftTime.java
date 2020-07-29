package com.publicTA.template.src.testNotification.models;

import com.google.gson.annotations.SerializedName;

public class ResponseLeftTime {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private LeftTime result;

    public class LeftTime {
        @SerializedName("trName")
        private String trName;

        @SerializedName("stName")
        private String stName;

        @SerializedName("type")
        private int type;

        @SerializedName("leftTime")
        private int leftTime;

        @SerializedName("location")
        private int location;

        @SerializedName("subwayType")
        private int subwayType;

        public String getTrName() {
            return trName;
        }

        public String getStName() {
            return stName;
        }

        public int getType() {
            return type;
        }

        public int getLeftTime() {
            return leftTime;
        }

        public int getLocation() {
            return location;
        }

        public int getSubwayType() {
            return subwayType;
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

    public LeftTime getResult() {
        return result;
    }
}
