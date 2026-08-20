package com.norbertotaveras.mobilefoundation.deeplinks

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Parses URI strings into SDK deep-link models.
 */
interface DeepLinkParser {
    /**
     * Parses [value] into a [DeepLink].
     */
    fun parse(value: String): SdkResult<DeepLink>
}
