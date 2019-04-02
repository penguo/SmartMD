package com.penguodev.smartmd.ui.editor

class MarkdownManager {

    companion object {
        private const val blockquote = "    |"
    }

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
                text.startsWith("# ") -> "<big><big><big><big><b>${text.removePrefix("# ")}</b></big></big></big></big>"
                text.startsWith("## ") -> "<big><big><b>${text.removePrefix("## ")}</b></big></big>"
                text.startsWith("### ") -> "<big><b>${text.removePrefix("### ")}</b></big>"
                text.startsWith("#### ") -> "<big>${text.removePrefix("#### ")}</big>"
                text.startsWith("##### ") -> "<b>${text.removePrefix("##### ")}</b>"
                text.startsWith("###### ") -> "<b>${text.removePrefix("###### ")}</b>"
                else -> text
            }
        } else {
            when {
                text.startsWith("* ") -> "\u25A0 ${text.removePrefix("* ")}"
                text.startsWith("- ") -> "\u25A0 ${text.removePrefix("* ")}"
                text.contains("***") -> text.applyRegex("***", "<b><i>", "</i></b>")
                text.contains("___") -> text.applyRegex("___", "<b><i>", "</i></b>")
                text.contains("**") -> text.applyRegex("**", "<b>", "</b>")
                text.contains("__") -> text.applyRegex("__", "<b>", "</b>")
                text.contains("*") -> text.applyRegex("*", "<i>", "</i>")
                text.contains("_") -> text.applyRegex("_", "<i>", "</i>")
                text.contains("~~") -> text.applyRegex("~~", "<strike>", "</strike>")
                text.startsWith("> ") -> "$blockquote ${text.removePrefix("> ")}"
                text.startsWith(">> ") -> "$blockquote$blockquote ${text.removePrefix(">> ")}"

                else -> text
            }
        }.replace("\n", "<br/>").replace(" ", "&nbsp;")
    }

    private fun String.applyRegex(regex: String, front: String, end: String): String {
        return this.split(regex).let {
            StringBuilder().apply {
                it.forEachIndexed { index, s ->
                    if (index % 2 == 1) {
                        if (it.size > index + 1) {
                            if (s.isNotEmpty()) {
                                append("$front$s$end")
                            } else {
                                append("$regex$regex")
                            }
                        } else {
                            append("$regex$s")
                        }
                    } else {
                        append(s)
                    }
                }
            }.toString()
        }
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