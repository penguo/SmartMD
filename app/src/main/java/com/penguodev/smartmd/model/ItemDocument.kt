package com.penguodev.smartmd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemDocument")
data class ItemDocument(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val header: String,
    val text: String,
    val createTime: Long,
    val lastUpdateTime: Long
)