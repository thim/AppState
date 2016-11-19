package com.jenzz.appstate.internal.rx;

import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.RxAppState;

import rx.Emitter;
import rx.functions.Action1;
import rx.functions.Cancellable;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

public final class AppStateEmitter implements Action1<Emitter<AppState>> {

  private final RxAppState appState;

  public AppStateEmitter(@NonNull RxAppState appState) {
    this.appState = appState;
  }

  @Override
  public void call(final Emitter<AppState> appStateEmitter) {
    final AppStateListener appStateListener = new AppStateListener() {
      @Override
      public void onAppDidEnterForeground() {
        appStateEmitter.onNext(FOREGROUND);
      }

      @Override
      public void onAppDidEnterBackground() {
        appStateEmitter.onNext(BACKGROUND);
      }
    };

    appStateEmitter.setCancellation(new Cancellable() {
      @Override
      public void cancel() throws Exception {
        appState.removeListener(appStateListener);
      }
    });

    appState.addListener(appStateListener);
  }
}
