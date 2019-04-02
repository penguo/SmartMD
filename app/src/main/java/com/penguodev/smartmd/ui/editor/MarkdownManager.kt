package com.penguodev.smartmd.ui.editor

import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.penguodev.smartmd.ui.editor.model.MarkdownFormat
import java.util.*

class MarkdownManager {

    fun apply(text: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        val list = text.split("\n\n")
        list.forEachIndexed { index, s ->
            if (index != 0) {
                ssb.append("\n\n")
            }
            ssb.append(convert(s))
        }
        return ssb
    }

    private fun convert(text: String): SpannableString {
        MarkdownFormat.getList().forEach {
            if (it.condition.invoke(text)) {
                return it.getSpannableString(text)
            }
        }
        return SpannableString(text)
    }

}