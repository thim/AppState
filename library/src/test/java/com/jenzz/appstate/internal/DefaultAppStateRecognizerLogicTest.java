package com.jenzz.appstate.internal;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.fakes.FakeApplication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rx.functions.Action1;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultAppStateRecognizerLogicTest {

  private static final Action1<ActivityLifecycleCallbacks> ACTIVITY_STARTED =
          new Action1<ActivityLifecycleCallbacks>() {
            @Override
            public void call(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
              activityLifecycleCallbacks.onActivityStarted(new Activity());
            }
          };
  private static final Intent SCREEN_OFF = new Intent(ACTION_SCREEN_OFF);

  @NonNull private final DefaultAppStateRecognizer recognizer = new DefaultAppStateRecognizer();
  @NonNull private final FakeApplication fakeApplication = new FakeApplication();

  @Mock private AppStateListener mockListener;

  @Before
  public void setUp() {
    initMocks(this);
    recognizer.addListener(mockListener);
    recognizer.start(fakeApplication);
  }

  @Test
  public void returnsBackgroundByDefault() {
    assertThat(recognizer.getAppState()).isEqualTo(BACKGROUND);
  }

  @Test
  public void notifiesForegroundWhenActivityStarted() {
    givenAppState(BACKGROUND);

    fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);

    verify(mockListener).onAppDidEnterForeground();
  }

  @Test
  public void doesNotNotifyForegroundAgainWhenMultipleActivitiesStarted() {
    givenAppState(BACKGROUND);

    fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);
    fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);
    fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);

    verify(mockListener, times(1)).onAppDidEnterForeground();
  }

  @Test
  public void notifiesBackgroundWhenTrimMemoryUiHidden() {
    givenAppState(FOREGROUND);

    fakeApplication.notifyTrimMemory(TRIM_MEMORY_UI_HIDDEN);

    verify(mockListener).onAppDidEnterBackground();
  }

  @Test
  public void notifiesBackgroundWhenTrimMemoryGreaterThanUiHidden() {
    givenAppState(FOREGROUND);

    fakeApplication.notifyTrimMemory(TRIM_MEMORY_UI_HIDDEN + 10);

    verify(mockListener).onAppDidEnterBackground();
  }

  @Test
  public void doesNotNotifyBackgroundWhenTrimMemorySmallerThanUiHidden() {
    givenAppState(FOREGROUND);

    fakeApplication.notifyTrimMemory(TRIM_MEMORY_UI_HIDDEN - 10);

    verify(mockListener, never()).onAppDidEnterBackground();
  }

  @Test
  public void doesNotNotifyBackgroundWhenTrimMemoryUiHiddenButAppAlreadyInBackground() {
    givenAppState(BACKGROUND);

    fakeApplication.notifyTrimMemory(TRIM_MEMORY_UI_HIDDEN);

    verify(mockListener, never()).onAppDidEnterBackground();
  }

  @Test
  public void notifiesBackgroundWhenScreenTurnedOff() {
    givenAppState(FOREGROUND);

    fakeApplication.notifyReceivers(SCREEN_OFF);

    verify(mockListener).onAppDidEnterBackground();
  }

  @Test
  public void doesNotNotifyBackgroundWhenScreenTurnedOffButAppAlreadyInBackground() {
    givenAppState(BACKGROUND);

    fakeApplication.notifyReceivers(SCREEN_OFF);

    verify(mockListener, never()).onAppDidEnterBackground();
  }

  @Test
  public void removesListener() {
    givenAppState(BACKGROUND);

    recognizer.removeListener(mockListener);

    fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);

    verifyZeroInteractions(mockListener);
  }

  private void givenAppState(@NonNull AppState appState) {
    switch (appState) {
      case FOREGROUND:
        fakeApplication.notifyActivityLifecycleCallbacks(ACTIVITY_STARTED);
        break;
      case BACKGROUND:
        // no-op (default state)
        break;
    }
  }
}
