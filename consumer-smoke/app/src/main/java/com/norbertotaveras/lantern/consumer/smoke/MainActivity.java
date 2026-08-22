package com.norbertotaveras.lantern.consumer.smoke;

import android.app.Activity;
import android.os.Bundle;
import com.norbertotaveras.lantern.analytics.AnalyticsEvent;
import com.norbertotaveras.lantern.analytics.firebase.FirebaseAnalyticsProvider;
import com.norbertotaveras.lantern.appversioning.AppVersion;
import com.norbertotaveras.lantern.auth.core.AuthSession;
import com.norbertotaveras.lantern.auth.firebase.FirebaseAuthProvider;
import com.norbertotaveras.lantern.auth.firebasegoogle.FirebaseGoogleAuthProvider;
import com.norbertotaveras.lantern.auth.google.CredentialManagerGoogleAuthProvider;
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest;
import com.norbertotaveras.lantern.core.Environment;
import com.norbertotaveras.lantern.core.SdkConfig;
import com.norbertotaveras.lantern.deeplinks.DeepLink;
import com.norbertotaveras.lantern.featureflags.FeatureFlag;
import com.norbertotaveras.lantern.logging.AndroidSdkLogger;
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest;
import com.norbertotaveras.lantern.network.okhttp.OkHttpNetworkClientFactory;
import com.norbertotaveras.lantern.notifications.NotificationPayload;
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingTokenProvider;
import com.norbertotaveras.lantern.permissions.AndroidPermissionManager;
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults;
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigProvider;
import com.norbertotaveras.lantern.securestorage.DataStoreSecureKeyValueStore;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<?>[] sdkTypes = new Class<?>[] {
            AnalyticsEvent.class,
            FirebaseAnalyticsProvider.class,
            AppVersion.class,
            AuthSession.class,
            FirebaseAuthProvider.class,
            FirebaseGoogleAuthProvider.class,
            CredentialManagerGoogleAuthProvider.class,
            BackgroundWorkRequest.class,
            DeepLink.class,
            FeatureFlag.class,
            AndroidSdkLogger.class,
            MediaPickRequest.class,
            OkHttpNetworkClientFactory.class,
            NotificationPayload.class,
            FirebaseMessagingTokenProvider.class,
            AndroidPermissionManager.class,
            RemoteConfigDefaults.class,
            FirebaseRemoteConfigProvider.class,
            DataStoreSecureKeyValueStore.class
        };
        if (sdkTypes.length == 0) {
            throw new IllegalStateException("SDK smoke references are missing.");
        }

        new SdkConfig(Environment.Development, true);
    }
}
