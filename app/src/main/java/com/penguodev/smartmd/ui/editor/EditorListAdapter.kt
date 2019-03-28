package com.penguodev.smartmd.ui.editor

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.BindingViewHolder
import com.penguodev.smartmd.databinding.ItemEditorBinding
import com.penguodev.smartmd.databinding.ItemEditorEditBinding
import com.penguodev.smartmd.model.ItemEdit


class EditorListAdapter(private val viewModel: EditorViewModel) :
    RecyclerView.Adapter<BindingViewHolder>() {

    fun getItem(position: Int): ItemEdit {
        return viewModel.editorManager.getItem(position)
    }

    override fun getItemCount(): Int {
        return viewModel.editorManager.getItemCount()
    }

    var currentFocusedLine: Int? = null

    fun notifyFocusChanged(line: Int?) {
        var from = -1
        var to = -1

        currentFocusedLine?.let {
            from = it
        }
        line?.let {
            currentFocusedLine = line
            when {
                from == -1 -> from = line
                from <= line -> to = line
                from > line -> {
                    to = from
                    from = line
                }
            }
        }
        when {
            from == -1 && to == -1 -> return
            from != -1 && to == -1 -> notifyItemChanged(from)
            from != -1 && to != -1 -> notifyItemRangeChanged(from, to)
        }
    }

    companion object {
        const val DEFAULT = 0
        const val FOCUSED = 1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.itemAnimator = null
        recyclerView.layoutAnimation = null
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentFocusedLine == position) {
            FOCUSED
        } else {
            DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return if (viewType == FOCUSED) {
            BindingViewHolder.create(parent, R.layout.item_editor_edit)
        } else {
            BindingViewHolder.create(parent, R.layout.item_editor).apply {
                binding.root.setOnClickListener {
                    viewModel.focusedLine.value = adapterPosition
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            when (this) {
                is ItemEditorBinding -> {
                    this.item = item
                }
                is ItemEditorEditBinding -> {
                    this.item = item
                }
            }
        }
    }
}
