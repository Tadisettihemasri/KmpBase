package com.a4b.kmpbase.core.common.telemetry

/**
 * Platform-neutral telemetry contracts. Shared code depends only on these; the Android app binds
 * Firebase-backed implementations when google-services.json is present, otherwise the no-ops below.
 */
interface CrashReporter {
    fun recordException(throwable: Throwable)
    fun log(message: String)
    fun setUserId(id: String?)
}

interface AnalyticsTracker {
    fun setCollectionEnabled(enabled: Boolean)
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
    fun setUserProperty(name: String, value: String?)
}

interface RemoteConfig {
    /** Fetch + activate. Call after the first frame, never during startup. */
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String, default: String): String
    fun getBoolean(key: String, default: Boolean): Boolean
    fun getLong(key: String, default: Long): Long
}

interface PushTokenProvider {
    /** Enables push auto-init and returns the token. Call only after notification permission is granted. */
    suspend fun token(): String?
}

object NoOpCrashReporter : CrashReporter {
    override fun recordException(throwable: Throwable) = Unit
    override fun log(message: String) = Unit
    override fun setUserId(id: String?) = Unit
}

object NoOpAnalyticsTracker : AnalyticsTracker {
    override fun setCollectionEnabled(enabled: Boolean) = Unit
    override fun logEvent(name: String, params: Map<String, Any?>) = Unit
    override fun setUserProperty(name: String, value: String?) = Unit
}

object NoOpRemoteConfig : RemoteConfig {
    override suspend fun fetchAndActivate(): Boolean = false
    override fun getString(key: String, default: String): String = default
    override fun getBoolean(key: String, default: Boolean): Boolean = default
    override fun getLong(key: String, default: Long): Long = default
}

object NoOpPushTokenProvider : PushTokenProvider {
    override suspend fun token(): String? = null
}
