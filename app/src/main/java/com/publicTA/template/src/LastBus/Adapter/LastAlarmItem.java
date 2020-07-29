package com.publicTA.template.src.LastBus.Adapter;

import com.publicTA.template.src.AddAlarm.Path.Adapter.PathItem;

import java.io.Serializable;

public class LastAlarmItem implements Serializable{
    PathItem pathItem;
    String dayCycle;
    boolean onOff;

    public LastAlarmItem(PathItem pathItem, String dayCycle, boolean onOff) {
        this.pathItem = pathItem;
        this.dayCycle = dayCycle;
        this.onOff = onOff;
    }

    public PathItem getPathItem() {
        return pathItem;
    }

    public void setPathItem(PathItem pathItem) {
        this.pathItem = pathItem;
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
}
