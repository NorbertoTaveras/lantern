package com.norbertotaveras.mobilefoundation.deeplinks

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface DeepLinkParser {
    fun parse(value: String): SdkResult<DeepLink>
}
