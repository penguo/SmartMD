package com.penguodev.smartmd

import android.app.Application
import android.content.Context
import timber.log.Timber

class MDApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
    }
}