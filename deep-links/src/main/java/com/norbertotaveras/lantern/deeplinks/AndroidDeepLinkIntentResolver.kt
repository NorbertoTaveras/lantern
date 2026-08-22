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
