package com.llthx.pithy;

import com.llthx.llog.LLog;
import com.llthx.pithy.event.PithyEventCallback;

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

    public boolean register(PithyPrototype pithyPrototype, PithyThread pithyThread) {
        LLog.d(TAG,"register(), eventKey : " + pithyPrototype.getEventKey() + " , pithyThread : " + pithyThread.getPithyThreadName());

        return pithyThread.putEvent(pithyPrototype);
    }

    public boolean unRegister(String TAG, String key) {
        return unRegister(TAG, key, PithyThread.MAIN_THREAD);
    }

    public boolean unRegister(String TAG, String key, PithyThread pithyThread) {
        return pithyThread.removeEvent(TAG, key);
    }

    public boolean unRegister(String TAG) {
        return unRegister(TAG,PithyThread.MAIN_THREAD);
    }

    public boolean unRegister(String TAG, PithyThread pithyThread) {
        return pithyThread.removeAllTagEvent(TAG);
    }

    public void unRegisterAllEvent() {
        unRegisterAllEvent(PithyThread.MAIN_THREAD);
    };

    public void unRegisterAllEvent(PithyThread pithyThread) {
        LLog.d(TAG,"unRegisterAllEvent()");

        pithyThread.removeAllEvent();
    };

    public void dumpAllEvent() {
        dumpAllEvent(PithyThread.MAIN_THREAD);
    };

    public String[] dumpAllEvent(PithyThread pithyThread) {
        LLog.d(TAG,"dumpAllEvent(), pithyThreadName: " + pithyThread.getPithyThreadName());

        return pithyThread.dumpAllEvent();
    };

    public String[] dumpTAGEvent() {
        return dumpAllEvent(PithyThread.MAIN_THREAD);
    };

    public String[] dumpAllThreadTAGEvent(String TAG) {
        LLog.d(this.TAG,"dumpAllThreadTAGEvent(), TAG : " + TAG);

        String[] MAIN_THREAD = PithyThread.MAIN_THREAD.dumpAllTagEvent(TAG);
        String[] NORMAL_THREAD = PithyThread.NORMAL_THREAD.dumpAllTagEvent(TAG);
        String[] ASYNC_THREAD = PithyThread.ASYNC_THREAD.dumpAllTagEvent(TAG);
        List<String> list = Arrays.asList(MAIN_THREAD);
        list.addAll(Arrays.asList(NORMAL_THREAD));
        list.addAll(Arrays.asList(ASYNC_THREAD));

        return (String[]) list.toArray();
    };

    public String[] dumpAllThreadEvent() {
        LLog.d(TAG,"dumpAllThreadEvent()");

        String[] MAIN_THREAD = PithyThread.MAIN_THREAD.dumpAllEvent();
        String[] NORMAL_THREAD = PithyThread.NORMAL_THREAD.dumpAllEvent();
        String[] ASYNC_THREAD = PithyThread.ASYNC_THREAD.dumpAllEvent();

        List<String> list = Arrays.asList(MAIN_THREAD);
        list.addAll(Arrays.asList(NORMAL_THREAD));
        list.addAll(Arrays.asList(ASYNC_THREAD));

        return (String[]) list.toArray();
    };

    public void post(String TAG, String key){

    }

    public void post(String TAG, String key, PithyThread pithyThread){
        pithyThread.post(TAG, key);
    }
}
