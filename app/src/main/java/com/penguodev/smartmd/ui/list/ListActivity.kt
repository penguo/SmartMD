package com.penguodev.smartmd.ui.list

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityListBinding
import com.penguodev.smartmd.ui.editor.EditorActivity
import kotlinx.coroutines.*
import timber.log.Timber

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: ListRcvAdapter
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        viewModel = ViewModelProviders.of(this)
            .get(ListViewModel::class.java)
            .also { binding.viewModel = it }
        adapter = ListRcvAdapter().also {
            binding.recyclerView.adapter = it
            binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        }
        viewModel.apply {
            itemList.observe(this@ListActivity, Observer {
                Timber.d("itemListSize: ${it.size}")
                adapter.submitList(it)
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.Default) {
                        delay(300)
                    }
                    binding.recyclerView.smoothScrollToPosition(0)
                }
            })
        }
    }

    inner class ClickHandler {
        fun onClickAdd(view: View) {
//            startActivity(EditorActivity.createActivityIntent(view.context, null))
            startActivity(Intent(this@ListActivity, EditorActivity::class.java))
        }
    }
}
