package com.plcoding.bookpedia.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

object HttpClientFactory {
    fun create(engine: HttpClientEngine):HttpClient{
        return HttpClient(engine){
            install(ContentNegotiation){
                json(
                    json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout){
                requestTimeoutMillis = 10_000L
                socketTimeoutMillis = 10_000L
            }
            install(Logging){
                logger = object :Logger{
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = io.ktor.client.plugins.logging.LogLevel.ALL
            }
        }
    }
}