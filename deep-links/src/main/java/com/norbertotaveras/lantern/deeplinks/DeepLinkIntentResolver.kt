package com.norbertotaveras.lantern.deeplinks

import android.content.Intent
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Resolves Android intents into parsed deep links.
 */
interface DeepLinkIntentResolver {
    /**
     * Parses a deep link from [intent].
     */
    fun resolve(intent: Intent): SdkResult<DeepLink>
}
