package com.penguodev.smartmd.ui.editor

class MarkdownManager {

    private var cachedList = mutableListOf<String>()

    fun apply(list: MutableList<String>): String {
        cachedList.clear()
        list.forEachIndexed { index, s ->
            cachedList.add(applyLine(s))
        }
        return cachedList.toEditorString()
    }

    private fun applyLine(text: String): String {
        return if (text.startsWith("#")) {
            when {
                text.startsWith("# ") -> "<big><big><big><b>${text.removePrefix("# ")}</b></big></big></big>"
                text.startsWith("## ") -> "<big><big><b>${text.removePrefix("## ")}</b></big></big>"
                text.startsWith("### ") -> "<big><b>${text.removePrefix("### ")}</b></big>"
                text.startsWith("#### ") -> "<big>${text.removePrefix("#### ")}</big>"
                text.startsWith("##### ") -> "<b>${text.removePrefix("##### ")}</b>"
                text.startsWith("###### ") -> "<b>${text.removePrefix("###### ")}</b>"
                else -> text
            }
        } else {
            text
        }.replace("\n", "<br/>")
    }

    private fun List<String>.toEditorString(): String {
        return StringBuilder().apply {
            this@toEditorString.forEachIndexed { index, s ->
                if (index != 0) {
                    append("<br/><br/>")
                }
                append(s)
            }
        }.toString()
    }
}