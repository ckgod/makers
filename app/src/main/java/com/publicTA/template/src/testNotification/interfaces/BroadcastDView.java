package com.publicTA.template.src.testNotification.interfaces;

import com.publicTA.template.src.testNotification.models.ResponseLastTime;
import com.publicTA.template.src.testNotification.models.ResponseLeftTime;


public interface BroadcastDView {
    void getLeftTimeSuccess(ResponseLeftTime.LeftTime result);

    void getLeftTimeFailure(String message);

    void getLastTimeSuccess(ResponseLastTime.LastTime result);

    void getLastTimeFailure(String message);
}
