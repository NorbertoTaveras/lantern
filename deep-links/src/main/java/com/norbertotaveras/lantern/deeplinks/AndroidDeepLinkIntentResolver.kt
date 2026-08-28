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

package com.norbertotaveras.lantern.deeplinks

import android.content.Intent
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Resolves Android [Intent] data URIs into SDK deep-link models.
 */
class AndroidDeepLinkIntentResolver(
    private val parser: DeepLinkParser = DefaultDeepLinkParser()
) : DeepLinkIntentResolver {
    /**
     * Parses `intent.dataString` into a [DeepLink].
     */
    override fun resolve(intent: Intent): SdkResult<DeepLink> {
        val value = intent.dataString
        if (value.isNullOrBlank()) {
            return SdkResult.Failure(
                SdkError(
                    code = DeepLinkErrorCodes.MISSING_INTENT_URI,
                    message = "Intent does not contain a deep link URI."
                )
            )
        }

        return parser.parse(value)
    }
}
