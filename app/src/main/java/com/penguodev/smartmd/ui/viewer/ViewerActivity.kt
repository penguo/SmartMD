package com.penguodev.smartmd.ui.viewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityViewerBinding
import com.penguodev.smartmd.ui.editor.EditorActivity

class ViewerActivity : AppCompatActivity() {

    companion object {

        const val RC_VIEWER_TO_EDITOR = 1001

        fun createActivityIntent(context: Context, documentId: Long?): Intent {
            return Intent(context, ViewerActivity::class.java).apply {
                putExtra("documentId", documentId)
            }
        }
    }

    private lateinit var binding: ActivityViewerBinding
    private lateinit var viewerManager: ViewerManager
    private var documentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewer)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        documentId = intent.getLongExtra("documentId", -1L)

        viewerManager = ViewerManager(binding.viewerTv)
            .apply {
                if (documentId != -1L) {
                    setItemDocument(documentId)
                }
            }
        binding.viewerTv.movementMethod = ScrollingMovementMethod.getInstance()
    }

    inner class ClickHandler {
        fun onClickEdit(view: View) {
            startActivityForResult(EditorActivity.createActivityIntent(view.context, documentId), RC_VIEWER_TO_EDITOR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_VIEWER_TO_EDITOR && resultCode == Activity.RESULT_OK) {
            viewerManager.setItemDocument(documentId)
        }
    }

}