package com.jenzz.appstate;

/**
 * Listener for app state changes.
 */
public interface AppStateListener {

  /**
   * Called whenever the app comes into foreground.
   */
  void onAppDidEnterForeground();

  /**
   * Called whenever the app goes into background.
   */
  void onAppDidEnterBackground();
}