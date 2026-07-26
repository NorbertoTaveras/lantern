package com.norbertotaveras.mobilefoundation.logging

class NoOpSdkLogger : SdkLogger {
    override fun debug(message: String) = Unit
    override fun info(message: String) = Unit
    override fun warning(message: String, throwable: Throwable?) = Unit
    override fun error(message: String, throwable: Throwable?) = Unit
}