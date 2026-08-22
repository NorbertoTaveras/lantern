package com.norbertotaveras.mobilefoundation.consumer.smoke;

import android.app.Activity;
import android.os.Bundle;
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsEvent;
import com.norbertotaveras.mobilefoundation.analytics.firebase.FirebaseAnalyticsProvider;
import com.norbertotaveras.mobilefoundation.appversioning.AppVersion;
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession;
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthProvider;
import com.norbertotaveras.mobilefoundation.auth.firebasegoogle.FirebaseGoogleAuthProvider;
import com.norbertotaveras.mobilefoundation.auth.google.CredentialManagerGoogleAuthProvider;
import com.norbertotaveras.mobilefoundation.backgroundwork.BackgroundWorkRequest;
import com.norbertotaveras.mobilefoundation.core.Environment;
import com.norbertotaveras.mobilefoundation.core.SdkConfig;
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLink;
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlag;
import com.norbertotaveras.mobilefoundation.logging.AndroidSdkLogger;
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest;
import com.norbertotaveras.mobilefoundation.network.okhttp.OkHttpNetworkClientFactory;
import com.norbertotaveras.mobilefoundation.notifications.NotificationPayload;
import com.norbertotaveras.mobilefoundation.notifications.firebase.FirebaseMessagingTokenProvider;
import com.norbertotaveras.mobilefoundation.permissions.AndroidPermissionManager;
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults;
import com.norbertotaveras.mobilefoundation.remoteconfig.firebase.FirebaseRemoteConfigProvider;
import com.norbertotaveras.mobilefoundation.securestorage.DataStoreSecureKeyValueStore;

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
