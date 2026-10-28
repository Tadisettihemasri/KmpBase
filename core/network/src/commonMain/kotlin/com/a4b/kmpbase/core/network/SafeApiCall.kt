package com.a4b.kmpbase.core.network

import com.a4b.kmpbase.core.common.result.AppError
import com.a4b.kmpbase.core.common.result.AppResult
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Runs an API call and maps every failure to [AppError]. Never throws except for cancellation.
 *
 * ```
 * suspend fun fetchUser(id: String) = safeApiCall { client.get("users/$id").body<UserDto>() }
 * ```
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: ResponseException) {
    val status = e.response.status
    if (status == HttpStatusCode.Unauthorized) {
        AppResult.Failure(AppError.Unauthorized)
    } else {
        AppResult.Failure(AppError.Http(status.value, runCatching { e.response.bodyAsText() }.getOrNull()))
    }
} catch (_: HttpRequestTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (_: ConnectTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (_: SocketTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (e: ContentConvertException) {
    AppResult.Failure(AppError.Serialization(e))
} catch (e: SerializationException) {
    AppResult.Failure(AppError.Serialization(e))
} catch (_: IOException) {
    AppResult.Failure(AppError.Network)
} catch (e: Exception) {
    AppResult.Failure(AppError.Unknown(e))
}
