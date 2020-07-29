package com.publicTA.template.src.AddAlarm.Path.Adapter;

import java.io.Serializable;

public class PathItem implements Serializable {
    int transitId;
    int StationId;
    int transitType;
    String startStation;
    String direction;
    String transitName;
    int nextStationId;
    String nextStationName;


    int subwayType;
    int LeftTime;
    int secondLeftTime;
    int LeftStation;
    int secondLeftStation;

    public PathItem(int transitId, int stationId, int transitType, String startStation, String direction, String transitName, int nextStationId, String nextStationName) {
        this.transitId = transitId;
        this.StationId = stationId;
        this.transitType = transitType;
        this.startStation = startStation;
        this.direction = direction;
        this.transitName = transitName;
        this.nextStationId = nextStationId;
        this.nextStationName = nextStationName;
        subwayType = -1;
        LeftTime = -1;
        secondLeftTime = -1;
        LeftStation = -1;
        secondLeftStation = -1;
    }

    public int getTransitId() {
        return transitId;
    }

    public void setTransitId(int transitId) {
        this.transitId = transitId;
    }

    public int getStationId() {
        return StationId;
    }

    public void setStationId(int stationId) {
        StationId = stationId;
    }

    public int getTransitType() {
        return transitType;
    }

    public void setTransitType(int transitType) {
        this.transitType = transitType;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTransitName() {
        return transitName;
    }

    public void setTransitName(String transitName) {
        this.transitName = transitName;
    }

    public int getNextStationId() {
        return nextStationId;
    }

    public void setNextStationId(int nextStationId) {
        this.nextStationId = nextStationId;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public void setNextStationName(String nextStationName) {
        this.nextStationName = nextStationName;
    }

    public int getSubwayType() {
        return subwayType;
    }

    public void setSubwayType(int subwayType) {
        this.subwayType = subwayType;
    }

    public int getLeftTime() {
        return LeftTime;
    }

    public void setLeftTime(int leftTime) {
        LeftTime = leftTime;
    }

    public int getSecondLeftTime() {
        return secondLeftTime;
    }

    public void setSecondLeftTime(int secondLeftTime) {
        this.secondLeftTime = secondLeftTime;
    }

    public int getLeftStation() {
        return LeftStation;
    }

    public void setLeftStation(int leftStation) {
        LeftStation = leftStation;
    }

    public int getSecondLeftStation() {
        return secondLeftStation;
    }

    public void setSecondLeftStation(int secondLeftStation) {
        this.secondLeftStation = secondLeftStation;
    }
}
