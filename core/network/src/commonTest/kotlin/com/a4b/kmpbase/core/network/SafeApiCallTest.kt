package com.a4b.kmpbase.core.network

import com.a4b.kmpbase.core.common.config.AppConfig
import com.a4b.kmpbase.core.common.result.AppError
import com.a4b.kmpbase.core.common.result.AppResult
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SafeApiCallTest {

    @Serializable
    private data class UserDto(val id: Int, val name: String)

    private val config = AppConfig(baseUrl = "https://example.test/", environment = "test", isDebug = false)

    private fun clientRespondingWith(status: HttpStatusCode, body: String) = createHttpClient(
        engine = MockEngine { respond(body, status, headersOf(HttpHeaders.ContentType, "application/json")) },
        config = config,
    )

    @Test
    fun successBodyIsParsed() = runTest {
        val client = clientRespondingWith(HttpStatusCode.OK, """{"id":1,"name":"Asha","extra":"ignored"}""")
        val result = safeApiCall { client.get("users/1").body<UserDto>() }
        assertEquals(AppResult.Success(UserDto(1, "Asha")), result)
    }

    @Test
    fun unauthorizedMapsToUnauthorized() = runTest {
        val client = clientRespondingWith(HttpStatusCode.Unauthorized, "{}")
        val result = safeApiCall { client.get("users/1").body<UserDto>() }
        assertEquals(AppResult.Failure(AppError.Unauthorized), result)
    }

    @Test
    fun serverErrorMapsToHttpWithCode() = runTest {
        val client = clientRespondingWith(HttpStatusCode.InternalServerError, "boom")
        val result = safeApiCall { client.get("users/1").body<UserDto>() }
        val error = assertIs<AppResult.Failure>(result).error
        assertEquals(AppError.Http(500, "boom"), error)
    }

    @Test
    fun malformedBodyMapsToSerialization() = runTest {
        val client = clientRespondingWith(HttpStatusCode.OK, """{"id":"not-a-number"}""")
        val result = safeApiCall { client.get("users/1").body<UserDto>() }
        assertIs<AppError.Serialization>(assertIs<AppResult.Failure>(result).error)
    }
}
