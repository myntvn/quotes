package com.mynt.quickstart

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mynt.quickstart.di.appModule
import com.mynt.quickstart.di.platformModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule, platformModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "QuickStart",
        ) {
            App()
        }
    }
}