package com.norbertotaveras.mobilefoundation.consumer.smoke;

import android.app.Activity;
import android.os.Bundle;

import com.norbertotaveras.mobilefoundation.auth.core.AuthProviderType;
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession;
import com.norbertotaveras.mobilefoundation.core.Enviroment;
import com.norbertotaveras.mobilefoundation.core.SdkConfig;
import com.norbertotaveras.mobilefoundation.core.SdkResult;
import com.norbertotaveras.mobilefoundation.logging.NoOpSdkLogger;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SdkConfig config = new SdkConfig(Enviroment.Development, true);
        NoOpSdkLogger logger = new NoOpSdkLogger();
        AuthSession session = new AuthSession(
            "consumer-smoke",
            AuthProviderType.Anonymous,
            null,
            null
        );
        SdkResult.Success<String> result = new SdkResult.Success<>("ok");

        logger.info(config.getEnvironment().name());
        logger.debug(session.getProvider().name());
        logger.debug(result.getData());
    }
}
