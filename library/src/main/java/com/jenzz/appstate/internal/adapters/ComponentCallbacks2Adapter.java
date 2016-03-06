package com.jenzz.appstate.internal.adapters;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

public abstract class ComponentCallbacks2Adapter implements ComponentCallbacks2 {

  //@formatter:off
  @Override public void onTrimMemory(int level) {}
  @Override public void onConfigurationChanged(Configuration newConfig) {}
  @Override public void onLowMemory() {}
  //@formatter:on
}
