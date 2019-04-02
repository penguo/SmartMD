package com.penguodev.smartmd

import android.app.Application
import timber.log.Timber

class MDApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}