package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this@EditorActivity)
            .get(EditorViewModel::class.java)
            .apply {
                manager =
                    EditorManager(this@EditorActivity, binding.editorTvStart, binding.editorEt, binding.editorTvEnd)
            }.also { binding.viewModel = it }
    }

}