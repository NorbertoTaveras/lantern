package com.norbertotaveras.mobilefoundation.deeplinks

import android.content.Intent
import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Resolves Android intents into parsed deep links.
 */
interface DeepLinkIntentResolver {
    /**
     * Parses a deep link from [intent].
     */
    fun resolve(intent: Intent): SdkResult<DeepLink>
}
