package com.penguodev.smartmd.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.BindingViewHolder
import com.penguodev.smartmd.databinding.ActivityListBinding
import com.penguodev.smartmd.databinding.ItemDocumentBinding
import com.penguodev.smartmd.model.ItemDocument

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: ListRcvAdapter
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        viewModel = ViewModelProviders.of(this)
            .get(ListViewModel::class.java)
        adapter = ListRcvAdapter().also { binding.recyclerView.adapter = it }
        viewModel.apply {
            itemList.observe(this@ListActivity, Observer {
                adapter.submitList(it)
            })
        }
    }
}
