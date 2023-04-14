package com.llthx.pithy;

import com.llthx.pithy.event.PithySubscriptionCallback;

public class PithySubscriptionPrototype {
    private String key;
    private int Priority;
    private PithySubscriptionCallback callback;

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MID = 5;
    public static final int PRIORITY_HIGH = 10;

    public PithySubscriptionPrototype(String key, int priority, PithySubscriptionCallback callback) {
        this.key = key;
        Priority = priority;
        this.callback = callback;
    }

    public PithySubscriptionPrototype(String key, PithySubscriptionCallback callback) {
        this.key = key;
        Priority = PRIORITY_LOW;
        this.callback = callback;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public PithySubscriptionCallback getCallback() {
        return callback;
    }

    public void setCallback(PithySubscriptionCallback callback) {
        this.callback = callback;
    }
}
