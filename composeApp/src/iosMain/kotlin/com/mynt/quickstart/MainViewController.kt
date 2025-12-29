package com.mynt.quickstart

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.mynt.quickstart.db.AppDatabase
import com.mynt.quickstart.db.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController {
    App()
}
