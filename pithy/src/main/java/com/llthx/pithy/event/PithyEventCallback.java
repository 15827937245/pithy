package com.llthx.pithy.event;

import com.llthx.pithy.PithyCallBack;

import org.json.JSONObject;

public interface PithyEventCallback {
    default void  pithyEventCallback(String eventKey, String jsonData){};

    default void pithyEventCallback(String eventKey, String jsonData, PithyCallBack callBack) {
        if (null == callBack){
            pithyEventCallback(eventKey, jsonData);
        }
    }
}
