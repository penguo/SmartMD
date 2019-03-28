package com.penguodev.smartmd.ui.editor

import com.penguodev.smartmd.model.ItemEdit

class EditorManager {

    private val list: MutableMap<Int, ItemEdit> = mutableMapOf()

    fun submitDocument(text: String) {
        text.split("\n").forEachIndexed { index, s ->
            list[index] = ItemEdit(s)
        }
    }

    fun getItemCount(): Int {
        return list.size
    }

    fun getItem(line: Int): ItemEdit {
        return list.getOrPut(line) {
            ItemEdit()
        }
    }
}