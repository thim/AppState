package com.jenzz.appstate.stubs;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.internal.AppStateRecognizer;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.jenzz.appstate.AppState.BACKGROUND;

public class StubAppStateRecognizer implements AppStateRecognizer {

  @NonNull private final List<AppStateListener> listeners = new ArrayList<>();

  @NonNull private AppState appState = BACKGROUND;
  private boolean isStarted;

  @Override
  public void addListener(@NonNull AppStateListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(@NonNull AppStateListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void start(@NonNull Application app) {
    isStarted = true;
  }

  @Override
  public void stop(@NonNull Application app) {
    isStarted = false;
  }

  @NonNull
  @Override
  public AppState getAppState() {
    return appState;
  }

  public void setAppState(@NonNull AppState appState) {
    this.appState = appState;
  }

  @NonNull
  public List<AppStateListener> getListeners() {
    return listeners;
  }

  public boolean isStarted() {
    return isStarted;
  }

  public void notifyAppStateListener(@NonNull Action1<AppStateListener> action) {
    for (AppStateListener listener : listeners) {
      action.call(listener);
    }
  }
}
