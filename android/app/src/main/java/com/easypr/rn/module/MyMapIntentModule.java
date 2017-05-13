package com.easypr.rn.module;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by lucher on 17-4-14.
 */

public class MyMapIntentModule extends ReactContextBaseJavaModule {

    public static final String REACTCLASSNAME = "MyMapIntentModule";
    private Context mContext;

    public static final String LOCAL_ACTION = "com.helloworld.module.ACTION_SEND";

    public MyMapIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(mContext);
        lbm.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("msg");
                send(getReactApplicationContext(), msg);
            }
        }, new IntentFilter(LOCAL_ACTION));
    }

    @Override
    public String getName() {
        return REACTCLASSNAME;
    }

    /**
     * js页面跳转到activity 并传数据
     *
     * @param name
     */
    @ReactMethod
    public void startActivityByClassname(String name) {
        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
                Class aimActivity = Class.forName(name);
                Intent intent = new Intent(currentActivity, aimActivity);
                currentActivity.startActivity(intent);
            }
            send(getReactApplicationContext(), "原生页面打开成功!");
        } catch (Exception e) {

            throw new JSApplicationIllegalArgumentException("无法打开activity页面: " + e.getMessage());
        }
    }


    public void send(ReactContext reactContext, String content) {
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putString("content", content);
        sendEvent(reactContext, "notify", writableMap);
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}