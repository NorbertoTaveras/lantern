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

package com.norbertotaveras.lantern.remoteconfig

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.remoteconfig.internal.RemoteConfigKeyValidator

/**
 * Validated key for remote config values.
 *
 * Prefer [from] for dynamic values so invalid keys are returned as [SdkResult.Failure].
 * [unsafe] is intended for trusted constants.
 */
@JvmInline
value class RemoteConfigKey private constructor(val value: String) {
    companion object {
        /**
         * Creates a [RemoteConfigKey] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<RemoteConfigKey> {
            return when (val result = RemoteConfigKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(RemoteConfigKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [RemoteConfigKey] without validation.
         */
        fun unsafe(value: String): RemoteConfigKey {
            return RemoteConfigKey(value)
        }
    }
}
