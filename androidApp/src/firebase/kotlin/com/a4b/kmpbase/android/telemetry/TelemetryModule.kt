package com.a4b.kmpbase.android.telemetry

import com.a4b.kmpbase.core.common.telemetry.AnalyticsTracker
import com.a4b.kmpbase.core.common.telemetry.CrashReporter
import com.a4b.kmpbase.core.common.telemetry.PushTokenProvider
import com.a4b.kmpbase.core.common.telemetry.RemoteConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Overrides the no-op telemetry from core:common. Each Firebase SDK instance is created on first use. */
internal fun telemetryModule() = module {
    single<CrashReporter> { FirebaseCrashReporter() }
    single<AnalyticsTracker> { FirebaseAnalyticsTracker(androidContext()) }
    single<RemoteConfig> { FirebaseRemoteConfigSource() }
    single<PushTokenProvider> { FirebasePushTokenProvider() }
}
