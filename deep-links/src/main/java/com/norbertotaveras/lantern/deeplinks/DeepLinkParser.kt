package com.norbertotaveras.lantern.deeplinks

import com.norbertotaveras.lantern.core.SdkResult

/**
 * Parses URI strings into SDK deep-link models.
 */
interface DeepLinkParser {
    /**
     * Parses [value] into a [DeepLink].
     */
    fun parse(value: String): SdkResult<DeepLink>
}
