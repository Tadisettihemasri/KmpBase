package com.a4b.kmpbase.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.a4b.kmpbase.config.BuildKonfig
import com.a4b.kmpbase.core.designsystem.component.EmptyView

/** Proves theme + Koin + navigation wiring. Replace with the first real feature module. */
@Composable
fun HomePlaceholderScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { padding ->
        EmptyView(
            title = "KmpBase is running",
            subtitle = "Environment: ${BuildKonfig.ENVIRONMENT}\nAdd your first feature under features/",
            modifier = Modifier.padding(padding),
        )
    }
}
