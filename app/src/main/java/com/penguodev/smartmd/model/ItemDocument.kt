package com.penguodev.smartmd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemDocument")
data class ItemDocument(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var header: String?,
    var text: String,
    val createTime: Long,
    var lastUpdateTime: Long
)