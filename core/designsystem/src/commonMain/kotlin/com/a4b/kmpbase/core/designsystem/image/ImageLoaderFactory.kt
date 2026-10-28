package com.a4b.kmpbase.core.designsystem.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.a4b.kmpbase.core.common.log.AppLogger

private const val MEMORY_CACHE_PERCENT = 0.20

/**
 * Pass to `setSingletonImageLoaderFactory` at the app root. Coil only invokes the factory when the
 * first image is requested, so nothing image-related is created at startup.
 * Disk cache uses Coil's platform default (app cache dir / NSCachesDirectory).
 */
fun newImageLoader(context: PlatformContext, isDebug: Boolean): ImageLoader {
    AppLogger.lazyInit("ImageLoader")
    return ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())
            add(SvgDecoder.Factory())
            addPlatformDecoders()
        }
        .memoryCache { MemoryCache.Builder().maxSizePercent(context, MEMORY_CACHE_PERCENT).build() }
        .crossfade(true)
        .apply { if (isDebug) logger(DebugLogger()) }
        .build()
}

/** Android adds GIF/animated WebP; iOS has nothing extra. */
internal expect fun coil3.ComponentRegistry.Builder.addPlatformDecoders()
