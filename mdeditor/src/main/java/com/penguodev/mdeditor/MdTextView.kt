package com.penguodev.mdeditor

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.penguodev.mdeditor.components.MdTextHeader
import android.provider.MediaStore.Images.Media.getBitmap
import android.R.attr.bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView


class MdTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : TextView(context, attrs, defStyleAttr) {

    companion object {
        @JvmStatic
        @BindingAdapter("mdHeader")
        fun setMdHeader(view: MdTextView, header: MdTextHeader?) {
            (header ?: MdTextHeader.NORMAL).let {
                view.textSize = it.textSizeSP.toFloat()
                view.setTypeface(null, it.textStyle)
            }
        }

        @JvmStatic
        @BindingAdapter("mdHeader")
        fun setMdHeader(view: EditText, header: MdTextHeader?) {
            (header ?: MdTextHeader.NORMAL).let {
                view.textSize = it.textSizeSP.toFloat()
                view.setTypeface(null, it.textStyle)
            }
        }

        @JvmStatic
        @BindingAdapter("mdImage")
        fun setMdImage(view: ImageView, uri: Uri?) {
            if (uri == null) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
                try {
                    view.setImageURI(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    view.setImageResource(R.drawable.img_not_found)
                }
            }
        }
    }

    private var touchX: Float? = null
    private var touchY: Float? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchX = event?.x
        touchY = event?.y
        return super.onTouchEvent(event)
    }

    fun setOnXYClickListener(listener: ((view: MdTextView, touchX: Float?, touchY: Float?) -> Unit)? = null) {
        if (listener == null) {
            setOnClickListener(null)
        } else {
            setOnClickListener {
                listener.invoke(this@MdTextView, touchX, touchY)
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