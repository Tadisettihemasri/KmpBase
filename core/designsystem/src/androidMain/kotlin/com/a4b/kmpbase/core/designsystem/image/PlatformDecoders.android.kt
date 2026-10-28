package com.a4b.kmpbase.core.designsystem.image

import android.os.Build
import coil3.ComponentRegistry
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder

internal actual fun ComponentRegistry.Builder.addPlatformDecoders() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) add(AnimatedImageDecoder.Factory()) else add(GifDecoder.Factory())
}
