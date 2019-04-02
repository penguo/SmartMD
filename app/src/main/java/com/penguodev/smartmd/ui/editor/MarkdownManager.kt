package com.penguodev.smartmd.ui.editor

class MarkdownManager {

        fun apply(text: String): String {
            val ssb = StringBuilder()
            val list = text.split("\n\n")
            list.forEachIndexed { index, s ->
                if (index != 0) {
                    ssb.append("<br/><br/>")
                }
                ssb.append(convert(s))
            }
            return ssb.toString()
        }

        private fun convert(text: String): String {
            when {
                text.startsWith("# ") -> {
                    return text.removePrefix("# ").let { "<h1>$it</h1>" }
                }
                text.startsWith("## ") -> {
                    return text.removePrefix("## ").let { "<h2>$it</h2>" }
                }
                text.startsWith("### ") -> {
                    return text.removePrefix("### ").let { "<h3>$it</h3>" }
                }
                text.startsWith("#### ") -> {
                    return text.removePrefix("#### ").let { "<h4>$it</h4>" }
                }
                text.startsWith("##### ") -> {
                    return text.removePrefix("##### ").let { "<h5>$it</h5>" }
                }
                text.startsWith("###### ") -> {
                    return text.removePrefix("###### ").let { "<h6>$it</h6>" }
                }
            }
            return text
        }

    }