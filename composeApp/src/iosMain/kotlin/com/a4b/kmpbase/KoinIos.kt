package com.a4b.kmpbase

import com.a4b.kmpbase.di.appConfig
import com.a4b.kmpbase.di.initKoin
import kotlin.experimental.ExperimentalNativeApi

/** Called once from iOSApp.init(). */
@OptIn(ExperimentalNativeApi::class)
@Suppress("unused")
fun startKoinIos() {
    initKoin(config = appConfig(isDebug = Platform.isDebugBinary))
}
