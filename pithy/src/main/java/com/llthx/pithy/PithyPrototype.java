package com.llthx.pithy;

import com.llthx.llog.LLog;
import com.llthx.pithy.event.PithyEventCallback;

public class PithyPrototype {
    private String TAG;
    private static final String PithyPrototype_TAG = "PithyPrototype";

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

    public PithyPrototype(String eventKey, PithyEventCallback callback) throws Exception {
        String[] strings = disassembleEventKey(eventKey);

        if (null != strings) {
            this.TAG = strings[0];
            this.key = strings[1];
        } else {
            LLog.e(PithyPrototype_TAG,"eventKey not conform to the regulation!");

            throw new Exception();
        }

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

    public static String[] disassembleEventKey(String eventKey) {
        if (eventKey.contains(PITHY_TAG)) {
            return eventKey.split(PITHY_TAG);
        } else {
            LLog.e(PithyPrototype_TAG,"eventKey not conform to the regulation!");

            return null;
        }
    }

    public String getEventKey() {
        return eventKey;
    }

}
