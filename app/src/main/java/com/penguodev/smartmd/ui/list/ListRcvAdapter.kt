package com.penguodev.smartmd.ui.list

import android.app.PendingIntent
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.TaskStackBuilder
import com.penguodev.smartmd.MDApplication.Companion.RC_LIST_TO_EDITOR
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.BindingViewHolder
import com.penguodev.smartmd.databinding.ItemDocumentBinding
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.EditorActivity
import com.penguodev.smartmd.ui.viewer.ViewerActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

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
                it.context.startActivity(ViewerActivity.createActivityIntent(it.context, getItem(adapterPosition).id))
            }
            binding.root.setOnLongClickListener {
                AlertDialog.Builder(it.context)
                    .setItems(arrayOf("보기", "수정", "삭제")) { dialog, which ->
                        when (which) {
                            0 -> {
                                it.context.startActivity(
                                    ViewerActivity.createActivityIntent(
                                        it.context,
                                        getItem(adapterPosition).id
                                    )
                                )
                            }
                            1 -> {
                                it.context.startActivities(
                                    arrayOf(
                                        ViewerActivity.createActivityIntent(
                                            it.context,
                                            getItem(adapterPosition).id
                                        ),
                                        EditorActivity.createActivityIntent(
                                            it.context,
                                            getItem(adapterPosition).id
                                        )
                                    )
                                )
                            }
                            2 -> AlertDialog.Builder(it.context)
                                .setTitle("정말로 삭제하시겠습니까?")
                                .setMessage("삭제한 문서는 복구되지 않습니다.")
                                .setPositiveButton("삭제") { subDialog, which ->
                                    GlobalScope.async {
                                        MDDatabase.instance.documentDao.delete(getItem(adapterPosition))
                                    }
                                }
                                .setNegativeButton("취소") { subDialog, which ->
                                    subDialog.dismiss()
                                }
                                .show()
                        }
                    }.show()
                return@setOnLongClickListener true
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