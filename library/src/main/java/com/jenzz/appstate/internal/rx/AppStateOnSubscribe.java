package com.jenzz.appstate.internal.rx;

import android.support.annotation.NonNull;
import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.RxAppState;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

public final class AppStateOnSubscribe implements Observable.OnSubscribe<AppState> {

  private final RxAppState appState;

  public AppStateOnSubscribe(@NonNull RxAppState appState) {
    this.appState = appState;
  }

  @Override
  public void call(final Subscriber<? super AppState> subscriber) {
    final AppStateListener appStateListener = new AppStateListener() {
      @Override
      public void onAppDidEnterForeground() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(FOREGROUND);
        }
      }

      @Override
      public void onAppDidEnterBackground() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(BACKGROUND);
        }
      }
    };

    subscriber.add(Subscriptions.create(new Action0() {
      @Override
      public void call() {
        appState.removeListener(appStateListener);
      }
    }));

    appState.addListener(appStateListener);
  }
}