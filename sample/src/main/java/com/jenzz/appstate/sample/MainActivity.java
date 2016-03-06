package com.jenzz.appstate.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jenzz.appstate.AppState;
import com.jenzz.appstate.RxAppState;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

  private Subscription subscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    subscription = RxAppState.monitor(getApplication())
        .subscribe(new Action1<AppState>() {
          @Override
          public void call(AppState appState) {
            // Hocus, Pocus, Abracadabra!
          }
        });
  }

  @Override
  protected void onDestroy() {
    subscription.unsubscribe();
    super.onDestroy();
  }
}