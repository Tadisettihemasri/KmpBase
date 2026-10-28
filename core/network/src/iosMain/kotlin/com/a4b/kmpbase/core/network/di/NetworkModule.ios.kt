package com.a4b.kmpbase.core.network.di

import com.a4b.kmpbase.core.common.log.AppLogger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformNetworkModule: Module = module {
    single<HttpClientEngine> {
        AppLogger.lazyInit("Darwin engine")
        Darwin.create()
    }
}
