package com.watch_dex.di

import android.app.Application

class MainApplication : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}
