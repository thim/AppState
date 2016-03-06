package com.jenzz.appstate.sample;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.RxAppState;
import rx.functions.Action1;

import static android.widget.Toast.LENGTH_LONG;

public class SampleApplication extends Application {

  private static final String TAG = SampleApplication.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    // Reactive sample
    RxAppState.monitor(this).subscribe(new Action1<AppState>() {
      @Override
      public void call(AppState appState) {
        Log.d(TAG, "RxAppState: " + appState.name());
        Toast.makeText(SampleApplication.this, "RxAppState: " + appState.name(), LENGTH_LONG).show();
      }
    });

    // Callback sample
    RxAppState appState = RxAppState.create(this);
    appState.addListener(new AppStateListener() {
      @Override
      public void onAppDidEnterForeground() {
        Log.d(TAG, "RxAppState: onAppDidEnterForeground");
      }

      @Override
      public void onAppDidEnterBackground() {
        Log.d(TAG, "RxAppState: onAppDidEnterBackground");
      }
    });
    appState.startMonitoring();
  }
}