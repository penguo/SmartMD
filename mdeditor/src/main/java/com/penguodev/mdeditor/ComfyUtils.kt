package com.penguodev.mdeditor

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter

fun EditText.getSelection(): Pair<Int, Int> {
    val a = selectionStart
    val b = selectionEnd
    return if (a <= b) a to b
    else b to a
}

@BindingAdapter("visibleGone")
fun setVisibleGone(view: View, visibleGone: Boolean?) {
    view.visibility = if (visibleGone == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
