package com.jenzz.appstate.internal;

import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentCallbacks2;
import com.jenzz.appstate.dummies.DummyAppStateListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppStateRecognizerTest {

  @Mock Application mockApplication;

  private final AppStateRecognizer appStateRecognizer = new AppStateRecognizer();

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void registerCallbacks() {
    appStateRecognizer.start(mockApplication, new DummyAppStateListener());

    verify(mockApplication).registerActivityLifecycleCallbacks(any(ActivityLifecycleCallbacks.class));
    verify(mockApplication).registerComponentCallbacks(any(ComponentCallbacks2.class));
  }

  @Test
  public void unregistersCallbacks() {
    appStateRecognizer.stop(mockApplication);

    verify(mockApplication).unregisterActivityLifecycleCallbacks(any(ActivityLifecycleCallbacks.class));
    verify(mockApplication).unregisterComponentCallbacks(any(ComponentCallbacks2.class));
  }

  @Test
  public void returnsNullAppStateByDefault() {
    assertThat(appStateRecognizer.getAppState()).isNull();
  }
}
