package com.a4b.kmpbase.core.network

import com.a4b.kmpbase.core.common.config.AppConfig
import com.a4b.kmpbase.core.common.di.commonModule
import com.a4b.kmpbase.core.network.di.networkModule
import io.ktor.client.HttpClient
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class NetworkModuleTest {

    private val config = AppConfig(baseUrl = "https://example.test/", environment = "test", isDebug = true)

    @Test
    fun httpClientResolvesFromKoinGraphAsSingleton() {
        val koin = koinApplication { modules(commonModule(config), networkModule) }.koin
        val client = koin.get<HttpClient>()
        assertNotNull(client)
        assertSame(client, koin.get<HttpClient>())
        client.close()
    }
}
