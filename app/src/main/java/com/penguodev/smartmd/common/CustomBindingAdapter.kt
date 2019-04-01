package com.penguodev.smartmd.common

import android.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import android.widget.TextView

@BindingAdapter("fromHtml")
fun fromHtml(view: TextView, text: String?) {
    view.text = ComfyUtil.fromHtml(text)
}

@BindingAdapter("visibleGone")
fun setVisibleGone(view: View, visibleGone: Boolean?) {
    view.visibility = if (visibleGone == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}