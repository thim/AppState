package com.jenzz.appstate;

/**
 * States of the application.
 */
public enum AppState {

  /**
   * The app is in foreground, i.e. visible to the user.
   */
  FOREGROUND,

  /**
   * The app is in background, i.e. not visible to the user.
   */
  BACKGROUND
}