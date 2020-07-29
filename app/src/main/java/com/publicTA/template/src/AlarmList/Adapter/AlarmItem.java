package com.publicTA.template.src.AlarmList.Adapter;

import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;

import java.io.Serializable;
import java.util.ArrayList;

public class AlarmItem implements Serializable {
    ArrayList<PathItem> pathItemArrayList;
    int startAMPMType;
    int startHours;
    int startMinutes;
    int endHours;
    int endMinutes;
    int endAMPMType;
    String dayCycle;
    boolean onOff;
    String label;

    public AlarmItem(ArrayList<PathItem> pathItemArrayList, int startAMPMType, int startHours, int startMinutes, int endAMPMType, int endHours, int endMinutes, String dayCycle, boolean onOff, String label) {
        this.pathItemArrayList = pathItemArrayList;
        this.startAMPMType = startAMPMType;
        this.startHours = startHours;
        this.startMinutes = startMinutes;
        this.endHours = endHours;
        this.endMinutes = endMinutes;
        this.endAMPMType = endAMPMType;
        this.dayCycle = dayCycle;
        this.onOff = onOff;
        this.label = label;
    }

    public ArrayList<PathItem> getPathItemArrayList() {
        return pathItemArrayList;
    }

    public void setPathItemArrayList(ArrayList<PathItem> pathItemArrayList) {
        this.pathItemArrayList = pathItemArrayList;
    }

    public int getStartAMPMType() {
        return startAMPMType;
    }

    public void setStartAMPMType(int startAMPMType) {
        this.startAMPMType = startAMPMType;
    }

    public int getStartHours() {
        return startHours;
    }

    public void setStartHours(int startHours) {
        this.startHours = startHours;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public int getEndHours() {
        return endHours;
    }

    public void setEndHours(int endHours) {
        this.endHours = endHours;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    public int getEndAMPMType() {
        return endAMPMType;
    }

    public void setEndAMPMType(int endAMPMType) {
        this.endAMPMType = endAMPMType;
    }

    public String getDayCycle() {
        return dayCycle;
    }

    public void setDayCycle(String dayCycle) {
        this.dayCycle = dayCycle;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
