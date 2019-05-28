package com.penguodev.mdeditor.components

import android.graphics.Typeface
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan

enum class MdGrammer(var grammer: String, var regex: Regex, var span: Any) {
    BoldItalic("***", Regex("\\*{3}"), StyleSpan(Typeface.BOLD_ITALIC)),
    BoldItalic2("___", Regex("_{3}"), StyleSpan(Typeface.BOLD_ITALIC)),
    Bold("**", Regex("\\*{2}"), StyleSpan(Typeface.BOLD)),
    Bold2("__", Regex("_{2}"), StyleSpan(Typeface.BOLD)),
    Italic("*", Regex("\\*"), StyleSpan(Typeface.ITALIC)),
    Italic2("_", Regex("_"), StyleSpan(Typeface.ITALIC)),
    Strikethrough("~~", Regex("~{2}"), StrikethroughSpan());
}