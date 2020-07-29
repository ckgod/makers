package com.publicTA.template.src.AddAlarm.Path.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseStation {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<Station> result;

    public class Station {
        @SerializedName("stId")
        private int stId;

        @SerializedName("stName")
        private String stName;

        @SerializedName("stType")
        private int stType;

        @SerializedName("stLine")
        private String stLine;

        public int getStId() {
            return stId;
        }

        public String getStName() {
            return stName;
        }

        public int getStType() {
            return stType;
        }

        public String getStLine() {
            return stLine;
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

    public ArrayList<Station> getStation() {
        return result;
    }
}
