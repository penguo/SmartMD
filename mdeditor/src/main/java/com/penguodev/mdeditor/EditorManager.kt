package com.penguodev.mdeditor

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText


class EditorManager(
    private val frontTV: MdTextView,
    private val editText: EditText,
    private val rearTV: MdTextView
) {

    private val mdManager = MarkdownManager()

    private val frontList = mutableListOf<String>()
    private val rearList = mutableListOf<String>()

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
//        setNewDocument()
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

    private fun onDelPressed() {
        when {
            editText.selectionStart == 0 -> {
                val prevText = editText.text.toString()
                frontList.lastOrNull()?.let {
                    frontList.remove(it)
                    notifyLineChanged(it + prevText, it.length)
                }
            }
        }
    }

    fun getList(): List<String> {
        return mutableListOf<String>().apply {
            addAll(frontList)
            add(editText.text.toString())
            addAll(rearList)
        }
    }

    fun getSize(): Int {
        return frontList.size + rearList.size + 1
    }

    fun setIndex(index: Int, selection: Int? = null) {
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

    fun notifyLineChanged(text: String, selection: Int? = null) {
        // 텍스트에 포맷 적용
//        editText.setText(if (text.startsWith("#")) {
//            MarkdownFormat.findFormat(text)?.let { format ->
//                SpannableString(text).apply {
//                    format.spans.forEach {
//                        this.setSpan(it, 0, this.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//                    }
//                }
//            }
//        } else {
//            SpannableString(text)
//        })
        editText.setText(text)
        editText.setSelection(selection?.let {
            if (it > text.length) text.length
            else it
        } ?: text.length)
//        fromHtml(frontTV, "${mdManager.apply(frontList)}<br/>")
//        fromHtml(rearTV, "<br/>${mdManager.apply(rearList)}")
//        setVisibleGone(frontTV, frontList.isNotEmpty())
//        setVisibleGone(rearTV, rearList.isNotEmpty())
//        SoftKeyManager.show(editText)
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
        private var wait: Long = System.currentTimeMillis()
        override fun afterTextChanged(s: Editable?) {
            if (s?.contains("\n") == true) {
                enter()
            }
//            GlobalScope.async {
//                val time = System.currentTimeMillis()
//                wait = time
//                delay(100)
//                if (wait == time) {
//                    Timber.e("triggered! $time")
//                    val format = MarkdownFormat.findFormat(s.toString())
//                    if (format != null) {
//                        editText.setTypeface(editText.typeface, Typeface.BOLD)
//                    } else {
//                        editText.setTypeface(editText.typeface, Typeface.NORMAL)
//                    }
//                }
//            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
//
//    fun setItemDocument(item: ItemDocument?) {
//        prevDocument = item
//        if (item == null) return
//        item.text.split("\n\n").toMutableList().let {
//            it.forEachIndexed { index, s ->
//                when (index) {
//                    it.size - 1 -> notifyLineChanged(s)
//                    else -> frontList.add(s)
//                }
//            }
//        }
//    }
//
//    fun setNewDocument() {
//        prevDocument = null
//        notifyLineChanged("# ")
//    }
//
//    fun getItemDocument(): ItemDocument {
//        val list = getList()
//        val header: String? = list.find { it.startsWith("# ") }?.removePrefix("# ")
//        val text = StringBuilder().apply {
//            list.forEachIndexed { index, s ->
//                if (index != 0) {
//                    append("\n\n")
//                }
//                append(s)
//            }
//        }.toString()
//        // 이미 있는 문서 ?: 새로운 문서
//        return prevDocument?.apply {
//            this.header = header
//            this.text = text
//            this.lastUpdateTime = System.currentTimeMillis()
//        } ?: ItemDocument(null, header, text, System.currentTimeMillis(), System.currentTimeMillis())
//    }
}