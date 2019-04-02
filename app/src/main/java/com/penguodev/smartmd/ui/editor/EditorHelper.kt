package com.penguodev.smartmd.ui.editor

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.BindingViewHolder
import com.penguodev.smartmd.databinding.ItemHelperBinding

class EditorHelper(view: RecyclerView, editText: EditText, button: Button) {

    private val adapter = EditorHelperAdapter(editText, button)

    init {
        view.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.adapter = adapter
        view.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.HORIZONTAL))
    }

}

class EditorHelperAdapter(private val editText: EditText, private val button: Button) :
    RecyclerView.Adapter<BindingViewHolder>() {
    private val itemList: List<EditorHelperOption> = EditorHelperOption.getList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder.create(parent, R.layout.item_helper).apply {
            binding.root.setOnClickListener { itemList[adapterPosition].onClick(it, editText, button) }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        (holder.binding as ItemHelperBinding).item = itemList[position]
    }
}

enum class EditorHelperOption(
    val title: String,
    val srcResId: Int,
    private val clickListener: (v: View, et: EditText, btn: Button) -> Unit
) {
    HEADER(
        "헤더",
        R.drawable.ic_header,
        { v, et, btn ->
            when {
                et.text.startsWith("# ") -> et.text.insert(0, "#")
                et.text.startsWith("## ") -> et.text.insert(0, "#")
                et.text.startsWith("### ") -> et.text.insert(0, "#")
                et.text.startsWith("#### ") -> et.text.insert(0, "#")
                et.text.startsWith("##### ") -> et.text.insert(0, "#")
                else -> et.text.insert(0, "# ")
            }
        }
    ),
    BOLD(
        "굵게",
        R.drawable.ic_bold,
        getAddAndAddListener("굵게", "**")
    ),
    ITALIC(
        "기울게",
        R.drawable.ic_italic,
        getAddAndAddListener("기울게", "*")
    ),
    STRIKE_THROUGH(
        "취소선",
        R.drawable.ic_strikethrough,
        getAddAndAddListener("취소선", "~~")
    ),
    LIST_BULLET(
        "글머리표",
        R.drawable.ic_bullet,
        { v, et, btn ->
            et.text.insert(0, "- ")
        }
    ),
    LIST_NUMBER(
        "문단 번호",
        R.drawable.ic_number,
        { v, et, btn ->
            et.text.insert(0, "1. ")
        }
    );

    companion object {
        fun getList() = listOf(HEADER, BOLD, ITALIC, STRIKE_THROUGH, LIST_BULLET, LIST_NUMBER)
    }

    fun onClick(view: View, editText: EditText, button: Button) {
        clickListener.invoke(view, editText, button)
    }
}

private fun getAddAndAddListener(title: String, text: String): (v: View, et: EditText, btn: Button) -> Unit {
    return { v, et, btn ->
        et.text.insert(et.selectionStart, text)
        btn.text = "$title 완료"
        btn.setOnClickListener {
            et.text.insert(et.selectionStart, text)
            btn.visibility = View.GONE
        }
        btn.visibility = View.VISIBLE
    }
}