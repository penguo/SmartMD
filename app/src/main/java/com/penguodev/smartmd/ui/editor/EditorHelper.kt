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
    private val itemList: List<EditorHelperOption> = listOf(
        EditorHelperOption.HEADER,
        EditorHelperOption.BOLD
    )

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
        "header",
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
    BOLD("bold",
        R.drawable.ic_bold,
        { v, et, btn ->
            et.text.insert(et.selectionStart, "<b>")
            btn.setOnClickListener {
                et.text.insert(et.selectionStart, "</b>")
                btn.visibility = View.GONE
            }
            btn.visibility = View.VISIBLE
        }
    );

    fun onClick(view: View, editText: EditText, button: Button) {
        clickListener.invoke(view, editText, button)
    }
}