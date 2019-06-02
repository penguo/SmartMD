package com.penguodev.smartmd.common

import androidx.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
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

@BindingAdapter("saveTime")
fun setSaveTime(view: TextView, time: Long?) {
    if (time == null) {
        view.text = ""
        return
    }
    view.text = SimpleDateFormat("yyyy. M. d. aa h:mm:ss", Locale.KOREAN).format(Date(time)) + " 에 저장됨."
}

@BindingAdapter("lastUpdateTime")
fun setLastUpdateTime(view: TextView, time: Long?) {
    if (time == null) {
        view.text = ""
        return
    }
    view.text = "최근 수정일: " + SimpleDateFormat("yyyy. M. d. aa h:mm:ss", Locale.KOREAN).format(Date(time))
}