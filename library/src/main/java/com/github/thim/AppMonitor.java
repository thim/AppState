package com.github.thim;

import static android.content.Intent.ACTION_SCREEN_OFF;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Classe para monitorar a transição de estado entre background e foreground da aplicação.
 */
public class AppMonitor {

    /**
     * Estado da aplicação.
     */
    public enum AppState {
        /**
         * O aplicativo esta visível para o usuário.
         */
        FOREGROUND,
        /**
         * O aplicativo não esta visível para o usuário.
         */
        BACKGROUND
    }

    /** Variaveis */
    private final Application.ActivityLifecycleCallbacks mActivityStartedCallback = new ActivityStartedCallback();
    private final ComponentCallbacks2 mUiHiddenCallback = new UiHiddenCallback();
    private final BroadcastReceiver mScreenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
    private final AtomicBoolean mIsFirstLaunch = new AtomicBoolean(true);
    private final List<AppStateListener> mListeners = new CopyOnWriteArrayList<>();
    private final Application mApp;
    private AppState mAppState = AppState.BACKGROUND;

    /**
     * Contrutor.
     * 
     * @param application
     *            instância do aplicativo.
     */
    public AppMonitor(Application application) {
        this.mApp = application;
    }

    /**
     * Adiciona um listener ao componente.
     * 
     * @param listener
     *            listener.
     */
    public void addListener(AppStateListener listener) {
        mListeners.add(listener);
    }

    /**
     * Remove um listener ao componente.
     * 
     * @param listener
     *            listener.
     */
    public void removeListener(AppStateListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Inicia o monitor de estado.
     */
    public void start() {
        mApp.registerActivityLifecycleCallbacks(mActivityStartedCallback);
        mApp.registerComponentCallbacks(mUiHiddenCallback);
        mApp.registerReceiver(mScreenOffBroadcastReceiver, new IntentFilter(ACTION_SCREEN_OFF));
    }

    /**
     * Finaliza o monitor de estado.
     */
    public void stop() {
        mApp.unregisterActivityLifecycleCallbacks(mActivityStartedCallback);
        mApp.unregisterComponentCallbacks(mUiHiddenCallback);
        mApp.unregisterReceiver(mScreenOffBroadcastReceiver);
    }

    /**
     * Retorna se o aplicativo esta em foreground.
     * 
     * @return TRUE se estiver em foreground.
     */
    private boolean isAppInForeground() {
        return mAppState == AppState.FOREGROUND;
    }

    /**
     * Retorna se o aplicativo esta em background.
     * 
     * @return TRUE se estiver em background.
     */
    private boolean isAppInBackground() {
        return mAppState == AppState.BACKGROUND;
    }

    /**
     * O aplicativo esta indo para o estado de foreground.
     */
    private void onAppDidEnterForeground() {
        mAppState = AppState.FOREGROUND;
        for (AppStateListener listener : mListeners) {
            listener.onAppDidEnterForeground();
        }
    }

    /**
     * O aplicativo esta indo para o estado de background.
     */
    private void onAppDidEnterBackground() {
        mAppState = AppState.BACKGROUND;
        for (AppStateListener listener : mListeners) {
            listener.onAppDidEnterBackground();
        }
    }

    /**
     * O aplicativo esta iniciando e indo para o estado de foreground.
     */
    private void onAppDidLaunch() {
        mAppState = AppState.FOREGROUND;
        for (AppStateListener listener : mListeners) {
            listener.onAppDidLaunch();
        }
    }

    /**
     * Listener para as mudanças de estado.
     */
    public interface AppStateListener {
        /**
         * Chamado quando o aplicativo vai para o estado de foreground.
         */
        void onAppDidEnterForeground();

        /**
         * Chamado quando o aplicativo vai para o estado de background.
         */
        void onAppDidEnterBackground();

        /**
         * Chamado quando o aplicativo é lançado.
         */
        void onAppDidLaunch();
    }

    /**
     * Classe para implementar o monitoramento do ciclo de vida das activities.
     */
    private class ActivityStartedCallback implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityStarted(Activity activity) {
            if (mIsFirstLaunch.compareAndSet(true, false)) {
                onAppDidLaunch();
            } else if (isAppInBackground()) {
                onAppDidEnterForeground();
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }
    }

    /**
     * Classe para implementar o monitoramento da memória.
     */
    private class UiHiddenCallback implements ComponentCallbacks2 {
        @Override
        public void onTrimMemory(int level) {
            if (level >= TRIM_MEMORY_UI_HIDDEN && isAppInForeground()) {
                onAppDidEnterBackground();
            }
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }

        @Override
        public void onLowMemory() {
        }
    }

    /**
     * Classe para implementar o receiver do broadcast disparado quando a tela é desligada
     * (android.intent.action.SCREEN_OFF).
     */
    private class ScreenOffBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAppInForeground()) {
                onAppDidEnterBackground();
            }
        }
    }
}
