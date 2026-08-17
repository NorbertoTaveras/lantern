package com.norbertotaveras.mobilefoundation.deeplinks

import android.content.Intent
import com.norbertotaveras.mobilefoundation.core.SdkResult

interface DeepLinkIntentResolver {
    fun resolve(intent: Intent): SdkResult<DeepLink>
}
