package com.a4b.kmpbase.android.telemetry

import android.content.Context
import android.os.Bundle
import com.a4b.kmpbase.core.common.log.AppLogger
import com.a4b.kmpbase.core.common.telemetry.AnalyticsTracker
import com.a4b.kmpbase.core.common.telemetry.CrashReporter
import com.a4b.kmpbase.core.common.telemetry.PushTokenProvider
import com.a4b.kmpbase.core.common.telemetry.RemoteConfig
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class FirebaseCrashReporter : CrashReporter {
    private val crashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun recordException(throwable: Throwable) = crashlytics.recordException(throwable)
    override fun log(message: String) = crashlytics.log(message)
    override fun setUserId(id: String?) = crashlytics.setUserId(id.orEmpty())
}

internal class FirebaseAnalyticsTracker(private val context: Context) : AnalyticsTracker {
    private val analytics by lazy {
        AppLogger.lazyInit("FirebaseAnalytics")
        FirebaseAnalytics.getInstance(context)
    }

    override fun setCollectionEnabled(enabled: Boolean) = analytics.setAnalyticsCollectionEnabled(enabled)

    override fun logEvent(name: String, params: Map<String, Any?>) {
        val bundle = Bundle()
        params.forEach { (key, value) ->
            when (value) {
                null -> Unit
                is String -> bundle.putString(key, value)
                is Int -> bundle.putLong(key, value.toLong())
                is Long -> bundle.putLong(key, value)
                is Float -> bundle.putDouble(key, value.toDouble())
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putString(key, value.toString())
                else -> bundle.putString(key, value.toString())
            }
        }
        analytics.logEvent(name, bundle)
    }

    override fun setUserProperty(name: String, value: String?) = analytics.setUserProperty(name, value)
}

internal class FirebaseRemoteConfigSource : RemoteConfig {
    private val remoteConfig by lazy {
        AppLogger.lazyInit("FirebaseRemoteConfig")
        FirebaseRemoteConfig.getInstance()
    }

    override suspend fun fetchAndActivate(): Boolean = remoteConfig.fetchAndActivate().awaitOrNull() ?: false
    override fun getString(key: String, default: String): String =
        remoteConfig.getString(key).ifEmpty { default }
    override fun getBoolean(key: String, default: Boolean): Boolean =
        if (remoteConfig.getKeysByPrefix(key).contains(key)) remoteConfig.getBoolean(key) else default
    override fun getLong(key: String, default: Long): Long =
        if (remoteConfig.getKeysByPrefix(key).contains(key)) remoteConfig.getLong(key) else default
}

internal class FirebasePushTokenProvider : PushTokenProvider {
    override suspend fun token(): String? {
        val messaging = FirebaseMessaging.getInstance()
        messaging.isAutoInitEnabled = true
        return messaging.token.awaitOrNull()
    }
}

private suspend fun <T> Task<T>.awaitOrNull(): T? = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task -> cont.resume(if (task.isSuccessful) task.result else null) }
}
