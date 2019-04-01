package com.penguodev.smartmd.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.text.Layout


class MDTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : TextView(context, attrs, defStyleAttr) {

    private var touchX: Float? = null
    private var touchY: Float? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchX = event?.x
        touchY = event?.y
        return super.onTouchEvent(event)
    }

    fun setOnXYClickListener(listener: ((view: View, touchX: Float?, touchY: Float?) -> Unit)? = null) {
        if (listener == null) {
            setOnClickListener(null)
        } else {
            setOnClickListener {
                listener.invoke(this@MDTextView, touchX, touchY)
            }
        }
    }

    fun getPreciseOffset(x: Int, y: Int): Int {
        val layout = layout

        if (layout != null) {
            val topVisibleLine = layout.getLineForVertical(y)
            val offset = layout.getOffsetForHorizontal(topVisibleLine, x.toFloat())

            val offset_x = layout.getPrimaryHorizontal(offset).toInt()
            if (offset_x > x) {
                return layout.getOffsetToLeftOf(offset)
            }
        }
        return getOffset(x, y)
    }

    fun getOffset(x: Int, y: Int): Int {
        val layout = layout
        var offset = -1

        if (layout != null) {
            val topVisibleLine = layout.getLineForVertical(y)
            offset = layout.getOffsetForHorizontal(topVisibleLine, x.toFloat())
        }

        return offset
    }
}