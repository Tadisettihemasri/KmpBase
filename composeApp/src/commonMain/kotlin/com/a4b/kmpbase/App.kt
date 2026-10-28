package com.a4b.kmpbase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import coil3.compose.setSingletonImageLoaderFactory
import com.a4b.kmpbase.core.common.config.AppConfig
import com.a4b.kmpbase.core.common.dispatcher.AppDispatchers
import com.a4b.kmpbase.core.common.telemetry.RemoteConfig
import com.a4b.kmpbase.core.designsystem.image.newImageLoader
import com.a4b.kmpbase.core.designsystem.theme.AppTheme
import com.a4b.kmpbase.navigation.AppNavHost
import kotlinx.coroutines.withContext
import org.koin.compose.getKoin

@Composable
fun App() {
    val koin = getKoin()
    val config = remember(koin) { koin.get<AppConfig>() }

    // Factory runs on the first image request, not here.
    setSingletonImageLoaderFactory { context -> newImageLoader(context, config.isDebug) }

    // Deferred work: runs after the first composition is committed, off the main thread.
    LaunchedEffect(Unit) {
        withContext(koin.get<AppDispatchers>().io) {
            koin.get<RemoteConfig>().fetchAndActivate()
        }
    }

    AppTheme {
        AppNavHost()
    }
}
