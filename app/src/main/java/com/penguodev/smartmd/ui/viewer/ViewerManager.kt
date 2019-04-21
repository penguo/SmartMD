package com.penguodev.smartmd.ui.viewer

import android.text.*
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.penguodev.smartmd.common.SoftKeyManager
import com.penguodev.smartmd.common.fromHtml
import com.penguodev.smartmd.common.setVisibleGone
import com.penguodev.smartmd.common.ui.MDTextView
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.MarkdownManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber

class ViewerManager(
    private val textView: TextView
) {
    private val mdManager = MarkdownManager()
    private val list = mutableListOf<String>()

    private fun update() {
        fromHtml(textView, "<br/>" + mdManager.apply(list) + "<br/>")
    }

    fun setItemDocument(id: Long) {
        GlobalScope.async {
            setItemDocument(MDDatabase.instance.documentDao.getItem(id))
        }
    }

    fun setItemDocument(item: ItemDocument?) {
        if (item == null) return
        list.clear()
        list.addAll(item.text.split("\n\n"))
        update()
    }

}