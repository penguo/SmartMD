package com.penguodev.smartmd.ui.editor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.penguodev.smartmd.R
import com.penguodev.smartmd.common.SoftKeyManager
import com.penguodev.smartmd.databinding.ActivityEditorBinding
import com.penguodev.smartmd.repository.MDDatabase
import kotlinx.coroutines.*
import timber.log.Timber

class EditorActivity : AppCompatActivity() {

    companion object {
        fun createActivityIntent(context: Context, documentId: Long?): Intent {
            return Intent(context, EditorActivity::class.java).apply {
                putExtra("documentId", documentId)
            }
        }
    }

    private lateinit var binding: ActivityEditorBinding
    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        val documentId = intent.getLongExtra("documentId", -1L)

        viewModel = ViewModelProviders.of(this@EditorActivity)
            .get(EditorViewModel::class.java)
            .apply {
                manager =
                    EditorManager(
                        this@apply,
                        binding.editorTvStart,
                        binding.editorEt,
                        binding.editorTvEnd
                    ).apply {
                        if (documentId != -1L) {
                            setItemDocument(documentId)
                        }
                    }
            }.also { binding.viewModel = it }
        EditorHelper(binding.recyclerViewBottom, binding.editorEt, binding.editorBtnOption)
    }

    inner class ClickHandler {
        fun onClickSave(view: View) {
            saveAndFinish()
        }
    }

    private fun saveAndFinish() {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                viewModel.manager.getItemDocument().let {
                    MDDatabase.instance.documentDao.submit(it)
                }
            }
            finish()
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("저장하시겠습니까?")
            .setPositiveButton("저장") { dialog, which ->
                saveAndFinish()
            }
            .setNegativeButton("취소") { dialog, which ->

            }
            .setNeutralButton("종료") { dialog, which ->
                finish()
            }
            .show()
    }
}