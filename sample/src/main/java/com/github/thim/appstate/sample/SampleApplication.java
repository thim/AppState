package com.github.thim.appstate.sample;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.github.thim.AppMonitor;

import static android.widget.Toast.LENGTH_LONG;

public class SampleApplication extends Application {

    private static final String TAG = "AppMonitor";

    private AppMonitor mMonitor;

    @Override
    public void onCreate() {
        super.onCreate();

        mMonitor = new AppMonitor(this);
        mMonitor.addListener(new MyAppState());
        mMonitor.start();
    }

    public AppMonitor getAppMonitor() {
        return mMonitor;
    }

    class MyAppState implements AppMonitor.AppStateListener {

        @Override
        public void onAppDidEnterForeground() {
            Log.d(TAG, "onAppDidEnterForeground()");
            Toast.makeText(SampleApplication.this, TAG + ": FOREGROUND", LENGTH_LONG).show();
        }

        @Override
        public void onAppDidEnterBackground() {
            Log.d(TAG, "onAppDidEnterBackground()");
            Toast.makeText(SampleApplication.this, TAG + ": BACKGROUND", LENGTH_LONG).show();
        }

        @Override
        public void onAppDidLaunch() {
            Log.d(TAG, "onAppDidLaunch()");
            Toast.makeText(SampleApplication.this, TAG + ": FOREGROUND | LAUNCH", LENGTH_LONG)
                    .show();
        }
    }
}