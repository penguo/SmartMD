package com.penguodev.mdeditor

import android.widget.EditText

fun EditText.getSelection(): Pair<Int, Int> {
    val a = selectionStart
    val b = selectionEnd
    return if (a <= b) a to b
    else b to a
}