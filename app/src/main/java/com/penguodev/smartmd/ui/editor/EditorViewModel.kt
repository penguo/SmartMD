package com.penguodev.smartmd.ui.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {

    lateinit var manager: EditorManager
//    val focusedLine = MutableLiveData<Int>()

    val text = MutableLiveData<String>()
}
