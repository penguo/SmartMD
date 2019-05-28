package com.penguodev.smartmd.ui.editor.toolbar

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class ToolbarBase(private val viewGroup: ViewGroup) {
    abstract val binding: ViewDataBinding
    abstract val type: ToolbarType

    private var attached: Boolean = false

    fun attach() {
        if (!attached) {
            attached = true
            viewGroup.addView(binding.root)
        }
    }

    fun detach() {
        if (attached) {
            attached = false
            viewGroup.removeView(binding.root)
        }
    }
}