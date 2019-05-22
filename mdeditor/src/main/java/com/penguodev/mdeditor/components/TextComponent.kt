package com.penguodev.mdeditor.components

enum class TextHeader {
    H1, H2, H3, H4, H5, H6
}

enum class TextMode {
    UL, OL
}

interface Component {

}

data class Text(val text: String, val header: TextHeader?, val mode: TextMode?) : Component