package com.llthx.pithy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.llthx.llog.LLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public enum PithyThread {
    MAIN_THREAD{
        public String TAG = "PithyThread_MAIN_THREAD";

        @Override
        public void init() {
            handler = new Handler(Looper.myLooper());
            super.init();
        }
    },
    NORMAL_THREAD{
        private String TAG = "PithyThread_NORMAL_THREAD";
        private HandlerThread handlerThread;

        @Override
        public void init() {
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
            super.init();
        }

        @Override
        public String getPithyThreadName() {
            return "NORMAL_THREAD";
        }
    },
    ASYNC_THREAD{
        private String TAG = "PithyThread_ASYNC_THREAD";
        private ThreadPoolExecutor ThreadPool;
        private Map regeditMap = new HashMap<String, ArrayList<PithySubscriptionPrototype>>();
        
        private Object mLock = new Object();

        @Override
        public void init() {
            ThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            super.init();
        }

        @Override
        protected void post(String TAG, String key, String jsonData, PithyCallBack callBack) {
            LLog.d(this.TAG,"post , TAG : " + TAG + ", key : " + key);

            String eventKey = PithyPrototype.generateEventKey(TAG,key);

            if (null != map && map.containsKey(eventKey)) {
                LLog.d(this.TAG,"post , eventKey : " + eventKey);
                PithyPrototype pithyPrototype =(PithyPrototype) map.get(eventKey);

                if (null != ThreadPool) {
                    ThreadPool.execute(()-> pithyPrototype.getCallback().pithyEventCallback(eventKey, jsonData, callBack));
                } else {
                    LLog.e(this.TAG,"post() , eventKey does not exist!");
                }
            } else {
                LLog.e(this.TAG,"post() , eventKey does not exist!");
            }
        }

        @Override
        public String getPithyThreadName() {
            return "ASYNC_THREAD";
        }

        @Override
        protected void registerSubscription(PithySubscriptionPrototype prototype) {
            synchronized (mLock) {
                ArrayList<PithySubscriptionPrototype> list;
                if (regeditMap.containsKey(prototype.getKey())) {
                    list = (ArrayList<PithySubscriptionPrototype>) map.get(prototype.getKey());
                    if (list != null){
                        if (list.contains(prototype)) {
                            return;
                        }

                        if (list.isEmpty()) {
                            list.add(prototype);
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getPriority() < prototype.getPriority()) {
                                    list.add(i,prototype);
                                    break;
                                }
                            }

                            if (prototype.getPriority() < list.get(list.size()-1).getPriority()) {
                                list.add(prototype);
                            }
                        }
                    }
                } else {
                    list = new ArrayList<>();
                    list.add(prototype);
                }

                map.put(prototype.getKey(),list);
            }
        }

        protected void unRegisterSubscription(PithySubscriptionPrototype prototype) {
            synchronized (mLock) {
                ArrayList<PithySubscriptionPrototype> list;
                if (regeditMap.containsKey(prototype.getKey())) {
                    list = (ArrayList<PithySubscriptionPrototype>) map.get(prototype.getKey());
                    if (list != null && !list.isEmpty()){
                        if (list.contains(prototype)) {
                            list.remove(prototype);
                            map.put(prototype.getKey(),list);
                        }
                    }
                }

                return;
            }
        }

        protected void publish(String key ,Object obj) {
            ThreadPool.execute(()-> {
                ArrayList<PithySubscriptionPrototype> list = (ArrayList<PithySubscriptionPrototype>) map.get(key);
                if (null != list && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).getCallback().subscription(key, obj);
                    }
                }
            });
        }
    };

    protected Map map = new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>();
    private String TAG = "PithyThread";

    protected Handler handler;
    private boolean mbIsInit = false;

    protected boolean putEvent(PithyPrototype pithyPrototype) {
        String eventKey = pithyPrototype.getEventKey();

        if (null == eventKey || map.containsKey(eventKey)) {
            return false;
        } else {
            map.put(eventKey,pithyPrototype);
            return true;
        }
    }

    protected boolean removeEvent(PithyPrototype pithyPrototype) {
        if (null != pithyPrototype && null != pithyPrototype.getEventKey()){
            if (map.containsKey(pithyPrototype.getEventKey())) {
                map.remove(pithyPrototype.getEventKey());

                return true;
            }
        }

        return false;
    }

    protected boolean removeEvent(String TAG, String key) {
        if (null == TAG || null == key) {
            return false;
        }

        if (map.containsKey(PithyPrototype.generateEventKey(TAG, key))) {
            map.remove(PithyPrototype.generateEventKey(TAG, key));

            return true;
        }

        return false;

    }

    protected boolean removeAllTagEvent(String TAG) {
        if (null == TAG) {
            return false;
        }

        for (Object eventKey : map.keySet()) {
            String key = (String) eventKey;

            if (key.contains(TAG + PithyPrototype.PITHY_TAG)) {
                map.remove(key);
            }

        }

        return true;
    }

    protected boolean isRegister(String TAG, String key) {
        if (null != TAG && null != key) {
            return map.containsKey(PithyPrototype.generateEventKey(TAG, key));
        } else {
            LLog.e(this.TAG,"isRegister(), TAG or key is null!");

            return false;
        }
    }

    protected void removeAllEvent() {
        map.clear();
    }

    protected String[] dumpAllEvent() {
        String strList[] = new String[map.size()];
        int num = 0;
        for (Object eventKey : map.keySet()) {
            String key = (String) eventKey;
            strList[num] = key;
            num++;
        }

        return strList;
    }

    protected String[] dumpAllTagEvent(String TAG) {
        if (null == TAG) {
            return null;
        }

        ArrayList list = new ArrayList();

        for (Object eventKey : map.keySet()) {
            String key = (String) eventKey;

            if (key.contains(TAG + PithyPrototype.PITHY_TAG)) {
                list.add(key);
            }

        }

        return (String[]) list.toArray();
    }

    protected void post(String TAG, String key, String jsonData, PithyCallBack callBack) {
        LLog.d(this.TAG,"post , TAG : " + TAG + ", key : " + key);

        String eventKey = PithyPrototype.generateEventKey(TAG,key);

        if (null != map && map.containsKey(eventKey)) {
            LLog.d(this.TAG,"post() , eventKey : " + eventKey);

            PithyPrototype pithyPrototype = (PithyPrototype) map.get(eventKey);

            if (null != handler) {
                handler.post(()-> pithyPrototype.getCallback().pithyEventCallback(eventKey, jsonData, callBack));
            } else {
                LLog.e(this.TAG,"post() , handler is null!");
            }
        } else {
            LLog.e(this.TAG,"post() , eventKey does not exist!");
        }
    }

    public String getPithyThreadName() {
        return "MAIN_THREAD";
    }

    protected void init(){
        LLog.d(this.TAG,"init()");

        mbIsInit = true;
    }

    public boolean isInit() {
        return mbIsInit;
    }

    protected void registerSubscription(PithySubscriptionPrototype prototype){}

    protected void unRegisterSubscription(PithySubscriptionPrototype prototype){}

    protected void publish(String key ,Object obj){}
}
