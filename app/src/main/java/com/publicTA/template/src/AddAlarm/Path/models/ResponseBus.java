package com.publicTA.template.src.AddAlarm.Path.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBus {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<Transit> result;

    public class Transit {
        @SerializedName("trId")
        private int trId;
        @SerializedName("trName")
        private String trName;
        @SerializedName("busType")
        private int busType;
        @SerializedName("nextId")
        private int nextId;
        @SerializedName("nextName")
        private String nextName;

        public int getTrId() {
            return trId;
        }

        public String getName() {
            return trName;
        }

        public int getBusType() {
            return busType;
        }

        public int getNextId() {
            return nextId;
        }

        public String getNextName() {
            return nextName;
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

    public ArrayList<Transit> getResult() {
        return result;
    }
}
