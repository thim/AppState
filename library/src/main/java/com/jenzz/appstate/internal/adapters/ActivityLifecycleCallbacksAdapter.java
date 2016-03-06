package com.jenzz.appstate.internal.adapters;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

public abstract class ActivityLifecycleCallbacksAdapter implements ActivityLifecycleCallbacks {

  //@formatter:off
  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
  @Override public void onActivityStarted(Activity activity) {}
  @Override public void onActivityResumed(Activity activity) {}
  @Override public void onActivityPaused(Activity activity) {}
  @Override public void onActivityStopped(Activity activity) {}
  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
  @Override public void onActivityDestroyed(Activity activity) {}
  //@formatter:on
}