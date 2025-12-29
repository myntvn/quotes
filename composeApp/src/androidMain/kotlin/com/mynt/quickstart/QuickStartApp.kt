package com.mynt.quickstart

import android.app.Application
import com.mynt.quickstart.di.appModule
import com.mynt.quickstart.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuickStartApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuickStartApp)
            modules(appModule, platformModule)
        }
    }
}

