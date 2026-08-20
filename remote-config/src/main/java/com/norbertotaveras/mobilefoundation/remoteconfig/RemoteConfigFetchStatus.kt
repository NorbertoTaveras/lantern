package com.norbertotaveras.mobilefoundation.remoteconfig

/**
 * Provider-neutral result of a remote config fetch.
 */
enum class RemoteConfigFetchStatus {
    /**
     * New values were fetched successfully.
     */
    Success,
    /**
     * Fetch was throttled by the backing provider.
     */
    Throttled,
    /**
     * Fetch completed but did not produce new values.
     */
    NoChange
}
