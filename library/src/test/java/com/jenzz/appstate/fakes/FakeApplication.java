package com.jenzz.appstate.fakes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

@SuppressLint("Registered")
public class FakeApplication extends Application {

  @NonNull private final List<ActivityLifecycleCallbacks> activityLifecycleCallbacks = new ArrayList<>();
  @NonNull private final List<ComponentCallbacks2> componentCallbacks = new ArrayList<>();
  @NonNull private final List<BroadcastReceiver> receivers = new ArrayList<>();

  @Override
  public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
    activityLifecycleCallbacks.add(callback);
  }

  @Override
  public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
    activityLifecycleCallbacks.remove(callback);
  }

  @Override
  public void registerComponentCallbacks(ComponentCallbacks callback) {
    if (callback instanceof ComponentCallbacks2) {
      componentCallbacks.add((ComponentCallbacks2) callback);
    }
  }

  @Override
  public void unregisterComponentCallbacks(ComponentCallbacks callback) {
    if (callback instanceof ComponentCallbacks2) {
      componentCallbacks.remove(callback);
    }
  }

  @Override
  public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
    receivers.add(receiver);
    return null;
  }

  @Override
  public void unregisterReceiver(BroadcastReceiver receiver) {
    receivers.remove(receiver);
  }

  public void notifyActivityLifecycleCallbacks(@NonNull Action1<ActivityLifecycleCallbacks> action) {
    for (ActivityLifecycleCallbacks callback : activityLifecycleCallbacks) {
      action.call(callback);
    }
  }

  public void notifyTrimMemory(int level) {
    for (ComponentCallbacks2 componentCallback : componentCallbacks) {
      componentCallback.onTrimMemory(level);
    }
  }

  public void notifyReceivers(@NonNull Intent intent) {
    for (BroadcastReceiver receiver : receivers) {
      receiver.onReceive(this, intent);
    }
  }
}
