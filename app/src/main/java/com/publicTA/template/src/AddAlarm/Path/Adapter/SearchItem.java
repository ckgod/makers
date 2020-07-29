package com.publicTA.template.src.AddAlarm.Path.Adapter;

public class SearchItem {
    String stationName;
    String direction;

    public SearchItem(String stationName, String direction) {
        this.stationName = stationName;
        this.direction = direction;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
