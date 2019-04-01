package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.graphics.Typeface
import android.text.*
import android.text.method.KeyListener
import android.text.style.StyleSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.penguodev.smartmd.common.ComfyUtil
import com.penguodev.smartmd.common.fromHtml
import com.penguodev.smartmd.common.setVisibleGone

class EditorManager(
    lifecycleOwner: LifecycleOwner,
    private val startTV: TextView,
    private val editText: EditText,
    private val endTV: TextView
) {
    private val start = MutableLiveData<String>()
    private val end = MutableLiveData<String>()
    private val mdManager = MarkdownManager()

    init {
        editText.addTextChangedListener(MDTextWatcher(this))
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString() == "") {
                showPreLine(true)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        start.observe(lifecycleOwner, Observer {
            setVisibleGone(startTV, it != null || it == "")
            it?.let { text ->
                startTV.text = ComfyUtil.fromHtml(mdManager.apply(text) + "<br/>")
            }
        })
        end.observe(lifecycleOwner, Observer {
            setVisibleGone(endTV, it != null || it == "")
            it?.let { text ->
                endTV.text = ComfyUtil.fromHtml("<br/>" + mdManager.apply(text))
            }
        })
    }

    fun setEditableText(text: String) {
        editText.setText(text)
    }

    fun enter() {
        Log.d("Check", "enter triggered")
        val texts = editText.text.toString().split("\n")

        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                appendStart(s)
            } else {
                setEditableText(s)
            }
        }
    }

    private fun showPreLine(remove: Boolean) {
        val list = start.value?.split("\n\n")

        val afterLine = StringBuilder()
        list?.forEachIndexed { index, s ->
            if (index < list.size - 1) {
                if (index != 0) {
                    afterLine.append("\n\n")
                }
                afterLine.append(s)
            }
        }
        // it.last()의 경우 공백.
        val editableLine = list?.last()
        if (!remove) {
            appendEnd(editText.text.toString())
        }
        editText.setText(editableLine)
        editText.setSelection(editableLine?.length ?: 0)
        start.value = afterLine.toString().let { if (it != "") it else null }
    }

    fun appendStart(text: String) {
        start.value = start.value?.let { "$it\n\n$text" } ?: text
    }

    fun appendEnd(text: String) {
        end.value = end.value?.let { "$text\n\n$it" } ?: text
    }
}

class MDTextWatcher(private val editorManager: EditorManager) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if (s?.contains("\n") == true) {
            editorManager.enter()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}

class MarkdownManager {

    fun apply(text: String): String {
        val ssb = StringBuilder()
        val list = text.split("\n\n")
        list.forEachIndexed { index, s ->
            if (index != 0) {
                ssb.append("<br/><br/>")
            }
            ssb.append(convert(s))
        }
        return ssb.toString()
    }

    private fun convert(text: String): String {
        when {
            text.startsWith("# ") -> {
                return text.removePrefix("# ").let { "<h1>$it</h1>" }
            }
            text.startsWith("## ") -> {
                return text.removePrefix("## ").let { "<h2>$it</h2>" }
            }
            text.startsWith("### ") -> {
                return text.removePrefix("### ").let { "<h3>$it</h3>" }
            }
            text.startsWith("#### ") -> {
                return text.removePrefix("#### ").let { "<h4>$it</h4>" }
            }
            text.startsWith("##### ") -> {
                return text.removePrefix("##### ").let { "<h5>$it</h5>" }
            }
            text.startsWith("###### ") -> {
                return text.removePrefix("###### ").let { "<h6>$it</h6>" }
            }
        }
        return text
    }

}