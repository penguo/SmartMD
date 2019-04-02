package com.penguodev.smartmd.common

import android.content.Context
import android.widget.EditText
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

object SoftKeyManager {

    private fun getInputMethodManager(context: Context): InputMethodManager? {
        return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager).also {
            if (it == null) {
                Timber.w("Failed to getInputMethodManager")
            }
        }
    }

    fun show(view: EditText) {
        view.requestFocus()
        getInputMethodManager(view.context)?.showSoftInput(view, 0)
    }

    fun hide(view: EditText) {
        getInputMethodManager(view.context)?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}