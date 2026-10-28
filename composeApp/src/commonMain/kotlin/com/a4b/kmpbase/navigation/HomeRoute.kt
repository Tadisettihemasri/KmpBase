package com.a4b.kmpbase.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe routes for app-level screens. Feature modules declare their own `@Serializable` routes
 * plus a `NavGraphBuilder.xxxScreen()` extension that AppNavHost calls.
 */
@Serializable
data object HomeRoute
