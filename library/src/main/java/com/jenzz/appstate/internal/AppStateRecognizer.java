package com.jenzz.appstate.internal;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentCallbacks2;
import android.support.annotation.NonNull;
import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.internal.adapters.ActivityLifecycleCallbacksAdapter;
import com.jenzz.appstate.internal.adapters.ComponentCallbacks2Adapter;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

public final class AppStateRecognizer {

  private final ActivityLifecycleCallbacks activityStartedCallback = new ActivityStartedCallback();
  private final ComponentCallbacks2 uiHiddenCallback = new UiHiddenCallback();

  private AppState appState = BACKGROUND;
  private AppStateListener appStateListener;
  private AtomicBoolean isFirstLaunch = new AtomicBoolean(true);

  public void start(@NonNull Application app, @NonNull AppStateListener appStateListener) {
    this.appStateListener = appStateListener;

    app.registerActivityLifecycleCallbacks(activityStartedCallback);
    app.registerComponentCallbacks(uiHiddenCallback);
  }

  public void stop(@NonNull Application app) {
    app.unregisterActivityLifecycleCallbacks(activityStartedCallback);
    app.unregisterComponentCallbacks(uiHiddenCallback);
  }

  @NonNull
  public AppState getAppState() {
    return appState;
  }

  private class ActivityStartedCallback extends ActivityLifecycleCallbacksAdapter {

    @Override
    public void onActivityStarted(Activity activity) {
      if (isFirstLaunch.compareAndSet(true, false)) {
        appState = FOREGROUND;
      }

      if (appState == BACKGROUND) {
        appState = FOREGROUND;
        appStateListener.onAppDidEnterForeground();
      }
    }
  }

  private class UiHiddenCallback extends ComponentCallbacks2Adapter {

    @Override
    public void onTrimMemory(int level) {
      if (level >= TRIM_MEMORY_UI_HIDDEN) {
        appState = BACKGROUND;
        appStateListener.onAppDidEnterBackground();
      }
    }
  }
}
