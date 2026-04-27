package com.example.diarioalimentare.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PastoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pasto: Pasto)

    @Query("SELECT * FROM pasti ORDER BY id DESC")
    fun getAllPasti(): Flow<List<Pasto>>

    @Delete
    suspend fun delete(pasto: Pasto)
}