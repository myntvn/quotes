package com.mynt.quickstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mynt.quickstart.data.quote.QuoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class QuoteViewModel(
    repository: QuoteRepository
) : ViewModel() {
    val quotes: StateFlow<List<Quote>> = repository.getQuotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
