package com.a4b.kmpbase.android

import android.app.Application
import com.a4b.kmpbase.android.telemetry.telemetryModule
import com.a4b.kmpbase.di.appConfig
import com.a4b.kmpbase.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class KmpBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Registration only — no dependency is constructed here.
        initKoin(
            config = appConfig(isDebug = BuildConfig.DEBUG),
            platformModules = listOf(androidAppModule, telemetryModule()),
        ) {
            androidContext(this@KmpBaseApplication)
            if (BuildConfig.DEBUG) androidLogger(Level.INFO)
        }
    }
}
