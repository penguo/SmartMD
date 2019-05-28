package com.penguodev.smartmd.ui.editor.toolbar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.penguodev.mdeditor.MdEditor
import com.penguodev.mdeditor.components.MdTextComponent
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ToolbarExpertBinding

class ToolbarExpert(lifecycleOwner: LifecycleOwner, private val mdEditor: MdEditor, viewGroup: ViewGroup) :
    ToolbarBase(viewGroup) {

    override val type: ToolbarType = ToolbarType.EXPERT

    override val binding = DataBindingUtil.inflate<ToolbarExpertBinding>(
        LayoutInflater.from(viewGroup.context),
        R.layout.toolbar_expert,
        viewGroup,
        false
    ).apply {
        setLifecycleOwner(lifecycleOwner)
        clickHandler = ClickHandler()
    }

    inner class ClickHandler {
        fun onClickPound(view: View) {
            mdEditor.adapter?.addCurrentItemText("#")
        }

        fun onClickStar(view: View) {
            mdEditor.adapter?.addCurrentItemText("*")
        }

        fun onClickTilde(view: View) {
            mdEditor.adapter?.addCurrentItemText("~")
        }

        fun onClickSB(view: View) {
            mdEditor.adapter?.addCurrentItemText("[", "]")
        }

        fun onClickParentheses(view: View) {
            mdEditor.adapter?.addCurrentItemText("(", ")")
        }

        fun onClickPB(view: View) {
            mdEditor.adapter?.addCurrentItemText(">")
        }
    }
}