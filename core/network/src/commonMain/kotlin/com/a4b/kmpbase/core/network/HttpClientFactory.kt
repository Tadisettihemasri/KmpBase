package com.a4b.kmpbase.core.network

import co.touchlab.kermit.Logger as KermitLogger
import com.a4b.kmpbase.core.common.config.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val REQUEST_TIMEOUT_MS = 30_000L
private const val CONNECT_TIMEOUT_MS = 15_000L

fun createHttpClient(
    engine: HttpClientEngine,
    config: AppConfig,
    tokenProvider: AuthTokenProvider = AuthTokenProvider.None,
    json: Json = NetworkJson,
): HttpClient = HttpClient(engine) {
    // Non-2xx responses throw, so safeApiCall can map them to AppError.
    expectSuccess = true

    install(ContentNegotiation) { json(json) }

    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MS
        connectTimeoutMillis = CONNECT_TIMEOUT_MS
        socketTimeoutMillis = REQUEST_TIMEOUT_MS
    }

    install(Auth) {
        bearer {
            loadTokens { tokenProvider.accessToken()?.let { BearerTokens(accessToken = it, refreshToken = null) } }
            sendWithoutRequest { true }
        }
    }

    if (config.isDebug) {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) = KermitLogger.withTag("Http").d { message }
            }
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
    }

    defaultRequest {
        url(config.baseUrl)
        contentType(ContentType.Application.Json)
    }
}
