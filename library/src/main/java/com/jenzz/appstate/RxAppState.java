package com.jenzz.appstate;

import android.app.Application;
import android.support.annotation.NonNull;
import com.jenzz.appstate.internal.rx.AppStateOnSubscribe;
import com.jenzz.appstate.internal.AppStateRecognizer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rx.Observable;

import static com.jenzz.appstate.AppState.BACKGROUND;
import static com.jenzz.appstate.AppState.FOREGROUND;

/**
 * An app state monitor that keeps track of whenever the application
 * goes into background and comes back into foreground.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public final class RxAppState {

  private final AppStateRecognizer appStateRecognizer = new AppStateRecognizer();
  private final AppStateListener internalAppStateListener = new InternalAppStateListener();
  private final List<AppStateListener> listeners = new CopyOnWriteArrayList<>();
  private final Application app;

  private Observable<AppState> observable;

  /**
   * Creates a new {@link RxAppState} instance for the given {@link Application}
   * and starts monitoring for app state changes.
   *
   * @return an {@link Observable} that emits {@link AppState} items
   * whenever the app goes into background and comes back into foreground.
   */
  public static Observable<AppState> monitor(@NonNull Application application) {
    return create(application).startMonitoring().asObservable();
  }

  /**
   * Creates a new {@link RxAppState} instance for the given {@link Application}.
   *
   * @return a new {@link RxAppState} instance
   */
  public static RxAppState create(@NonNull Application application) {
    return new RxAppState(application);
  }

  /**
   * Creates a new {@link RxAppState} instance for the given {@link Application}.
   */
  public RxAppState(@NonNull Application app) {
    this.app = app;
  }

  /**
   * Starts monitoring the app for background / foreground changes.
   *
   * @return the current {@link RxAppState} instance to allow method chaining
   */
  @NonNull
  public RxAppState startMonitoring() {
    appStateRecognizer.start(app, internalAppStateListener);
    return this;
  }

  /**
   * Stops monitoring the app for background / foreground changes.
   *
   * @return the current {@link RxAppState} instance to allow method chaining
   */
  @NonNull
  public RxAppState stopMonitoring() {
    appStateRecognizer.stop(app);
    return this;
  }

  /**
   * Adds a new {@link AppStateListener} to the app state monitor.
   *
   * @return the current {@link RxAppState} instance to allow method chaining
   */
  @NonNull
  public RxAppState addListener(@NonNull AppStateListener appStateListener) {
    listeners.add(appStateListener);
    return this;
  }

  /**
   * Removes the specified {@link AppStateListener} from the app state monitor.
   *
   * @return the current {@link RxAppState} instance to allow method chaining
   */
  @NonNull
  public RxAppState removeListener(@NonNull AppStateListener appStateListener) {
    listeners.remove(appStateListener);
    return this;
  }

  /**
   * Checks whether the app is currently in the foreground.
   *
   * @return {@code true} if the app is currently in the foreground, {@code false} otherwise
   */
  public boolean isAppInForeground() {
    return appStateRecognizer.getAppState() == FOREGROUND;
  }

  /**
   * Checks whether the app is currently in the background.
   *
   * @return {@code true} if the app is currently in the background, {@code false} otherwise
   */
  public boolean isAppInBackground() {
    return appStateRecognizer.getAppState() == BACKGROUND;
  }

  /**
   * Creates a new {@link Observable} that emits {@link AppState} items
   * whenever the app goes into background and comes back into foreground.
   *
   * @return a new {@link Observable}
   */
  @NonNull
  public Observable<AppState> asObservable() {
    if (observable == null) {
      observable = Observable.create(new AppStateOnSubscribe(this));
    }
    return observable;
  }

  private class InternalAppStateListener implements AppStateListener {

    @Override
    public void onAppDidEnterForeground() {
      for (AppStateListener listener : listeners) {
        listener.onAppDidEnterForeground();
      }
    }

    @Override
    public void onAppDidEnterBackground() {
      for (AppStateListener listener : listeners) {
        listener.onAppDidEnterBackground();
      }
    }
  }
}