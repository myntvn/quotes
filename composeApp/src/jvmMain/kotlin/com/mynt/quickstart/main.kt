package com.mynt.quickstart

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mynt.quickstart.db.AppDatabase
import com.mynt.quickstart.db.DatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "QuickStart",
    ) {
        val api = remember { QuoteApi() }
        val database = remember { AppDatabase(DatabaseDriverFactory().createDriver()) }
        val repository = remember { QuoteRepository(database, api) }
        App(repository)
    }
}