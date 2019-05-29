package com.penguodev.mdeditor.components

import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log


data class MdComponent(var text: String) {

    var mode: TextMode? = null

    fun getHeader(): MdTextHeader {
        return if (text.startsWith("#")) {
            when {
                text.startsWith("# ") -> MdTextHeader.H1
                text.startsWith("## ") -> MdTextHeader.H2
                text.startsWith("### ") -> MdTextHeader.H3
                text.startsWith("#### ") -> MdTextHeader.H4
                text.startsWith("##### ") -> MdTextHeader.H5
                text.startsWith("###### ") -> MdTextHeader.H6
                else -> MdTextHeader.NORMAL
            }
        } else {
            MdTextHeader.NORMAL
        }
//        if (text.startsWith("- ")) mode = TextMode.OL
//        if (text.startsWith("1. ")) mode = TextMode.UL
    }

    fun getSpanned(): Spanned = SpannableStringBuilder(
        if (text.startsWith("#")) {
            when {
                text.startsWith("# ") -> text.replaceFirst("# ", getZeroWidthSpace(2))
                text.startsWith("## ") -> text.replaceFirst("## ", getZeroWidthSpace(3))
                text.startsWith("### ") -> text.replaceFirst("### ", getZeroWidthSpace(4))
                text.startsWith("#### ") -> text.replaceFirst("#### ", getZeroWidthSpace(5))
                text.startsWith("##### ") -> text.replaceFirst("##### ", getZeroWidthSpace(6))
                text.startsWith("###### ") -> text.replaceFirst("###### ", getZeroWidthSpace(7))
                else -> text
            }
        } else {
            text
        }
    ).apply {
        MdGrammer.values().forEach {
            if (this.contains(it.regex)) {
                this.applyRegex(it, it.span)
            }
        }
        var image: Uri? = null
        val imageRegex = Regex("!\\[.*]\\([^!]*\\)")
        if (text.contains(imageRegex)) {
            val matcher = imageRegex.toPattern().matcher(text)
            while (matcher.find()) {
                this.replace(matcher.start(), matcher.end(), getZeroWidthSpace(matcher.end() - matcher.start()))
            }
        }
    }

    fun getImage(): Uri? {
        // Image Detection
        // TODO:: 현재 버그로 첫번째의 Regex만 탐지되고 있음.
        var image: Uri? = null
        val imageRegex = Regex("!\\[.*]\\([^!]*\\)")
        if (text.contains(imageRegex)) {
            val matcher = imageRegex.toPattern().matcher(text)
            while (matcher.find()) {
                val value = matcher.group()
                val uriString = value.substring(value.indexOf('(') + 1, value.indexOf(')'))
                image = Uri.parse(uriString)
            }
        }
        return image
    }

    private fun SpannableStringBuilder.applyRegex(mdGrammer: MdGrammer, span: Any) {
        val match = mdGrammer.regex.toPattern().matcher(this)

        var start: Pair<Int, Int>? = null
        val regexLength = mdGrammer.grammer.length
        while (match.find()) {
            if (start == null) {
                start = match.start() to match.end()
            } else {
                Log.d("match", "${start.first} ~ ${match.end()}")
                replace(start.first, start.second, getZeroWidthSpace(regexLength))
                replace(match.start(), match.end(), getZeroWidthSpace(regexLength))
                setSpan(span, start.first, match.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                start = null
            }
        }
    }

    private fun getZeroWidthSpace(size: Int): String {
        return StringBuilder().apply {
            var count = 0
            while (size != count) {
                append("\u200B")
                count++
            }
        }.toString()
    }
}
