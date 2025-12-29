package com.mynt.quickstart.di

import com.mynt.quickstart.QuoteApi
import com.mynt.quickstart.data.quote.QuoteRepository
import com.mynt.quickstart.QuoteViewModel
import com.mynt.quickstart.data.quote.QuoteRepositoryImpl
import com.mynt.quickstart.db.AppDatabase
import com.mynt.quickstart.db.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { QuoteApi() }
    single { get<DatabaseDriverFactory>().createDriver() }
    single { AppDatabase(get()) }
    single<QuoteRepository> { QuoteRepositoryImpl(get(), get()) }
    viewModelOf(::QuoteViewModel)
}

