package com.penguodev.smartmd.ui.editor

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class EditorViewModel : ViewModel() {

    lateinit var manager: EditorManager
//    val focusedLine = MutableLiveData<Int>()

    val text = MutableLiveData<String>()
}
