package com.easypr.rn.rtpackage;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.easypr.rn.module.MyMapIntentModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lucher on 17-4-14.
 */

public class MyReactPackage implements ReactPackage {

    private MyMapIntentModule module;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        module = new MyMapIntentModule(reactContext);
        modules.add(module);
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    public MyMapIntentModule getMyMapIntentModule() {
        return module;
    }
}