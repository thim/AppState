package com.jenzz.appstate.sample;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.RxAppState;

import rx.functions.Action1;

import static android.widget.Toast.LENGTH_LONG;
import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

public class SampleApplication extends Application {

  private static final String TAG = "RxAppState";

  private RxAppState appState;

  @Override
  public void onCreate() {
    super.onCreate();

    // RX sample
    RxAppState.monitor(this).subscribe(new Action1<AppState>() {
      @Override
      public void call(AppState appState) {
        // Hocus, Pocus, Abracadabra!
      }
    });

    // Callback sample
    appState = RxAppState.create(this)
        .addListener(new SampleAppStateListener())
        .startMonitoring();
  }

  public RxAppState getAppState() {
    return appState;
  }

  private class SampleAppStateListener implements AppStateListener {

    @Override
    public void onAppDidEnterForeground() {
      logAndToast(FOREGROUND);
    }

    @Override
    public void onAppDidEnterBackground() {
      logAndToast(BACKGROUND);
    }
  }

  private void logAndToast(AppState appState) {
    Log.d(TAG, appState.toString());
    Toast.makeText(SampleApplication.this, TAG + ": " + appState, LENGTH_LONG).show();
  }
}