package com.pr1n.repository.remote.base

import java.util.concurrent.TimeUnit

data class RequestConfig private constructor(
    val connectTimeout: Long,
    val readTimeout: Long,
    val writeTimeout: Long
) {
    companion object {
        const val DEFAULT_TIMEOUT = 60 * 1000L
    }

    class Builder : IConfigBuilder {
        private var connectTimeout: Long = DEFAULT_TIMEOUT
        private var readTimeout: Long = DEFAULT_TIMEOUT
        private var writeTimeout: Long = DEFAULT_TIMEOUT
        override fun connectTimeout(timeout: Long, unit: TimeUnit) { connectTimeout = timeout.convertToMilliSeconds(unit) }
        override fun readTimeout(timeout: Long, unit: TimeUnit) { readTimeout = timeout.convertToMilliSeconds(unit) }
        override fun writeTimeout(timeout: Long, unit: TimeUnit) { writeTimeout = timeout.convertToMilliSeconds(unit) }
        fun build() = RequestConfig(connectTimeout, readTimeout, writeTimeout)
        private fun Long.convertToMilliSeconds(unit: TimeUnit) = when (unit) {
            TimeUnit.NANOSECONDS -> unit.toMillis(this)
            TimeUnit.MICROSECONDS -> unit.toMillis(this)
            TimeUnit.MILLISECONDS -> this
            TimeUnit.SECONDS -> unit.toMillis(this)
            TimeUnit.MINUTES -> unit.toMillis(this)
            TimeUnit.HOURS -> unit.toMillis(this)
            TimeUnit.DAYS -> unit.toMillis(this)
        }
    }
}