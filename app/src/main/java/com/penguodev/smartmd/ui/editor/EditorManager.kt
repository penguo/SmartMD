//package com.penguodev.smartmd.ui.editor
//
//import android.arch.lifecycle.LifecycleOwner
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.Observer
//import android.text.*
//import android.util.Log
//import android.view.KeyEvent
//import android.widget.EditText
//import com.penguodev.smartmd.common.ComfyUtil
//import com.penguodev.smartmd.common.setVisibleGone
//import com.penguodev.smartmd.common.ui.MDTextView
//import timber.log.Timber
//
//
//class EditorManager(
//    lifecycleOwner: LifecycleOwner,
//    private val startTV: MDTextView,
//    private val editText: EditText,
//    private val endTV: MDTextView
//) {
//    private val start = MutableLiveData<String>()
//    private val end = MutableLiveData<String>()
//    private val mdManager = MarkdownManager()
//
//    init {
//        editText.addTextChangedListener(MDTextWatcher(this))
//        editText.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString() == "") {
//                showPreLine(true)
//                return@setOnKeyListener true
//            }
//            return@setOnKeyListener false
//        }
//        start.observe(lifecycleOwner, Observer {
//            setVisibleGone(startTV, it != null || it == "")
//            it?.let { text ->
//                startTV.text = ComfyUtil.fromHtml(mdManager.apply(text) + "<br/>")
//            }
//        })
//        end.observe(lifecycleOwner, Observer {
//            setVisibleGone(endTV, it != null || it == "")
//            it?.let { text ->
//                endTV.text = ComfyUtil.fromHtml("<br/>" + mdManager.apply(text))
//            }
//        })
//        initTV(startTV)
//        initTV(endTV)
//    }
//
//    fun setEditableText(text: String) {
//        editText.setText(text)
//    }
//
//    fun enter() {
//        val texts = editText.text.toString().split("\n")
//
//        texts.forEachIndexed { index, s ->
//            if (index != texts.size - 1) {
//                appendStart(s)
//            } else {
//                setEditableText(s)
//            }
//        }
//    }
//
//    fun getLineIndex(): Int {
//        // size - 1 + 1
//        return start.value?.split("\n\n")?.size ?: -1
//    }
//
//    private fun showPreLine(remove: Boolean) {
//        val list = start.value?.split("\n\n")
//
//        val afterLine = StringBuilder()
//        list?.forEachIndexed { index, s ->
//            if (index < list.size - 1) {
//                if (index != 0) {
//                    afterLine.append("\n\n")
//                }
//                afterLine.append(s)
//            }
//        }
//        // it.last()의 경우 공백.
//        val editableLine = list?.last()
//        if (!remove) {
//            appendEnd(editText.text.toString())
//        }
//        editText.setText(editableLine)
//        editText.setSelection(editableLine?.length ?: 0)
//        setLiveValue(start, afterLine.toString())
//    }
//
//    private fun setLine(line: Int?) {
//        val currentLine = getLineIndex()
//        val newStart = StringBuilder()
//        val newEnd = StringBuilder()
//        Timber.d("setLine: $line, currentLine: $currentLine")
//        when {
//            line == null -> {
//                return
//            }
//            line == currentLine -> {
//                return
//            }
//            line < currentLine -> {
//                var text: String? = null
//                start.value?.split("\n\n")?.forEachIndexed { index, s ->
//                    when {
//                        index < line -> {
//                            if (index > 0) {
//                                newStart.append("\n\n")
//                            }
//                            newStart.append(s)
//                        }
//                        index == line -> {
//                            text = s
//                        }
//                        index > line -> {
//                            if (index - line > 1) {
//                                newEnd.append("\n\n")
//                            }
//                            newEnd.append(s)
//                        }
//                    }
//                }
//                setLiveValue(start, newStart.toString())
//                setLiveValue(end, newEnd.toString())
//                editText.setText(text)
////                var text: String? = null
////                start.value?.split("\n\n")?.forEachIndexed { index, s ->
////                    when {
////                        index < line -> {
////                            if (index != 0) {
////                                newStart.append("\n\n")
////                            }
////                            newStart.append(s)
////                        }
////                        index == line -> {
////                            text = s
////                        }
////                        index > line -> {
////                            if (index - line - 1 != 0) {
////                                newEnd.append("\n\n")
////                            }
////                            newEnd.append(s)
////                        }
////                    }
////                }
////                start.value = newStart.toString()
////                end.value = newEnd.toString() + "\n\n" + end.value
////                editText.setText(text)
////                Log.d("Check", "output: $line $currentLine $text")
//            }
//            line > currentLine -> {
//            }
//            else -> return
//        }
//    }
//
//    fun appendStart(text: String) {
//        setLiveValue(start, start.value?.let { "$it\n\n$text" } ?: text)
//    }
//
//    fun appendEnd(text: String) {
//        setLiveValue(end, end.value?.let { "$it\n\n$text" } ?: text)
//    }
//
//    fun initTV(view: MDTextView) {
//        view.setOnXYClickListener { _, touchX, touchY ->
//            if (touchX == null || touchY == null) return@setOnXYClickListener
//            val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
//            setLine(findClickedLine(view.text, position))
//        }
//    }
//
//    fun setLiveValue(liveData: MutableLiveData<String>, text: String?) {
//        if (text == null || text == "") liveData.value = null
//        else liveData.value = text
//    }
//
//    private fun findClickedLine(text: CharSequence, position: Int): Int? {
//        var tempPosition = 0
//        text.split("\n\n").forEachIndexed { index, s ->
//            if (tempPosition <= position && position < tempPosition + s.length + 2) {
//                return index
//            } else {
//                tempPosition += s.length + 2
//            }
//        }
//        return null
//    }
//}
//
//class MDTextWatcher(private val editorManager: EditorManager) : TextWatcher {
//    override fun afterTextChanged(s: Editable?) {
//        if (s?.contains("\n") == true) {
//            editorManager.enter()
//        }
//    }
//
//    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//    }
//
//    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//    }
//
//}
//
//class MarkdownManager {
//
//    fun apply(text: String): String {
//        val ssb = StringBuilder()
//        val list = text.split("\n\n")
//        list.forEachIndexed { index, s ->
//            if (index != 0) {
//                ssb.append("<br/><br/>")
//            }
//            ssb.append(convert(s))
//        }
//        return ssb.toString()
//    }
//
//    private fun convert(text: String): String {
//        when {
//            text.startsWith("# ") -> {
//                return text.removePrefix("# ").let { "<h1>$it</h1>" }
//            }
//            text.startsWith("## ") -> {
//                return text.removePrefix("## ").let { "<h2>$it</h2>" }
//            }
//            text.startsWith("### ") -> {
//                return text.removePrefix("### ").let { "<h3>$it</h3>" }
//            }
//            text.startsWith("#### ") -> {
//                return text.removePrefix("#### ").let { "<h4>$it</h4>" }
//            }
//            text.startsWith("##### ") -> {
//                return text.removePrefix("##### ").let { "<h5>$it</h5>" }
//            }
//            text.startsWith("###### ") -> {
//                return text.removePrefix("###### ").let { "<h6>$it</h6>" }
//            }
//        }
//        return text
//    }
//
//}