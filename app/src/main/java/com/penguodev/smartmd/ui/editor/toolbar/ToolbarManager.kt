package com.penguodev.smartmd.ui.editor.toolbar

import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.penguodev.mdeditor.MdEditor
import com.penguodev.smartmd.R

class ToolbarManager(val lifecycleOwner: LifecycleOwner, val mdEditor: MdEditor, val viewGroup: ViewGroup) {
    private val toolbarList = listOf(
        ToolbarExpert(this),
        ToolbarFast(this)
    )
    private var currentAttachedType: ToolbarType? = null
    var pickImageFunction: (() -> Unit)? = null

    fun getToolbar(type: ToolbarType): ToolbarBase {
        return toolbarList.find { it.type == type }!!
    }

    fun attachToolbar(type: ToolbarType) {
        if (currentAttachedType == type) return
        toolbarList.filter { it.type != type }.forEach {
            it.detach()
        }
        getToolbar(type).attach()
        currentAttachedType = type
    }

    fun swapToolbar() {
        when (currentAttachedType) {
            ToolbarType.EXPERT -> attachToolbar(ToolbarType.FAST)
            ToolbarType.FAST -> attachToolbar(ToolbarType.EXPERT)
            null -> attachToolbar(ToolbarType.FAST)
        }
    }

    fun onClickImage() {
        pickImageFunction?.let {
            it.invoke()
        } ?: run {
            Toast.makeText(viewGroup.context, viewGroup.context.getString(R.string.unsupported), Toast.LENGTH_SHORT)
                .show()
        }
    }
}
