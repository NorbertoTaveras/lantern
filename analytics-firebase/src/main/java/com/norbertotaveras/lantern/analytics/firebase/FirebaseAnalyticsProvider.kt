/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.analytics.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.norbertotaveras.lantern.analytics.AnalyticsEvent
import com.norbertotaveras.lantern.analytics.AnalyticsProvider
import com.norbertotaveras.lantern.analytics.AnalyticsUserId
import com.norbertotaveras.lantern.analytics.AnalyticsUserProperty
import com.norbertotaveras.lantern.analytics.firebase.internal.FirebaseAnalyticsValueMapper
import com.norbertotaveras.lantern.core.SdkError
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
            SdkResult.Failure(trackFailure(exception))
        }
    }

    override suspend fun setUserId(userId: AnalyticsUserId?): SdkResult<Unit> {
        return try {
            firebaseAnalytics.setUserId(userId?.value)
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to set Firebase Analytics user ID.", exception)
            SdkResult.Failure(userIdFailure(exception))
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
            SdkResult.Failure(userPropertyFailure(exception))
        }
    }

    override suspend fun reset(): SdkResult<Unit> {
        return try {
            firebaseAnalytics.resetAnalyticsData()
            SdkResult.Success(Unit)
        } catch (exception: Throwable) {
            logger.error("Unable to reset Firebase Analytics data.", exception)
            SdkResult.Failure(resetFailure(exception))
        }
    }

    private fun trackFailure(cause: Throwable) = SdkError(
        code = FirebaseAnalyticsErrorCodes.TRACK_FAILED,
        message = "Unable to track Firebase Analytics event.",
        cause = cause
    )

    private fun userIdFailure(cause: Throwable) = SdkError(
        code = FirebaseAnalyticsErrorCodes.USER_ID_FAILED,
        message = "Unable to set Firebase Analytics user ID.",
        cause = cause
    )

    private fun userPropertyFailure(cause: Throwable) = SdkError(
        code = FirebaseAnalyticsErrorCodes.USER_PROPERTY_FAILED,
        message = "Unable to set Firebase Analytics user property.",
        cause = cause
    )

    private fun resetFailure(cause: Throwable) = SdkError(
        code = FirebaseAnalyticsErrorCodes.RESET_FAILED,
        message = "Unable to reset Firebase Analytics data.",
        cause = cause
    )
}
