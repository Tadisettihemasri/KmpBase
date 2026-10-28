package com.a4b.kmpbase.core.network.di

import com.a4b.kmpbase.core.common.log.AppLogger
import com.a4b.kmpbase.core.network.AuthTokenProvider
import com.a4b.kmpbase.core.network.NetworkJson
import com.a4b.kmpbase.core.network.createHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

/** Provides the platform [io.ktor.client.engine.HttpClientEngine] (OkHttp on Android, Darwin on iOS). */
internal expect val platformNetworkModule: Module

/** Definitions only — the HttpClient is built on the first API call, not at startup. */
val networkModule: Module = module {
    includes(platformNetworkModule)
    single<Json> { NetworkJson }
    single<AuthTokenProvider> { AuthTokenProvider.None }
    single<HttpClient> {
        AppLogger.lazyInit("HttpClient")
        createHttpClient(engine = get(), config = get(), tokenProvider = get(), json = get())
    }
}
