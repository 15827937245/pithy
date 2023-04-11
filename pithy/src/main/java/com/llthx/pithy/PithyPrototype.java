package com.llthx.pithy;

import com.llthx.pithy.event.PithyEventCallback;

public class PithyPrototype {
    private String TAG;

    private String key;

    private PithyEventCallback callback;

    private String eventKey;

    public static final String PITHY_TAG = "_pithy_";

    public PithyPrototype(String TAG, String key, PithyEventCallback callback) {
        this.TAG = TAG;
        this.key = key;
        this.callback = callback;
        this.eventKey = generateEventKey(this.TAG, this.key);
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCallback(PithyEventCallback callback) {
        this.callback = callback;
    }

    public String getTAG() {
        return TAG;
    }

    public String getKey() {
        return key;
    }

    public PithyEventCallback getCallback() {
        return callback;
    }

    public static String generateEventKey(String TAG, String key) {
        return TAG + PITHY_TAG + key;
    }

    public String getEventKey() {
        return eventKey;
    }

}
