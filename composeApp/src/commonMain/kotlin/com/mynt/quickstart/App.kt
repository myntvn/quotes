package com.mynt.quickstart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }
        val api = remember { QuoteApi() }

        when (val screen = currentScreen) {
            is Screen.List -> {
                QuoteListScreen(
                    api = api,
                    onQuoteClick = { quote ->
                        currentScreen = Screen.Details(quote)
                    }
                )
            }
            is Screen.Details -> {
                QuoteDetailScreen(
                    quote = screen.quote,
                    onBack = {
                        currentScreen = Screen.List
                    }
                )
            }
        }
    }
}

sealed class Screen {
    object List : Screen()
    data class Details(val quote: Quote) : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteListScreen(
    api: QuoteApi,
    onQuoteClick: (Quote) -> Unit
) {
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            quotes = api.getAllQuotes()
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Famous Quotes") })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text("Error: $error", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(quotes) { quote ->
                        QuoteItem(quote, onClick = { onQuoteClick(quote) })
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteItem(quote: Quote, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = quote.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "- ${quote.author}", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteDetailScreen(
    quote: Quote,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quote Details") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(text = quote.content, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "- ${quote.author}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Details:", style = MaterialTheme.typography.titleSmall)
            Text(text = quote.details, style = MaterialTheme.typography.bodyMedium)
        }
    }
}