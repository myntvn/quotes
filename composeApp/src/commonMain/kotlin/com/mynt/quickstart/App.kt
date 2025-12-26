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
fun App(repository: QuoteRepository) {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }

        when (val screen = currentScreen) {
            is Screen.List -> {
                QuoteListScreen(
                    repository = repository,
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
    repository: QuoteRepository,
    onQuoteClick: (Quote) -> Unit
) {
    val quotes by repository.getQuotes().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Famous Quotes") })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn {
                items(quotes) { quote ->
                    QuoteItem(quote, onClick = { onQuoteClick(quote) })
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