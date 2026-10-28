package com.a4b.kmpbase.core.network.di

import com.a4b.kmpbase.core.common.log.AppLogger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Interceptor
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Every `single<Interceptor>` bound in Koin is added to OkHttp — the app module uses this
 * to plug in Chucker (debug) without core:network knowing about it.
 */
internal actual val platformNetworkModule: Module = module {
    single<HttpClientEngine> {
        AppLogger.lazyInit("OkHttp engine")
        val interceptors = getAll<Interceptor>()
        OkHttp.create { interceptors.forEach(::addInterceptor) }
    }
}
