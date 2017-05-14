React Native 中文网
http://reactnative.cn/

How to understand the “ReactContext” in “Sending events to Javascript” in React Native
http://stackoverflow.com/questions/34348519/how-to-understand-the-reactcontext-in-sending-events-to-javascript-in-react

============
npm start

adb reverse tcp:8081 tcp:8081
echo fs.inotify.max_user_watches=582222 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p

react-native run-android
--------

问题记录：
1.Caused by: java.lang.IllegalAccessError: Method 'void android.support.v4.net.ConnectivityManagerCompat.<init>()' is inaccessible to class 'com.facebook.react.modules.netinfo.NetInfoModule' (declaration of 'com.facebook.react.modules.netinfo.NetInfoModule' appears in /data/app/package.name-2/base.apk)
at com.facebook.react.modules.netinfo.NetInfoModule.<init>(NetInfoModule.java:55)
解决：
 maven {
            `url "$rootDir/../node_modules/react-native/android"`//地址是否正确
        }
 修改url "$rootDir*/..*/node_modules/react-native/android"为url "$rootDir/node_modules/react-native/android"

 http://www.jianshu.com/p/22aa14664cf9?open_source=weibo_search
 http://blog.csdn.net/android_ls/article/details/51803312

 2. java.lang.RuntimeException: com.facebook.react.devsupport.JSException: Could not get BatchedBridge, make sure your bundle is packaged correctly
 解决：
 "scripts": {
     "test": "react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output app/src/main/assets/index.android.bundle --sourcemap-output app/src/main/assets/index.android.map --assets-dest android/app/src/main/res/",
     "start": "node node_modules/react-native/local-cli/cli.js start",
   }
  运行 npm test

参考方法三：http://www.jianshu.com/p/22aa14664cf9?open_source=weibo_search

3. Execution failed for task ':app:installDebug'.
 > com.android.builder.testing.api.DeviceException: Could not create ADB Bridge. ADB location: /home/lucher/main/softs/android/sdk/platform-tools/adb
adb 路径不对

http://blog.csdn.net/haibo_bear/article/details/52984646
http://www.jianshu.com/p/22aa14664cf9?open_source=weibo_search

4. java.lang.UnsupportedClassVersionError: com/android/build/gradle/AppPlugin : Unsupported major.minor
很显然是class版本不支持。经查询，Android Studio2.2必须使用JDK8及以上版本，而且是强制的

http://blog.csdn.net/android_machong/article/details/53405467


5. java.lang.UnsupportedClassVersionError: com/android/build/gradle/AppPlugin : Unsupported major.minor version 52.0
jdk版本为1.7，需要改为1.8及以上

6.
 ERROR  watch /home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/babel-preset-react-native/node_modules/babel-plugin-check-es2015-constants/node_modules/babel-runtime/node_modules/core-js/library/fn/object ENOSPC
{"code":"ENOSPC","errno":"ENOSPC","syscall":"watch /home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/babel-preset-react-native/node_modules/babel-plugin-check-es2015-constants/node_modules/babel-runtime/node_modules/core-js/library/fn/object","filename":"/home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/babel-preset-react-native/node_modules/babel-plugin-check-es2015-constants/node_modules/babel-runtime/node_modules/core-js/library/fn/object"}
Error: watch /home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/babel-preset-react-native/node_modules/babel-plugin-check-es2015-constants/node_modules/babel-runtime/node_modules/core-js/library/fn/object ENOSPC
    at exports._errnoException (util.js:1022:11)
    at FSWatcher.start (fs.js:1429:19)
    at Object.fs.watch (fs.js:1456:11)
    at NodeWatcher.watchdir (/home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/react-native/node_modules/jest-haste-map/node_modules/sane/src/node_watcher.js:148:20)
    at Walker.<anonymous> (/home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/react-native/node_modules/jest-haste-map/node_modules/sane/src/node_watcher.js:361:12)
    at emitTwo (events.js:106:13)
    at Walker.emit (events.js:191:7)
    at /home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/react-native/node_modules/jest-haste-map/node_modules/sane/node_modules/walker/lib/walker.js:69:16
    at go$readdir$cb (/home/lucher/main/workspace/Workspace_Studio/helloworld/node_modules/react-native/node_modules/graceful-fs/graceful-fs.js:149:14)
    at FSReqWrap.oncomplete (fs.js:123:15)姐姐剞劂
解决：
echo fs.inotify.max_user_watches=582222 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p

http://stackoverflow.com/questions/34662574/node-js-getting-error-nodemon-internal-watch-failed-watch-enospc


7.错误日志如下：
04-13 16:05:48.489: E/ReactNativeJS(494): Can't find variable: PlateRecognizer
  04-13 16:05:48.509: E/AndroidRuntime(494): FATAL EXCEPTION: mqt_native_modules
  04-13 16:05:48.509: E/AndroidRuntime(494): Process: com.aiseminar.platerecognizer, PID: 494
  04-13 16:05:48.509: E/AndroidRuntime(494): com.facebook.react.common.JavascriptException: Can't find variable: PlateRecognizer, stack:
  04-13 16:05:48.509: E/AndroidRuntime(494): <unknown>@12:728
  04-13 16:05:48.509: E/AndroidRuntime(494): run@346:506
  04-13 16:05:48.509: E/AndroidRuntime(494): runApplication@346:1842
  04-13 16:05:48.509: E/AndroidRuntime(494): value@49:2897
  04-13 16:05:48.509: E/AndroidRuntime(494): <unknown>@49:1013
  04-13 16:05:48.509: E/AndroidRuntime(494): <unknown>@49:106
  04-13 16:05:48.509: E/AndroidRuntime(494): value@49:985
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.modules.core.ExceptionsManagerModule.showOrThrowError(ExceptionsManagerModule.java:99)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.modules.core.ExceptionsManagerModule.reportFatalException(ExceptionsManagerModule.java:83)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at java.lang.reflect.Method.invoke(Native Method)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.bridge.BaseJavaModule$JavaMethod.invoke(BaseJavaModule.java:345)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.cxxbridge.JavaModuleWrapper.invoke(JavaModuleWrapper.java:141)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.bridge.queue.NativeRunnable.run(Native Method)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at android.os.Handler.handleCallback(Handler.java:739)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at android.os.Handler.dispatchMessage(Handler.java:95)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.bridge.queue.MessageQueueThreadHandler.dispatchMessage(MessageQueueThreadHandler.java:31)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at android.os.Looper.loop(Looper.java:148)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at com.facebook.react.bridge.queue.MessageQueueThreadImpl$3.run(MessageQueueThreadImpl.java:196)
  04-13 16:05:48.509: E/AndroidRuntime(494): 	at java.lang.Thread.run(Thread.java:818)
解决：
react-native降级到0.40.0
react@~15.4.0-rc.4
npm install react-native@0.40.0

dependencies {
   ......
    compile "com.facebook.react:react-native:0.40.0" // From node_modules.
}

  "dependencies": {
    "react": "~15.4.0",
    "react-native": "~0.40.0"
  },
  
https://github.com/facebook/react-native/issues/13039


其他办法：
Run cd android && ./gradlew clean && ./gradlew assembleRelease
Run adb install app/build/outputs/apk/app-release.apk
https://github.com/facebook/react-native/issues/10196



http://www.tuicool.com/articles/meUZjaR