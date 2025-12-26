package com.mynt.quickstart

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class QuoteApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAllQuotes(): List<Quote> {
        return httpClient.get("$SERVER_URL/quotes").body()
    }

    suspend fun getQuote(id: Int): Quote {
        return httpClient.get("$SERVER_URL/quotes/$id").body()
    }
}

