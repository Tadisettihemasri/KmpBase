package com.a4b.kmpbase.core.common.di

import com.a4b.kmpbase.core.common.config.AppConfig
import com.a4b.kmpbase.core.common.dispatcher.AppDispatchers
import com.a4b.kmpbase.core.common.dispatcher.DefaultAppDispatchers
import com.a4b.kmpbase.core.common.telemetry.AnalyticsTracker
import com.a4b.kmpbase.core.common.telemetry.CrashReporter
import com.a4b.kmpbase.core.common.telemetry.NoOpAnalyticsTracker
import com.a4b.kmpbase.core.common.telemetry.NoOpCrashReporter
import com.a4b.kmpbase.core.common.telemetry.NoOpPushTokenProvider
import com.a4b.kmpbase.core.common.telemetry.NoOpRemoteConfig
import com.a4b.kmpbase.core.common.telemetry.PushTokenProvider
import com.a4b.kmpbase.core.common.telemetry.RemoteConfig
import org.koin.dsl.module

/** Telemetry defaults to no-ops; a platform module loaded later overrides them (e.g. Firebase on Android). */
fun commonModule(config: AppConfig) = module {
    single { config }
    single<AppDispatchers> { DefaultAppDispatchers }
    single<CrashReporter> { NoOpCrashReporter }
    single<AnalyticsTracker> { NoOpAnalyticsTracker }
    single<RemoteConfig> { NoOpRemoteConfig }
    single<PushTokenProvider> { NoOpPushTokenProvider }
}
