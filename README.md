RxAppState [![Build Status](https://travis-ci.org/jenzz/RxAppState.svg?branch=master)](https://travis-ci.org/jenzz/RxAppState)
==========
A simple, reactive Android library based on [RxJava](https://github.com/ReactiveX/RxJava) that monitors app state changes.  
It notifies subscribers every time the app goes into background and comes back into foreground.

A typical use case is, for example, session tracking for analytics purposes
or suppressing push notifications when the app is currently visible to the user.

Background
----------
Android has this ancient pain of not providing any type of callback to know if your app is currently in the foreground or background.
It is lacking an equivalent of the iOS [UIApplicationDelegate](https://developer.apple.com/library/ios/documentation/UIKit/Reference/UIApplicationDelegate_Protocol)
which offers callbacks like [`applicationDidEnterBackground`](https://developer.apple.com/library/ios/documentation/UIKit/Reference/UIApplicationDelegate_Protocol/#//apple_ref/occ/intfm/UIApplicationDelegate/applicationDidEnterBackground:)
and [`applicationDidBecomeActive`](https://developer.apple.com/library/ios/documentation/UIKit/Reference/UIApplicationDelegate_Protocol/#//apple_ref/occ/intfm/UIApplicationDelegate/applicationDidBecomeActive:).

There are two popular discussions on this topic on StackOverflow:

* [How to detect when an Android app goes to the background and come back to the foreground](http://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo)
* [Checking if an Android application is running in the background](http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background)

This library internally uses a combination of `ActivityLifecycleCallbacks` and the `onTrimMemory(int level)` callback to identify the current app state.  
Just check out the source code (mainly: [AppStateRecognizer](https://github.com/jenzz/RxAppState/blob/master/library/src/main/java/com/jenzz/appstate/internal/AppStateRecognizer.java)).
The implementation is dead simple.

Usage
-----
You most probably want to monitor for app state changes in your application's `onCreate()` method
in which case you also don't need to worry about unsubscribing from the `Observable`.
Remember that if you subscribe in an `Activity` or `Fragment` don't forget to unsubscribe to avoid memory leaks.
```java
RxAppState.monitor(this).subscribe(new Action1<AppState>() {
    @Override
    public void call(AppState appState) {
        switch (appState) {
            case FOREGROUND:
                // Hocus Pocus...
                break;
            case BACKGROUND:
                // Abracadabra!
                break;
        }
    }
});
```

If you haven't jumped onto the hip RX bandwagon yet, you can also register oldskool callback listeners:
```java
RxAppState appState = RxAppState.create(this);
appState.addListener(new AppStateListener() {
    @Override
    public void onAppDidEnterForeground() {
        // ...
    }

    @Override
    public void onAppDidEnterBackground() {
        // ...
    }
});
appState.startMonitoring();
```

Example
-------
Check out the [sample project](https://github.com/jenzz/RxAppState/tree/master/sample) for an example implementation.

Download
--------
This library is published via [JitPack](https://jitpack.io/#jenzz/RxAppState).

**Step 1.** Add the JitPack repository to your project root `build.gradle`:

```groovy
allprojects {
  repositories {
	...
	maven { url "https://jitpack.io" }
  }
}
```

**Step 2.** Add the dependency to your app `build.gradle`:

```groovy
dependencies {
  compile 'com.jenzz:RxAppState:1.0.1'
}
```

License
-------
This project is licensed under the [MIT License](https://raw.githubusercontent.com/jenzz/RxAppState/master/LICENSE).