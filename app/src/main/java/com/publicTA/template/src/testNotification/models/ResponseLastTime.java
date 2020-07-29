package com.publicTA.template.src.testNotification.models;

import com.google.gson.annotations.SerializedName;

public class ResponseLastTime {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ResponseLastTime.LastTime result;

    public class LastTime {
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

        @SerializedName("status")
        private int status;

        @SerializedName("subwayLeft")
        private int subwayLeft;

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

        public int getStatus() {
            return status;
        }

        public int getSubwayLeft() {
            return subwayLeft;
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

    public LastTime getResult() {
        return result;
    }
}
