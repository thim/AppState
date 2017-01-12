package com.jenzz.appstate.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.jenzz.appstate.AppStateListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY)
class CompositeAppStateListener implements AppStateListener {

  @NonNull private final List<AppStateListener> listeners = new CopyOnWriteArrayList<>();

  @Override
  public void onAppDidEnterForeground() {
    for (AppStateListener listener : listeners) {
      listener.onAppDidEnterForeground();
    }
  }

  @Override
  public void onAppDidEnterBackground() {
    for (AppStateListener listener : listeners) {
      listener.onAppDidEnterBackground();
    }
  }

  void addListener(@NonNull AppStateListener listener) {
    listeners.add(listener);
  }

  void removeListener(@NonNull AppStateListener listener) {
    listeners.remove(listener);
  }
}
