package com.jenzz.appstate.internal;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.internal.adapters.NoOpActivityLifecycleCallbacks;
import com.jenzz.appstate.internal.adapters.NoOpComponentCallbacks2;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.support.annotation.RestrictTo.Scope.GROUP_ID;
import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

@RestrictTo(GROUP_ID)
public final class AppStateRecognizer {

  @NonNull private final ActivityLifecycleCallbacks activityStartedCallback = new ActivityStartedCallback();
  @NonNull private final ComponentCallbacks2 uiHiddenCallback = new UiHiddenCallback();
  @NonNull private final BroadcastReceiver screenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
  @NonNull private final AtomicBoolean isFirstLaunch = new AtomicBoolean(true);

  @NonNull private AppState appState = BACKGROUND;
  @NonNull private AppStateListener appStateListener = AppStateListener.NO_OP;

  public void start(@NonNull Application app, @NonNull AppStateListener appStateListener) {
    this.appStateListener = appStateListener;

    app.registerActivityLifecycleCallbacks(activityStartedCallback);
    app.registerComponentCallbacks(uiHiddenCallback);
    app.registerReceiver(screenOffBroadcastReceiver, new IntentFilter(ACTION_SCREEN_OFF));
  }

  public void stop(@NonNull Application app) {
    app.unregisterActivityLifecycleCallbacks(activityStartedCallback);
    app.unregisterComponentCallbacks(uiHiddenCallback);
    app.unregisterReceiver(screenOffBroadcastReceiver);
  }

  @NonNull
  public AppState getAppState() {
    return appState;
  }

  private void onAppDidEnterForeground() {
    appState = FOREGROUND;
    appStateListener.onAppDidEnterForeground();
  }

  private void onAppDidEnterBackground() {
    appState = BACKGROUND;
    appStateListener.onAppDidEnterBackground();
  }

  private class ActivityStartedCallback extends NoOpActivityLifecycleCallbacks {

    @Override
    public void onActivityStarted(Activity activity) {
      if (isFirstLaunch.compareAndSet(true, false)) {
        onAppDidEnterForeground();
        return;
      }

      if (appState == BACKGROUND) {
        onAppDidEnterForeground();
      }
    }
  }

  private class UiHiddenCallback extends NoOpComponentCallbacks2 {

    @Override
    public void onTrimMemory(int level) {
      if (level >= TRIM_MEMORY_UI_HIDDEN) {
        onAppDidEnterBackground();
      }
    }
  }

  private class ScreenOffBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (appState == FOREGROUND) {
        onAppDidEnterBackground();
      }
    }
  }
}
