Change Log
==========

Version 2.0.0 *(2016/12/01)*
----------------------------
* Internal refactor/rewrite to create a more stable, testable architecture
* API changes:
    * `RxAppState` is now called `RxAppStateMonitor`
    * `AppStateMonitor` is the new interface returned by factory methods
    * Minor renamings (e.g. `startMonitoring()` / `stopMonitoring()` to `start()` / `stop()`)
* **Behaviour change:** A `FOREGROUND` event is now fired on initialisation (#1)

Version 1.1.0 *(2016/06/23)*
----------------------------
* Handle BACKGROUND / FOREGROUND events when toggling screen on/off (#3)

Version 1.0.1 *(2016/05/02)*
----------------------------
* Bugfix: Missing default app state (#2)

Version 1.0.0 *(2016/03/06)*
----------------------------
* Initial release