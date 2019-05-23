package com.penguodev.mdeditor.components

interface MdComponent {
    companion object {
        fun getMdComponent(text: String): MdComponent {
            return MdTextComponent(text)
        }
    }
}