package com.penguodev.smartmd.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.penguodev.smartmd.MDApplication.Companion.RC_PICK_IMAGE
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityEditorBinding
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.toolbar.ToolbarManager
import com.penguodev.smartmd.ui.editor.toolbar.ToolbarType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import com.penguodev.smartmd.common.ComfyUtil
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        val documentId = intent.getLongExtra("documentId", -1)

        binding.mdEditor.setLifecycleOwner(this)
        binding.mdEditor.notifyDataSetChanged()

        ToolbarManager(this, binding.mdEditor, binding.editorSectionToolbar).apply {
            attachToolbar(ToolbarType.FAST)
            pickImageFunction = ::pickImage
        }
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
                    binding.mdEditor.getContent(),
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

    fun pickImage() {
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }.let {
            startActivityForResult(Intent.createChooser(it, "Select Picture"), RC_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_PICK_IMAGE -> {
                    if (data == null || data.data == null) {
                        Toast.makeText(this@EditorActivity, "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                        Timber.e("error occur.")
                        return
                    } else {
                        val uri: Uri = data.data!!
                        val fileName = uri.toString().split("/").last()
                        ComfyUtil.savefile(this@EditorActivity, uri).also {
                            binding.mdEditor.adapter?.addCurrentItemText("![$fileName]($it)")
                        }
                    }
                }
            }
        }
    }
}