package com.a4b.kmpbase.android

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android-only bindings. Every `Interceptor` here is picked up by core:network's OkHttp engine. */
internal val androidAppModule = module {
    // Real inspector in debug, pass-through no-op in release (see build.gradle.kts).
    single<Interceptor> { ChuckerInterceptor.Builder(androidContext()).build() }
}
