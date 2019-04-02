package com.penguodev.smartmd.ui.editor.model

import android.text.SpannableString

data class TextLine(
    val text: String,
    val format: MarkdownFormat?
) {
    constructor(text: String) : this(text, MarkdownFormat.findFormat(text))

    fun getSpannable(): SpannableString {
        return format?.getSpannableString(text) ?: SpannableString(text)
    }
}