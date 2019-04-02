package com.penguodev.smartmd.common

import androidx.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("srcResId")
fun setSrcResId(view: ImageView, resId: Int?) {
    if (resId == null) {
        view.setImageDrawable(null)
    } else {
        view.setImageResource(resId)
    }
}

@BindingAdapter("autoSaveTime")
fun setAutoSaveTime(view: TextView, time: Long?) {
    if (time == null) {
        view.text = ""
        return
    }
    view.text = SimpleDateFormat("aa hh:mm:ss", Locale.KOREAN).format(Date(time)) + "에 자동 저장됨."
}