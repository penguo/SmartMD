package com.penguodev.smartmd.ui.list

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.BindingViewHolder
import com.penguodev.smartmd.databinding.ItemDocumentBinding
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.ui.editor.EditorActivity

class ListRcvAdapter : ListAdapter<ItemDocument, BindingViewHolder>(object : DiffUtil.ItemCallback<ItemDocument>() {
    override fun areItemsTheSame(p0: ItemDocument, p1: ItemDocument): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: ItemDocument, p1: ItemDocument): Boolean {
        return p0 == p1
    }

}) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BindingViewHolder {
        return BindingViewHolder.create(p0, R.layout.item_document).apply {
            binding.root.setOnClickListener {
                it.context.startActivity(EditorActivity.createActivityIntent(it.context, getItem(adapterPosition).id))
            }
        }
    }

    override fun onBindViewHolder(p0: BindingViewHolder, p1: Int) {
        with(p0.binding) {
            when (this) {
                is ItemDocumentBinding -> {
                    this.item = getItem(p1)
                }
            }
        }
    }
}