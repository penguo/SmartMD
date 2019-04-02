package com.penguodev.smartmd.ui.editor

import androidx.lifecycle.LifecycleOwner
import android.text.*
import android.view.KeyEvent
import android.widget.EditText
import com.penguodev.smartmd.common.setVisibleGone
import com.penguodev.smartmd.common.ui.MDTextView
import timber.log.Timber


class EditorManager(
    lifecycleOwner: LifecycleOwner,
    private val frontTV: MDTextView,
    private val editText: EditText,
    private val rearTV: MDTextView
) {
    private val mdManager = MarkdownManager()

    private val frontList = mutableListOf<String>()
    private val rearList = mutableListOf<String>()

    init {
        editText.addTextChangedListener(MDTextWatcher())
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString() == "") {
                delete()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        initTV()
        notifyLineChanged("")
    }

    private fun enter() {
        val texts = editText.text.toString().split("\n")
        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                frontList.add(s)
            } else {
                notifyLineChanged(s)
            }
        }
    }

    private fun delete() {
        frontList.lastOrNull()?.let {
            frontList.remove(it)
            notifyLineChanged(it)
        }
    }

    private fun setIndex(index: Int, selection: Int? = null) {
        mutableListOf<String>().apply {
            addAll(frontList)
            add(editText.text.toString())
            addAll(rearList)
        }.let {
            frontList.clear()
            if (index != 0) {
                frontList.addAll(it.subList(0, index))
            }
            rearList.clear()
            if (index < it.size - 1) {
                rearList.addAll(it.subList(index + 1, it.size))
            }
            notifyLineChanged(it[index], selection)
        }
    }

    fun getCurrentIndex(): Int {
        // frontList.size - 1 + 1
        return frontList.size
    }

    fun notifyLineChanged(text: String, selection: Int? = null) {
        editText.setText(text)
        editText.setSelection(selection?.let {
            if (it > text.length) text.length
            else it
        } ?: text.length)
        frontTV.text = mdManager.apply(if (frontList.isEmpty()) {
            ""
        } else {
            "${frontList.toEditorString()}\n"
        })
        rearTV.text = mdManager.apply(if (rearList.isEmpty()) {
            ""
        } else {
            "\n${rearList.toEditorString()}"
        })
        setVisibleGone(frontTV, frontList.isNotEmpty())
        setVisibleGone(rearTV, rearList.isNotEmpty())
    }

    private fun List<String>.toEditorString(): String {
        return StringBuilder().apply {
            this@toEditorString.forEachIndexed { index, s ->
                if (index != 0) {
                    append("\n\n")
                }
                append(s)
            }
        }.toString()
    }

    fun initTV() {
        frontTV.setOnXYClickListener { view, touchX, touchY ->
            if (touchX == null || touchY == null) return@setOnXYClickListener
            val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
            findClickedLine(view.text, position).let {
                setIndex(it?.first ?: 0, it?.second)
            }
        }
        rearTV.setOnXYClickListener { view, touchX, touchY ->
            if (touchX == null || touchY == null) return@setOnXYClickListener
            val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
            findClickedLine(view.text, position).let {
                setIndex((it?.first ?: 0) + getCurrentIndex() + 1, it?.second)
            }
        }
    }

    private fun findClickedLine(text: CharSequence, position: Int): Pair<Int, Int>? {
        var tempPosition = 0
        text.split("\n\n").forEachIndexed { index, s ->
            if (tempPosition <= position && position < tempPosition + s.length + 2) {
                return index to (position - tempPosition).let { if (it <= s.length) it else s.length }
            } else {
                tempPosition += s.length + 2
            }
        }
        return null
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
