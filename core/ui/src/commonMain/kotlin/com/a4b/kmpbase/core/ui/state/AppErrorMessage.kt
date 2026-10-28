package com.a4b.kmpbase.core.ui.state

import com.a4b.kmpbase.core.common.result.AppError

/** Default user-facing copy. Move to Compose resources (strings.xml) when the app is localised. */
fun AppError.userMessage(): String = when (this) {
    AppError.Network -> "No internet connection. Check your network and try again."
    AppError.Timeout -> "This is taking longer than usual. Please try again."
    AppError.Unauthorized -> "Your session has expired. Please log in again."
    is AppError.Http -> if (code >= 500) "Something went wrong on our side. Please try again." else "Request failed ($code)."
    is AppError.Serialization, is AppError.Unknown -> "Something went wrong. Please try again."
}
