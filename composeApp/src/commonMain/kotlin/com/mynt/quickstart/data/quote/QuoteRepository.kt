package com.mynt.quickstart.data.quote

import com.mynt.quickstart.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getQuotes(): Flow<List<Quote>>

    suspend fun refreshQuotes()

    suspend fun getQuote(id: Int): Quote?
}
