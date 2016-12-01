package com.jenzz.appstate.internal;

import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.IntentFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultAppStateRecognizerSetupTest {

  private final DefaultAppStateRecognizer recognizer = new DefaultAppStateRecognizer();

  @Mock private Application mockApplication;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void registersCallbacks() {
    recognizer.start(mockApplication);

    verify(mockApplication).registerActivityLifecycleCallbacks(any(ActivityLifecycleCallbacks.class));
    verify(mockApplication).registerComponentCallbacks(any(ComponentCallbacks2.class));
    verify(mockApplication).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
  }

  @Test
  public void unregistersCallbacks() {
    recognizer.stop(mockApplication);

    verify(mockApplication).unregisterActivityLifecycleCallbacks(any(ActivityLifecycleCallbacks.class));
    verify(mockApplication).unregisterComponentCallbacks(any(ComponentCallbacks2.class));
    verify(mockApplication).unregisterReceiver(any(BroadcastReceiver.class));
  }
}
