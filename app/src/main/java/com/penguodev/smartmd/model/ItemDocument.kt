package com.penguodev.smartmd.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ItemDocument")
data class ItemDocument(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val header: String,
    val text: String,
    val createTime: Long,
    val lastUpdateTime: Long
)