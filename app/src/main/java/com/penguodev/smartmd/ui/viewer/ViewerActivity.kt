package com.penguodev.smartmd.ui.viewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.penguodev.smartmd.MDApplication.Companion.RC_VIEWER_TO_EDITOR
import com.penguodev.smartmd.R
import com.penguodev.smartmd.databinding.ActivityViewerBinding
import com.penguodev.smartmd.repository.MDDatabase
import com.penguodev.smartmd.ui.editor.EditorActivity

class ViewerActivity : AppCompatActivity() {

    companion object {

        fun createActivityIntent(context: Context, documentId: Long?): Intent {
            return Intent(context, ViewerActivity::class.java).apply {
                putExtra("documentId", documentId)
            }
        }
    }

    private lateinit var binding: ActivityViewerBinding
    private lateinit var viewModel: ViewerViewModel
    private var documentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewer)
        binding.lifecycleOwner = this
        binding.clickHandler = ClickHandler()

        documentId = intent.getLongExtra("documentId", -1L)
        viewModel = ViewModelProviders.of(this, ViewerViewModel.Factory(documentId))
            .get(ViewerViewModel::class.java)
            .apply {
                item.observe(this@ViewerActivity, Observer {
                    binding.mdEditor.editable = false
                    binding.mdEditor.setContent(it.text)
                    binding.item = it
                })
            }
    }

    inner class ClickHandler {
        fun onClickEdit(view: View) {
            startActivityForResult(EditorActivity.createActivityIntent(view.context, documentId), RC_VIEWER_TO_EDITOR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_VIEWER_TO_EDITOR && resultCode == Activity.RESULT_OK) {

        }
    }

}

class ViewerViewModel(private val documentId: Long) : ViewModel() {
    val item = MDDatabase.instance.documentDao.getLiveItem(documentId)

    class Factory(private val documentId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ViewerViewModel(documentId) as T
        }
    }
}