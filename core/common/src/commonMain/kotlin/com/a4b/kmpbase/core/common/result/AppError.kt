package com.a4b.kmpbase.core.common.result

sealed interface AppError {
    /** No connectivity / socket / DNS failure. */
    data object Network : AppError

    data object Timeout : AppError

    data object Unauthorized : AppError

    /** Non-2xx response other than 401. */
    data class Http(val code: Int, val body: String?) : AppError

    /** Response body did not match the expected model. */
    data class Serialization(val cause: Throwable) : AppError

    data class Unknown(val cause: Throwable) : AppError
}
