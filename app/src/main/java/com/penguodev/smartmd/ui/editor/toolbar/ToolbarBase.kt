package com.penguodev.smartmd.ui.editor.toolbar

import android.util.Log
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
            Log.d("Toolbar", "${type.name} is Attached")
        }
    }

    fun detach() {
        if (attached) {
            attached = false
            viewGroup.removeView(binding.root)
            Log.d("Toolbar", "${type.name} is Detached")
        }
    }
}