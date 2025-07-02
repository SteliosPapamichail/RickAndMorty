package com.steliospapamichail.rickandmorty

import android.app.Application
import com.steliospapamichail.rickandmorty.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}