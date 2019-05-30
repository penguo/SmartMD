package com.penguodev.smartmd.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.penguodev.smartmd.MDApplication


object PrefManager {
    private val defaultPref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(MDApplication.appContext)
    }

    fun saveTemporary(id: Long?, text: String?) {
        temporarySavedId = id
        temporarySavedText = text
    }

    var temporarySavedId: Long?
        set(value) {
            defaultPref.edit().putLong("temporarySaveId", value ?: -1).apply()
        }
        get() {
            return defaultPref.getLong("temporarySaveId", -1).let {
                if (it != -1L) it
                else null
            }
        }

    var temporarySavedText: String?
        set(value) {
            defaultPref.edit().putString("temporarySaveText", value).apply()
        }
        get() {
            return defaultPref.getString("temporarySaveText", null)
        }
}