package com.penguodev.smartmd.ui.editor

import androidx.lifecycle.LifecycleOwner
import android.text.*
import android.view.KeyEvent
import android.widget.EditText
import com.penguodev.smartmd.common.fromHtml
import com.penguodev.smartmd.common.setVisibleGone
import com.penguodev.smartmd.common.ui.MDTextView
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.model.MarkdownFormat
import com.penguodev.smartmd.ui.editor.model.TextLine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class EditorManager(
    lifecycleOwner: LifecycleOwner,
    private val frontTV: MDTextView,
    private val editText: EditText,
    private val rearTV: MDTextView
) {
    private val mdManager = MarkdownManager()

    private val frontList = mutableListOf<TextLine>()
    private val rearList = mutableListOf<TextLine>()

    // 이전 아이템 수정 시 저장용
    private var prevDocument: ItemDocument? = null

    init {
        editText.addTextChangedListener(MDTextWatcher())
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.selectionStart == 0) {
                onDelPressed()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        initTV()
        notifyLineChanged(TextLine(""))
    }

    private fun enter() {
        val texts = editText.text.toString().split("\n")
        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                frontList.add(TextLine(s))
            } else {
                notifyLineChanged(TextLine(s))
            }
        }
    }

    private fun onDelPressed() {
        when {
            editText.selectionStart == 0 -> {
                val prevText = editText.text.toString()
                frontList.lastOrNull()?.let {
                    frontList.remove(it)
                    notifyLineChanged(TextLine(it.text + prevText), it.text.length)
                }
            }
        }
    }

    fun getList(): List<TextLine> {
        return mutableListOf<TextLine>().apply {
            addAll(frontList)
            add(TextLine(editText.text.toString()))
            addAll(rearList)
        }
    }

    private fun setIndex(index: Int, selection: Int? = null) {
        getList().let {
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

    fun notifyLineChanged(line: TextLine, selection: Int? = null) {
        val etText = line.getSpannable()
        editText.setText(etText)
        editText.setSelection(selection?.let {
            if (it > etText.length) etText.length
            else it
        } ?: etText.length)
        frontTV.text = mdManager.apply(
            if (frontList.isEmpty()) {
                ""
            } else {
                "${frontList.toEditorString()}\n"
            }
        )
        rearTV.text = mdManager.apply(
            if (rearList.isEmpty()) {
                ""
            } else {
                "\n${rearList.toEditorString()}"
            }
        )
        setVisibleGone(frontTV, frontList.isNotEmpty())
        setVisibleGone(rearTV, rearList.isNotEmpty())
    }

    private fun List<TextLine>.toEditorString(): String {
        return StringBuilder().apply {
            this@toEditorString.forEachIndexed { index, textLine ->
                if (index != 0) {
                    append("\n\n")
                }
                append(textLine.text)
            }
        }.toString()
    }

    fun initTV() {
        frontTV.setOnXYClickListener { view, touchX, touchY ->
            if (touchX == null || touchY == null) return@setOnXYClickListener
            val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
            findClickedLine(view.text, position)?.let {
                setIndex(it.first, it.second)
            }
        }
        rearTV.setOnXYClickListener { view, touchX, touchY ->
            if (touchX == null || touchY == null) return@setOnXYClickListener
            val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
            findClickedLine(view.text, position)?.let {
                setIndex((it.first) + getCurrentIndex() + 1, it.second)
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

    fun setItemDocument(id: Long) {
        GlobalScope.async {
            setItemDocument(MDDatabase.instance.documentDao.getItem(id))
        }
    }

    fun setItemDocument(item: ItemDocument?) {
        prevDocument = item
        if (item == null) return
        item.text.split("\n\n").toMutableList().let {
            it.forEachIndexed { index, s ->
                when (index) {
                    it.size - 1 -> return@forEachIndexed
                    it.size - 2 -> notifyLineChanged(TextLine(s))
                    else -> frontList.add(TextLine(s))
                }
            }
        }
    }

    fun getItemDocument(): ItemDocument {
        val list = getList()
        val header: String? = list.find { it.text.startsWith("# ") }?.text
        val text = StringBuilder().apply {
            list.forEachIndexed { index, s ->
                if (index != 0) {
                    append("\n\n")
                }
                append(s)
            }
        }.toString()
        // 이미 있는 문서 ?: 새로운 문서
        return prevDocument?.apply {
            this.header = header
            this.text = text
            this.lastUpdateTime = System.currentTimeMillis()
        } ?: ItemDocument(null, header, text, System.currentTimeMillis(), System.currentTimeMillis())
    }

}
