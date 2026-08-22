package com.norbertotaveras.lantern.network.okhttp

import com.norbertotaveras.lantern.logging.SdkLogger
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Logs request and response metadata without logging bodies.
 *
 * Use [NetworkLoggingLevel.Headers] only when header-level diagnostics are needed; sensitive
 * header names are redacted before being sent to [logger].
 */
class NetworkLoggingInterceptor(
    private val logger: SdkLogger,
    private val level: NetworkLoggingLevel = NetworkLoggingLevel.Basic,
    private val redactedHeaders: Set<String> = DEFAULT_REDACTED_HEADERS
) : Interceptor {

    init {
        require(redactedHeaders.none { it.isBlank() }) {
            "redactedHeaders cannot contain blank header names."
        }
        require(redactedHeaders.all { it.isValidHeaderName() }) {
            "redactedHeaders can only contain valid HTTP header names."
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (level == NetworkLoggingLevel.None) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()
        logger.info("--> ${request.method} ${request.url}")
        logHeaders(request.headers)

        val startedAtNanos = System.nanoTime()
        return try {
            val response = chain.proceed(request)
            val tookMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNanos)
            logger.info("<-- ${response.code} ${response.message} ${response.request.url} (${tookMillis}ms)")
            logHeaders(response.headers)
            response
        } catch (exception: IOException) {
            val tookMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNanos)
            logger.warning("<-- HTTP FAILED ${request.url} (${tookMillis}ms)", exception)
            throw exception
        }
    }

    private fun logHeaders(headers: okhttp3.Headers) {
        if (level != NetworkLoggingLevel.Headers) {
            return
        }

        headers.forEach { (name, value) ->
            val safeValue = if (name.isRedacted()) REDACTED_VALUE else value
            logger.debug("$name: $safeValue")
        }
    }

    private fun String.isRedacted(): Boolean {
        return redactedHeaders.any { redactedHeader ->
            equals(redactedHeader, ignoreCase = true)
        }
    }

    private fun String.isValidHeaderName(): Boolean {
        return headerNamePattern.matches(this)
    }

    companion object {
        const val REDACTED_VALUE = "[redacted]"

        private val DEFAULT_REDACTED_HEADERS: Set<String> = setOf(
            "Authorization",
            "Cookie",
            "Proxy-Authorization",
            "Set-Cookie",
            "X-Api-Key"
        ).mapTo(mutableSetOf()) { it.lowercase(Locale.US) }

        private val headerNamePattern = Regex("^[!#$%&'*+.^_`|~0-9A-Za-z-]+$")
    }
}
