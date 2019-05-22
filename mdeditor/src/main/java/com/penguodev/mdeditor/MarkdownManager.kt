package com.penguodev.mdeditor

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
        var t = text
        return if (t.startsWith("#")) {
            t = when {
                t.startsWith("# ") -> "<big><big><big><b>${t.removePrefix("# ")}</b></big></big></big>"
                t.startsWith("## ") -> "<big><big><b>${t.removePrefix("## ")}</b></big></big>"
                t.startsWith("### ") -> "<big><b>${t.removePrefix("### ")}</b></big>"
                t.startsWith("#### ") -> "<big>${t.removePrefix("#### ")}</big>"
                t.startsWith("##### ") -> "<b>${t.removePrefix("##### ")}</b>"
                t.startsWith("###### ") -> "<b>${t.removePrefix("###### ")}</b>"
                else -> t
            }
            t
        } else {
            var repeat = true
            while (repeat) {
                t = when {
                    t.startsWith("* ") -> "\u25A0 ${t.removePrefix("* ")}"
                    t.startsWith("- ") -> "\u25A0 ${t.removePrefix("- ")}"
                    t.startsWith("  * ") -> "    \u25A0 ${t.removePrefix("  * ")}"
                    t.startsWith("  - ") -> "    \u25A0 ${t.removePrefix("  - ")}"
                    t.startsWith("    * ") -> "        \u25A0 ${t.removePrefix("    * ")}"
                    t.startsWith("    - ") -> "        \u25A0 ${t.removePrefix("    - ")}"
                    t.contains("***") -> t.applyRegex("***", "<b><i>", "</i></b>")
                    t.contains("___") -> t.applyRegex("___", "<b><i>", "</i></b>")
                    t.contains("**") -> t.applyRegex("**", "<b>", "</b>")
                    t.contains("__") -> t.applyRegex("__", "<b>", "</b>")
                    t.contains("*") -> t.applyRegex("*", "<i>", "</i>")
                    t.contains("_") -> t.applyRegex("_", "<i>", "</i>")
                    t.contains("~~") -> t.applyRegex("~~", "<strike>", "</strike>")
                    t.startsWith("> ") -> "$blockquote ${t.removePrefix("> ")}"
                    t.startsWith(">> ") -> "$blockquote$blockquote ${t.removePrefix(">> ")}"
                    else -> {
                        repeat = false
                        t
                    }
                }
            }
            t
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