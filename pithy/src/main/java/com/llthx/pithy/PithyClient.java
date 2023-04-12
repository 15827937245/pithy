package com.llthx.pithy;

import com.llthx.llog.LLog;
import com.llthx.pithy.event.PithyEventCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PithyClient {
    private final String TAG = "PithyClient";
    private static volatile PithyClient mInstance;
    private static Object mLock = new Object();

    private PithyClient(){};

    public static PithyClient getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new PithyClient();

                    LLog.setALL(true);
                }
            }
        }

        return mInstance;
    }


    public boolean register(String TAG, String key, PithyEventCallback callback) {
        return register(TAG, key, callback, PithyThread.MAIN_THREAD);
    }

    public boolean register(String TAG, String key, PithyEventCallback callback, PithyThread pithyThread) {
        return register(new PithyPrototype(TAG, key, callback), pithyThread);
    }

    public boolean register(String eventKey, PithyEventCallback callback) {
        return register(eventKey, callback, PithyThread.MAIN_THREAD);
    }

    public boolean register(String eventKey, PithyEventCallback callback, PithyThread pithyThread) {
        PithyPrototype pithyPrototype = null;

        try {
            pithyPrototype = new PithyPrototype(eventKey, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return register(pithyPrototype,pithyThread);
    }

    public boolean register(PithyPrototype pithyPrototype, PithyThread pithyThread) {
        LLog.d(TAG,"register(), eventKey : " + pithyPrototype.getEventKey() + " , pithyThread : " + pithyThread.getPithyThreadName());

        if (!pithyThread.isInit()) {
            pithyThread.init();
        }

        return pithyThread.putEvent(pithyPrototype);
    }

    public boolean unRegister(String TAG, String key) {
        return unRegister(TAG, key, PithyThread.MAIN_THREAD);
    }

    public boolean unRegister(String TAG, String key, PithyThread pithyThread) {
        if (!pithyThread.isInit()) {
            LLog.e(this.TAG,"unRegister(), " + pithyThread.getPithyThreadName() + "is not init!");

            return false;
        }

        LLog.d(TAG,"unRegister(), eventKey : " + PithyPrototype.generateEventKey(TAG, key) + " , pithyThread : " + pithyThread.getPithyThreadName());

        return pithyThread.removeEvent(TAG, key);
    }

    public boolean unRegister(String TAG) {
        return unRegister(TAG,PithyThread.MAIN_THREAD);
    }

    public boolean unRegister(String TAG, PithyThread pithyThread) {
        if (!pithyThread.isInit()) {
            LLog.e(this.TAG,"unRegister(), " + pithyThread.getPithyThreadName() + "is not init!");

            return false;
        }

        LLog.d(TAG,"unRegister(), TAG : " + TAG + " , pithyThread : " + pithyThread.getPithyThreadName());

        return pithyThread.removeAllTagEvent(TAG);
    }

    public void unRegisterAllEvent() {
        unRegisterAllEvent(PithyThread.MAIN_THREAD);
    };

    public void unRegisterAllEvent(PithyThread pithyThread) {
        if (!pithyThread.isInit()) {
            LLog.e(TAG,"unRegisterAllEvent(), " + pithyThread.getPithyThreadName() + "is not init!");

            return;
        }

        LLog.d(TAG,"unRegisterAllEvent()");

        pithyThread.removeAllEvent();
    };

    public void dumpAllEvent() {
        dumpAllEvent(PithyThread.MAIN_THREAD);
    };

    public String[] dumpAllEvent(PithyThread pithyThread) {
        if (!pithyThread.isInit()) {
            LLog.e(TAG,"dumpAllEvent(), " + pithyThread.getPithyThreadName() + "is not init!");

            return null;
        }

        LLog.d(TAG,"dumpAllEvent(), pithyThreadName: " + pithyThread.getPithyThreadName());

        return pithyThread.dumpAllEvent();
    };

    public String[] dumpTAGEvent() {
        return dumpAllEvent(PithyThread.MAIN_THREAD);
    };

    public String[] dumpAllThreadTAGEvent(String TAG) {
        LLog.d(this.TAG,"dumpAllThreadTAGEvent(), TAG : " + TAG);

        List<String> list = new ArrayList<>();

        if (PithyThread.MAIN_THREAD.isInit()) {
            List MAIN_THREAD = Arrays.asList(PithyThread.MAIN_THREAD.dumpAllTagEvent(TAG));

            if (!MAIN_THREAD.isEmpty()) {
                list.addAll(MAIN_THREAD);
            }
        }

        if (PithyThread.NORMAL_THREAD.isInit()) {
            List NORMAL_THREAD = Arrays.asList(PithyThread.NORMAL_THREAD.dumpAllTagEvent(TAG));

            if (!NORMAL_THREAD.isEmpty()) {
                list.addAll(NORMAL_THREAD);
            }
        }

        if (PithyThread.ASYNC_THREAD.isInit()) {
            List ASYNC_THREAD = Arrays.asList(PithyThread.ASYNC_THREAD.dumpAllTagEvent(TAG));

            if (!ASYNC_THREAD.isEmpty()) {
                list.addAll(ASYNC_THREAD);
            }
        }

        return (String[]) list.toArray();
    };

    public String[] dumpAllThreadEvent() {
        LLog.d(TAG,"dumpAllThreadEvent()");

        List<String> list = new ArrayList<>();

        if (PithyThread.MAIN_THREAD.isInit()) {
            List MAIN_THREAD = Arrays.asList(PithyThread.MAIN_THREAD.dumpAllTagEvent(TAG));

            if (!MAIN_THREAD.isEmpty()) {
                list.addAll(MAIN_THREAD);
            }
        }

        if (PithyThread.NORMAL_THREAD.isInit()) {
            List NORMAL_THREAD = Arrays.asList(PithyThread.NORMAL_THREAD.dumpAllTagEvent(TAG));

            if (!NORMAL_THREAD.isEmpty()) {
                list.addAll(NORMAL_THREAD);
            }
        }

        if (PithyThread.ASYNC_THREAD.isInit()) {
            List ASYNC_THREAD = Arrays.asList(PithyThread.ASYNC_THREAD.dumpAllTagEvent(TAG));

            if (!ASYNC_THREAD.isEmpty()) {
                list.addAll(ASYNC_THREAD);
            }
        }

        return (String[]) list.toArray();
    };

    public void post(String TAG, String key){
        post(TAG, key, PithyThread.MAIN_THREAD);
    }

    public void post(String TAG, String key, String jsonData){
        post(TAG, key, jsonData, PithyThread.MAIN_THREAD);
    }

    public void post(String TAG, String key, PithyThread pithyThread){
        post(TAG, key,null, pithyThread);
    }

    public void post(String TAG, String key, String jsonData, PithyThread pithyThread){
        if (!pithyThread.isInit()) {
            LLog.e(this.TAG,"post(), " + pithyThread.getPithyThreadName() + "is not init!");

            return;
        }

        pithyThread.post(TAG, key, jsonData);
    }
}
