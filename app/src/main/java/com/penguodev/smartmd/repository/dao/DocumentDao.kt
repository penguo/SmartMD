package com.penguodev.smartmd.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.penguodev.smartmd.model.ItemDocument

@Dao
interface DocumentDao {

    @Insert(onConflict = REPLACE)
    fun submit(data: ItemDocument)

    @Delete
    fun delete(data: ItemDocument)

    @Query("SELECT * FROM ItemDocument WHERE id = :id")
    fun getItem(id: Long): ItemDocument?

    @Query("SELECT * FROM ItemDocument ORDER BY lastUpdateTime DESC")
    fun getList(): List<ItemDocument>

    @Query("SELECT * FROM ItemDocument ORDER BY lastUpdateTime DESC")
    fun getLiveList(): LiveData<List<ItemDocument>>

}