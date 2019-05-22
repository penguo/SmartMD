package com.penguodev.mdeditor.components

enum class TextHeader {
    H1, H2, H3, H4, H5, H6
}

enum class TextMode {
    UL, OL
}

interface MdComponent {

}

data class TextComponent(var text: String, var header: TextHeader?, var mode: TextMode?) : MdComponent