package com.jenzz.appstate.internal;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;

import static android.support.annotation.RestrictTo.Scope.GROUP_ID;

@RestrictTo(GROUP_ID)
public interface AppStateRecognizer {

  void addListener(@NonNull AppStateListener listener);

  void removeListener(@NonNull AppStateListener listener);

  void start(@NonNull Application app);

  void stop(@NonNull Application app);

  @NonNull AppState getAppState();
}
