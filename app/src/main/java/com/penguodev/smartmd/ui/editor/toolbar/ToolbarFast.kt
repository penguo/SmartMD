package com.penguodev.smartmd.ui.editor.toolbar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.penguodev.mdeditor.MdEditor
import com.penguodev.mdeditor.components.MdGrammer
import com.penguodev.mdeditor.components.MdTextComponent
import com.penguodev.mdeditor.components.MdTextHeader
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ToolbarFastBinding

class ToolbarFast(lifecycleOwner: LifecycleOwner, private val mdEditor: MdEditor, viewGroup: ViewGroup) :
    ToolbarBase(viewGroup) {

    override val type: ToolbarType = ToolbarType.FAST

    override val binding = DataBindingUtil.inflate<ToolbarFastBinding>(
        LayoutInflater.from(viewGroup.context),
        R.layout.toolbar_fast,
        viewGroup,
        false
    ).apply {
        setLifecycleOwner(lifecycleOwner)
        clickHandler = ClickHandler()
    }

    inner class ClickHandler {
        fun onClickHeader(view: View) {
            mdEditor.adapter?.run {
                (getItem(getCurrentIndex()) as? MdTextComponent)?.let {
                    when (it.getHeader()) {
                        MdTextHeader.NORMAL -> addCurrentItemTextPrefix("# ")
                        MdTextHeader.H1, MdTextHeader.H2,
                        MdTextHeader.H3, MdTextHeader.H4,
                        MdTextHeader.H5 -> addCurrentItemTextPrefix("#")
                        MdTextHeader.H6 -> removeCurrentItemTextPrefix("###### ")
                    }
                }
            }
        }

        fun onClickBold(view: View) {
            val grammer = MdGrammer.Bold.grammer
            mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickItalic(view: View) {
            val grammer = MdGrammer.Italic.grammer
            mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickStrikethrough(view: View) {
            val grammer = MdGrammer.Strikethrough.grammer
            mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickBullet(view: View) {
            mdEditor.adapter?.addCurrentItemTextPrefix("* ")
        }

        fun onClickNumber(view: View) {
            mdEditor.adapter?.addCurrentItemTextPrefix("1. ")
        }

    }
}