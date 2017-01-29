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
package com.github.pwittchen.reactivenetwork.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reactivex.functions.Function;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConnectivityTest {

    @Test
    public void statusShouldBeEqualToGivenValue() throws Exception {
        // given
        final NetworkInfo.State givenState = NetworkInfo.State.CONNECTED;
        final int givenType = ConnectivityManager.TYPE_WIFI;
        final String givenTypeName = "WIFI";
        final Connectivity connectivity = Connectivity.create(givenState, givenType, givenTypeName);

        // when
        final Function<Connectivity, Boolean> equalTo = Connectivity.hasState(connectivity.getState());
        final Boolean shouldBeEqualToGivenStatus = equalTo.apply(connectivity);

        // then
        assertThat(shouldBeEqualToGivenStatus).isTrue();
    }

    @Test
    public void statusShouldBeEqualToOneOfGivenMultipleValues() throws Exception {
        // given
        final NetworkInfo.State givenState = NetworkInfo.State.CONNECTING;
        final int givenType = ConnectivityManager.TYPE_WIFI;
        final String givenTypeName = "WIFI";
        final Connectivity connectivity = Connectivity.create(givenState, givenType, givenTypeName);

        final NetworkInfo.State states[] =
                {NetworkInfo.State.CONNECTED, NetworkInfo.State.CONNECTING};

        // when
        final Function<Connectivity, Boolean> equalTo = Connectivity.hasState(states);
        final Boolean shouldBeEqualToGivenStatus = equalTo.apply(connectivity);

        // then
        assertThat(shouldBeEqualToGivenStatus).isTrue();
    }

    @Test
    public void typeShouldBeEqualToGivenValue() throws Exception {
        // given
        final NetworkInfo.State givenState = NetworkInfo.State.CONNECTED;
        final int givenType = ConnectivityManager.TYPE_WIFI;
        final String givenTypeName = "WIFI";
        final Connectivity connectivity = Connectivity.create(givenState, givenType, givenTypeName);

        // when
        final Function<Connectivity, Boolean> equalTo = Connectivity.hasType(connectivity.getType());
        final Boolean shouldBeEqualToGivenStatus = equalTo.apply(connectivity);

        // then
        assertThat(shouldBeEqualToGivenStatus).isTrue();
    }

    @Test
    public void typeShouldBeEqualToOneOfGivenMultipleValues() throws Exception {
        // given
        final NetworkInfo.State givenState = NetworkInfo.State.CONNECTING;
        final int givenType = ConnectivityManager.TYPE_MOBILE;
        final String givenTypeName = "MOBILE";
        final Connectivity connectivity = Connectivity.create(givenState, givenType, givenTypeName);

        final int givenTypes[] = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE};

        // when
        final Function<Connectivity, Boolean> equalTo = Connectivity.hasType(givenTypes);
        final Boolean shouldBeEqualToGivenStatus = equalTo.apply(connectivity);

        // then
        assertThat(shouldBeEqualToGivenStatus).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowAnExceptionWhenContextIsNull() {
        // given
        final Context context = null;

        // when
        Connectivity.create(context);

        // then
        // an exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowAnExceptionWhenStateIsNull() {
        // given
        final NetworkInfo.State state = null;
        final int type = 0;
        final String name = "name";

        // when
        Connectivity.create(state, type, name);

        // then
        // an exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowAnExceptionWhenNameIsNull() {
        // given
        final NetworkInfo.State state = NetworkInfo.State.CONNECTED;
        final int type = 0;
        final String name = null;

        // when
        Connectivity.create(state, type, name);

        // then
        // an exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowAnExceptionWhenNameIsEmpty() {
        // given
        final NetworkInfo.State state = NetworkInfo.State.CONNECTED;
        final int type = 0;
        final String name = "";

        // when
        Connectivity.create(state, type, name);

        // then
        // an exception is thrown
    }

    @Test
    public void shouldReturnProperToStringValue() {
        // given
        final NetworkInfo.State defaultState = NetworkInfo.State.DISCONNECTED;
        final int defaultType = -1;
        final String defaultName = "NONE";
        final String expectedToString = "Connectivity{"
                + "state="
                + defaultState
                + ", type="
                + defaultType
                + ", name='"
                + defaultName
                + '\''
                + '}';

        // when
        Connectivity connectivity = Connectivity.create();

        // then
        assertThat(connectivity.toString()).isEqualTo(expectedToString);
    }

    @Test
    public void shouldCreateDefaultConnectivity() {
        // given
        Connectivity connectivity;

        // when
        connectivity = Connectivity.create();

        // then
        assertThat(connectivity.isDefault()).isTrue();
    }

    @Test
    public void theSameConnectivityObjectsShouldBeEqual() {
        // given
        final Connectivity connectivityOne = Connectivity.create();
        final Connectivity connectivityTwo = Connectivity.create();

        // when
        boolean objectsAreEqual = connectivityOne.equals(connectivityTwo);

        // then
        assertThat(objectsAreEqual).isTrue();
    }

    @Test
    public void twoDefaultObjectsShouldBeInTheSameBucket() {
        // given
        final Connectivity connectivityOne = Connectivity.create();
        final Connectivity connectivityTwo = Connectivity.create();

        // when
        boolean hashCodesAreEqual = connectivityOne.hashCode() == connectivityTwo.hashCode();

        // then
        assertThat(hashCodesAreEqual).isTrue();
    }
}
