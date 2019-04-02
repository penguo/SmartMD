package com.penguodev.smartmd.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.MDDatabase

class ListViewModel : ViewModel() {

    val itemList: LiveData<List<ItemDocument>> = MDDatabase.instance.documentDao.getLiveList()
}