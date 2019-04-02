package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.text.*
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import com.penguodev.smartmd.common.ComfyUtil
import com.penguodev.smartmd.common.setVisibleGone
import com.penguodev.smartmd.common.ui.MDTextView
import timber.log.Timber


class EditorManager(
    lifecycleOwner: LifecycleOwner,
    private val startTV: MDTextView,
    private val editText: EditText,
    private val endTV: MDTextView
) {
    private val mdManager = MarkdownManager()

    private val cIndex = MutableLiveData<Int>().apply { value = 0 }
    private val list = mutableListOf<String>()

    init {
        editText.addTextChangedListener(MDTextWatcher())
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString() == "") {
                delete()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun enter() {
        val texts = editText.text.toString().split("\n")
        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                list[getCurrentIndex()] = s
            } else {
                list.add(getCurrentIndex(), s)
            }
        }
    }

    private fun delete() {
        setCurrentIndex(getCurrentIndex() - 1)
        list.removeAt(getCurrentIndex())
    }

    fun getCurrentIndex(): Int {
        return cIndex.value ?: 0
    }

    fun setCurrentIndex(index: Int) {
        cIndex.value = index.let {
            if (it < 0) 0
            else it
        }
    }

    fun setText(index: Int, text: String) {
        list[index] = text
    }

    inner class MDTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s?.contains("\n") == true) {
                enter()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
}
