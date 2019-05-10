package com.penguodev.smartmd.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityEditor2Binding
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xute.markdeditor.Styles.TextComponentStyle.NORMAL

class EditorActivity : AppCompatActivity() {
    companion object {
        fun createActivityIntent(context: Context, documentId: Long?): Intent {
            return Intent(context, EditorActivity::class.java).apply {
                putExtra("documentId", documentId)
            }
        }
    }

    private lateinit var binding: ActivityEditor2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor2)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        binding.mdEditor.configureEditor(
            "",
            "",
            false,
            "Input Here...",
            NORMAL
        )
        binding.controlBar.setEditor(binding.mdEditor)
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
                    binding.mdEditor.markdownContent,
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