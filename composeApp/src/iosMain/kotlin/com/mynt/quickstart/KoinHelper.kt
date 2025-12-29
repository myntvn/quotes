package com.mynt.quickstart

import com.mynt.quickstart.di.appModule
import com.mynt.quickstart.di.platformModule
import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(appModule, platformModule)
    }
}

