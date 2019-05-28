package com.penguodev.smartmd.ui.editor.toolbar

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.penguodev.mdeditor.MdEditor

class ToolbarManager(lifecycleOwner: LifecycleOwner, private val mdEditor: MdEditor, private val viewGroup: ViewGroup) {
    private val toolbarList = listOf(
        ToolbarExpert(lifecycleOwner, mdEditor, viewGroup),
        ToolbarFast(lifecycleOwner, mdEditor, viewGroup)
    )

    fun getToolbar(type: ToolbarType): ToolbarBase {
        return toolbarList.find { it.type == type }!!
    }

    fun attachToolbar(type: ToolbarType) {
        toolbarList.filter { it.type != type }.forEach {
            it.detach()
        }
        getToolbar(type).attach()
    }
}
