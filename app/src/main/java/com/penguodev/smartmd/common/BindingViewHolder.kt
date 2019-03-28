package com.penguodev.smartmd.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup

open class BindingViewHolder(open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, layoutResId: Int) =
            BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false))
    }
}