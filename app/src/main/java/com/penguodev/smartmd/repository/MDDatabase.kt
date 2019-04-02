package com.penguodev.smartmd.repository

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
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