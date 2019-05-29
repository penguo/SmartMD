package com.penguodev.smartmd

import android.app.Application
import android.content.Context
import timber.log.Timber

class MDApplication : Application() {

    companion object {
        lateinit var appContext: Context

        const val RC_VIEWER_TO_EDITOR = 1001
        const val RC_LIST_TO_EDITOR = 1002
        const val RC_PICK_IMAGE = 1003
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
    }
}