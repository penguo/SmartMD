package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class EditorViewModel : ViewModel() {

    val editorManager = EditorManager()
    val focusedLine = MutableLiveData<Int>()
}
