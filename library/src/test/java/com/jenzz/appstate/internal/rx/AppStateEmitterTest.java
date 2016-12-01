package com.jenzz.appstate.internal.rx;

import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.internal.AppStateEmitter;
import com.jenzz.appstate.stubs.StubAppStateRecognizer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rx.Emitter;
import rx.functions.Action1;
import rx.functions.Cancellable;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppStateEmitterTest {

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

  @NonNull private final StubAppStateRecognizer recognizer = new StubAppStateRecognizer();
  @NonNull private final AppStateEmitter emitter = new AppStateEmitter(recognizer);

  @Mock private Emitter<AppState> mockEmitter;

  @Before
  public void setUp () {
    initMocks(this);

    emitter.call(mockEmitter);
  }

  @Test
  public void emitsForeground() {
    recognizer.notifyAppStateListener(ACTION_FOREGROUND);

    verify(mockEmitter).onNext(FOREGROUND);
  }

  @Test
  public void emitsBackground() {
    recognizer.notifyAppStateListener(ACTION_BACKGROUND);

    verify(mockEmitter).onNext(BACKGROUND);
  }

  @Test
  public void setsCancellation() {
    verify(mockEmitter).setCancellation(any(Cancellable.class));
  }
}