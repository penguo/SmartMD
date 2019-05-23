package com.penguodev.smartmd.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.penguodev.mdeditor.MdEditorAdapter
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityEditorBinding
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.toolbar.ToolbarExpert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorActivity : AppCompatActivity() {
    companion object {
        fun createActivityIntent(context: Context, documentId: Long?): Intent {
            return Intent(context, EditorActivity::class.java).apply {
                putExtra("documentId", documentId)
            }
        }
    }

    private lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

//        val documentId = intent.getLongExtra("documentId", -1)

        binding.mdEditor.setLifecycleOwner(this)
        binding.mdEditor.notifyDataSetChanged()

        ToolbarExpert(this, binding.mdEditor, binding.editorSectionToolbar)
    }

    inner class ClickHandler {

        fun onClickSave(view: View) {
            saveAndFinish()
        }
    }

    private fun saveAndFinish() {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val currentTime = System.currentTimeMillis()
                ItemDocument(
                    null,
                    "TEST $currentTime",
                    binding.mdEditor.getContent() ?: "",
                    currentTime,
                    currentTime
                ).let {
                    MDDatabase.instance.documentDao.submit(it)
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}