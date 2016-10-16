/*
 * Copyright (C) 2016 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.reactivenetwork.library.network.observing.strategy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.network.observing.NetworkObservingStrategy;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;

/**
 * Network observing strategy for Android devices before Lollipop (API 20 or lower)
 */
public class PreLollipopNetworkObservingStrategy implements NetworkObservingStrategy {

  private BroadcastReceiver receiver;

  @Override public Observable<Connectivity> observeNetworkConnectivity(final Context context) {
    final IntentFilter filter = new IntentFilter();
    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

    return Observable.create(new ObservableOnSubscribe<Connectivity>() {
      @Override public void subscribe(final ObservableEmitter<Connectivity> e) throws Exception {

        receiver = new BroadcastReceiver() {
          @Override public void onReceive(Context context, Intent intent) {
            e.onNext(Connectivity.create(context));
          }
        };

        context.registerReceiver(receiver, filter);
      }
    }).doOnDispose(new Action() {
      @Override public void run() throws Exception {
        if (receiver != null) {
          context.unregisterReceiver(receiver);
        }
      }
    }).defaultIfEmpty(Connectivity.create());
  }
}
