package com.penguodev.smartmd.common

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup

open class BindingViewHolder(open val binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, layoutResId: Int) =
            BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false))
    }
}