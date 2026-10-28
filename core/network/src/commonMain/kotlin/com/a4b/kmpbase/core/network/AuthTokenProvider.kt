package com.a4b.kmpbase.core.network

/** Supplies the bearer token for API calls. Bind a real implementation once the app has auth. */
fun interface AuthTokenProvider {
    suspend fun accessToken(): String?

    companion object {
        val None = AuthTokenProvider { null }
    }
}
