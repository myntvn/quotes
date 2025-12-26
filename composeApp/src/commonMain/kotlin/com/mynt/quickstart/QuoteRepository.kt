package com.mynt.quickstart

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mynt.quickstart.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class QuoteRepository(
    private val database: AppDatabase,
    private val api: QuoteApi
) {
    private val queries = database.appDatabaseQueries

    fun getQuotes(): Flow<List<Quote>> {
        return queries.getAllQuotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { Quote(it.id.toInt(), it.content, it.author, it.details) }
            }
            .onStart {
                refreshQuotes()
            }
    }

    suspend fun refreshQuotes() {
        try {
            val quotes = api.getAllQuotes()
            database.transaction {
                queries.deleteAllQuotes()
                quotes.forEach { quote ->
                    queries.insertQuote(
                        id = quote.id.toLong(),
                        content = quote.content,
                        author = quote.author,
                        details = quote.details
                    )
                }
            }
        } catch (e: Exception) {
            // Handle error or ignore if offline
            e.printStackTrace()
        }
    }

    suspend fun getQuote(id: Int): Quote? {
        val entity = queries.getQuoteById(id.toLong()).executeAsOneOrNull()
        return if (entity != null) {
            Quote(entity.id.toInt(), entity.content, entity.author, entity.details)
        } else {
            try {
                val quote = api.getQuote(id)
                queries.insertQuote(quote.id.toLong(), quote.content, quote.author, quote.details)
                quote
            } catch (e: Exception) {
                null
            }
        }
    }
}

