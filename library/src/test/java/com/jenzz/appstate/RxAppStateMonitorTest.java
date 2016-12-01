package com.jenzz.appstate;

import android.support.annotation.NonNull;

import com.jenzz.appstate.fakes.FakeApplication;
import com.jenzz.appstate.stubs.StubAppStateRecognizer;

import org.junit.Test;

import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class RxAppStateMonitorTest {

  private static final Action1<AppStateListener> ACTION_FOREGROUND = new Action1<AppStateListener>() {
    @Override
    public void call(AppStateListener appStateListener) {
      appStateListener.onAppDidEnterForeground();
    }
  };
  private static final Action1<AppStateListener> ACTION_BACKGROUND = new Action1<AppStateListener>() {
    @Override
    public void call(AppStateListener appStateListener) {
      appStateListener.onAppDidEnterBackground();
    }
  };

  @NonNull private final StubAppStateRecognizer stubRecognizer = new StubAppStateRecognizer();
  @NonNull private final AppStateMonitor appStateMonitor = new RxAppStateMonitor(new FakeApplication(), stubRecognizer);

  @NonNull private final TestSubscriber<AppState> subscriber = TestSubscriber.create();

  @Test
  public void startsAndStopsMonitoring() {
    appStateMonitor.start();

    assertThat(stubRecognizer.isStarted()).isTrue();

    appStateMonitor.stop();

    assertThat(stubRecognizer.isStarted()).isFalse();
  }

  @Test
  public void addsAndRemovesListener() {
    AppStateListener listener = AppStateListener.NO_OP;

    appStateMonitor.addListener(listener);

    assertThat(stubRecognizer.getListeners()).contains(listener);

    appStateMonitor.removeListener(listener);

    assertThat(stubRecognizer.getListeners()).doesNotContain(listener);
  }

  @Test
  public void returnsForeground() {
    stubRecognizer.setAppState(FOREGROUND);

    assertThat(appStateMonitor.isAppInForeground()).isTrue();
    assertThat(appStateMonitor.isAppInBackground()).isFalse();
  }

  @Test
  public void returnsBackground() {
    stubRecognizer.setAppState(BACKGROUND);

    assertThat(appStateMonitor.isAppInForeground()).isFalse();
    assertThat(appStateMonitor.isAppInBackground()).isTrue();
  }

  @Test
  public void emitsForeground() {
    appStateMonitor.asObservable().subscribe(subscriber);

    stubRecognizer.notifyAppStateListener(ACTION_FOREGROUND);

    subscriber.assertValue(FOREGROUND);
    subscriber.assertNoTerminalEvent();
  }

  @Test
  public void emitsBackground() {
    appStateMonitor.asObservable().subscribe(subscriber);

    stubRecognizer.notifyAppStateListener(ACTION_BACKGROUND);

    subscriber.assertValue(BACKGROUND);
    subscriber.assertNoTerminalEvent();
  }
}