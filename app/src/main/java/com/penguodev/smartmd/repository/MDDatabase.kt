package com.penguodev.smartmd.repository

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.penguodev.smartmd.MDApplication
import com.penguodev.smartmd.model.ItemDocument
import com.penguodev.smartmd.repository.dao.DocumentDao


private const val DB_NAME = "SmartMD.db"
private const val DB_VERSION = 1

@Database(entities = [ItemDocument::class], version = DB_VERSION)
abstract class MDDatabase : RoomDatabase() {
    abstract val documentDao: DocumentDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(MDApplication.appContext, MDDatabase::class.java, DB_NAME)
                .build()
        }
    }

}