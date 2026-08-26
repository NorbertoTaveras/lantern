package com.norbertotaveras.lantern.analytics.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.norbertotaveras.lantern.analytics.AnalyticsEvent
import com.norbertotaveras.lantern.analytics.AnalyticsProvider
import com.norbertotaveras.lantern.analytics.AnalyticsUserId
import com.norbertotaveras.lantern.analytics.AnalyticsUserProperty
import com.norbertotaveras.lantern.analytics.firebase.internal.FirebaseAnalyticsValueMapper
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.logging.NoOpSdkLogger
import com.norbertotaveras.lantern.logging.SdkLogger

/**
 * [AnalyticsProvider] implementation backed by Firebase Analytics.
 */
class FirebaseAnalyticsProvider(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val logger: SdkLogger = NoOpSdkLogger()
) : AnalyticsProvider {

    /**
     * Creates a Firebase Analytics provider from [context].
     */
    constructor(
        context: Context,
        logger: SdkLogger = NoOpSdkLogger()
    ) : this(
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext),
        logger = logger
    )

    override suspend fun track(event: AnalyticsEvent): SdkResult<Unit> {
        return try {
            firebaseAnalytics.logEvent(
                event.name.value,
                FirebaseAnalyticsValueMapper.toBundle(event.parameters)
            )
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to track Firebase Analytics event ${event.name.value}.", exception)
            SdkResult.Failure(FirebaseAnalyticsErrorMapper.trackFailure(exception))
        }
    }

    override suspend fun setUserId(userId: AnalyticsUserId?): SdkResult<Unit> {
        return try {
            firebaseAnalytics.setUserId(userId?.value)
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to set Firebase Analytics user ID.", exception)
            SdkResult.Failure(FirebaseAnalyticsErrorMapper.userIdFailure(exception))
        }
    }

    override suspend fun setUserProperty(property: AnalyticsUserProperty): SdkResult<Unit> {
        return try {
            firebaseAnalytics.setUserProperty(
                property.name.value,
                FirebaseAnalyticsValueMapper.toUserPropertyValue(property.value)
            )
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to set Firebase Analytics user property ${property.name.value}.", exception)
            SdkResult.Failure(FirebaseAnalyticsErrorMapper.userPropertyFailure(exception))
        }
    }

    override suspend fun reset(): SdkResult<Unit> {
        return try {
            firebaseAnalytics.resetAnalyticsData()
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to reset Firebase Analytics data.", exception)
            SdkResult.Failure(FirebaseAnalyticsErrorMapper.resetFailure(exception))
        }
    }
}
