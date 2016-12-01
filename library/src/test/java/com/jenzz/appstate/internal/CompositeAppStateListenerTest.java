package com.jenzz.appstate.internal;

import android.support.annotation.NonNull;

import com.jenzz.appstate.AppStateListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CompositeAppStateListenerTest {

  @NonNull private final CompositeAppStateListener compositeListener = new CompositeAppStateListener();

  @Mock private AppStateListener listener1;
  @Mock private AppStateListener listener2;
  @Mock private AppStateListener listener3;

  @Before
  public void setUp () {
    initMocks(this);

    compositeListener.addListener(listener1);
    compositeListener.addListener(listener2);
    compositeListener.addListener(listener3);
  }

  @Test
  public void notifiesListenersOfForeground() {
    compositeListener.onAppDidEnterForeground();

    verify(listener1).onAppDidEnterForeground();
    verify(listener2).onAppDidEnterForeground();
    verify(listener3).onAppDidEnterForeground();
  }

  @Test
  public void notifiesListenersOfBackground() {
    compositeListener.onAppDidEnterBackground();

    verify(listener1).onAppDidEnterBackground();
    verify(listener2).onAppDidEnterBackground();
    verify(listener3).onAppDidEnterBackground();
  }
}