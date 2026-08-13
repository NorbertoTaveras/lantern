package com.norbertotaveras.mobilefoundation.deeplinks

import android.content.Intent
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult

class AndroidDeepLinkIntentResolver(
    private val parser: DeepLinkParser = DefaultDeepLinkParser()
) : DeepLinkIntentResolver {
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
