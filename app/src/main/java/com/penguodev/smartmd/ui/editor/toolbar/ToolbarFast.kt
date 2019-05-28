package com.penguodev.smartmd.ui.editor.toolbar

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.penguodev.mdeditor.components.MdGrammer
import com.penguodev.mdeditor.components.MdTextComponent
import com.penguodev.mdeditor.components.MdTextHeader
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ToolbarFastBinding

class ToolbarFast(private val tManager: ToolbarManager) :
    ToolbarBase(tManager.viewGroup) {

    override val type: ToolbarType = ToolbarType.FAST

    override val binding = DataBindingUtil.inflate<ToolbarFastBinding>(
        LayoutInflater.from(tManager.viewGroup.context),
        R.layout.toolbar_fast,
        tManager.viewGroup,
        false
    ).apply {
        lifecycleOwner = tManager.lifecycleOwner
        clickHandler = ClickHandler()
    }

    inner class ClickHandler {
        fun onClickHeader(view: View) {
            tManager.mdEditor.adapter?.run {
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
            tManager.mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickItalic(view: View) {
            val grammer = MdGrammer.Italic.grammer
            tManager.mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickStrikethrough(view: View) {
            val grammer = MdGrammer.Strikethrough.grammer
            tManager.mdEditor.adapter?.addCurrentItemText(grammer, grammer)
        }

        fun onClickBullet(view: View) {
            tManager.mdEditor.adapter?.addCurrentItemTextPrefix("* ")
        }

        fun onClickNumber(view: View) {
            tManager.mdEditor.adapter?.addCurrentItemTextPrefix("1. ")
        }

        fun onClickSwap(view: View) {
            tManager.swapToolbar()
        }
    }
}