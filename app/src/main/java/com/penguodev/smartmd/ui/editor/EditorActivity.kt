package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var adapter: EditorListAdapter
    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)

        viewModel = ViewModelProviders.of(this@EditorActivity)
            .get(EditorViewModel::class.java)
        adapter = EditorListAdapter(viewModel).also {
            binding.recyclerView.adapter = it
        }
        viewModel.apply {
            focusedLine.observe(this@EditorActivity, Observer {
                adapter.notifyFocusChanged(it)
            })
            editorManager.submitDocument("Hello\nMy Name is Jiho\nNice to meet you!")
        }
    }
}