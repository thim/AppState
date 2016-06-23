package com.jenzz.appstate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN;
import static android.os.Build.VERSION_CODES.M;
import static com.jenzz.appstate.AppState.BACKGROUND;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = M)
public class RxAppStateTest {

  @Test
  public void emitsNothingOnStartup() {
    TestSubscriber<AppState> testSubscriber = new TestSubscriber<>();

    RxAppState.monitor(application).subscribe(testSubscriber);

    testSubscriber.assertNoValues();
    testSubscriber.assertNoTerminalEvent();
  }

  @Test
  public void emitsBackgroundIfTrimLevelUiHidden() {
    TestSubscriber<AppState> testSubscriber = new TestSubscriber<>();

    RxAppState.monitor(application).subscribe(testSubscriber);
    application.onTrimMemory(TRIM_MEMORY_UI_HIDDEN);

    testSubscriber.assertValue(BACKGROUND);
    testSubscriber.assertNoTerminalEvent();
  }

  @Test
  public void doesNotEmitBackgroundIfTrimLevelLessThanUiHidden() {
    TestSubscriber<AppState> testSubscriber = new TestSubscriber<>();

    RxAppState.monitor(application).subscribe(testSubscriber);
    application.onTrimMemory(TRIM_MEMORY_UI_HIDDEN - 10);

    testSubscriber.assertNoValues();
    testSubscriber.assertNoTerminalEvent();
  }

  @Test
  public void emitsBackgroundIfTrimLevelGreaterThanUiHidden() {
    TestSubscriber<AppState> testSubscriber = new TestSubscriber<>();

    RxAppState.monitor(application).subscribe(testSubscriber);
    application.onTrimMemory(TRIM_MEMORY_UI_HIDDEN + 10);

    testSubscriber.assertValue(BACKGROUND);
    testSubscriber.assertNoTerminalEvent();
  }
}