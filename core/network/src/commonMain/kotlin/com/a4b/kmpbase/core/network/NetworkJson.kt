package com.a4b.kmpbase.core.network

import kotlinx.serialization.json.Json

/** Lenient JSON shared by the HTTP client and anything that persists API models. */
val NetworkJson: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
    encodeDefaults = true
    coerceInputValues = true
}
