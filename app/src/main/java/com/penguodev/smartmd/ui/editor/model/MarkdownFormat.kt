package com.penguodev.smartmd.ui.editor.model

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

enum class MarkdownFormat(
    val shortName: String,
    val spans: List<Any>,
    val condition: (String) -> Boolean,
    val operation: ((String) -> String)? = null
) {
    H1(
        "h1",
        listOf(
            StyleSpan(Typeface.BOLD),
            RelativeSizeSpan(3f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("# ")
        },
        {
            it.removePrefix("# ")
        }
    ),
    H2(
        "h2",
        listOf(
            StyleSpan(Typeface.BOLD),
            RelativeSizeSpan(1.8f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("## ")
        },
        {
            it.removePrefix("## ")
        }
    ),
    H3(
        "h3",
        listOf(
            StyleSpan(Typeface.BOLD),
            RelativeSizeSpan(1.4f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("### ")
        },
        {
            it.removePrefix("### ")
        }
    ),
    H4(
        "h4",
        listOf(
            RelativeSizeSpan(1.4f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("#### ")
        },
        {
            it.removePrefix("#### ")
        }
    ),
    H5(
        "h5",
        listOf(
            StyleSpan(Typeface.BOLD),
            RelativeSizeSpan(1.1f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("##### ")
        },
        {
            it.removePrefix("##### ")
        }
    ),
    H6(
        "h6",
        listOf(
            RelativeSizeSpan(1.1f),
            ForegroundColorSpan(Color.BLACK)
        ),
        {
            it.startsWith("###### ")
        },
        {
            it.removePrefix("###### ")
        }
    );

    companion object {
        fun getList(): List<MarkdownFormat> = listOf(H1, H2, H3, H4, H5, H6)
        fun findFormat(text: String): MarkdownFormat? {
            getList().forEach {
                if (it.condition.invoke(text)) {
                    return it
                }
            }
            return null
        }
    }

    fun getSpannableString(text: String): SpannableString {
        return SpannableString(this.operation?.invoke(text) ?: text).apply {
            spans.forEach {
                setSpan(it, 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}