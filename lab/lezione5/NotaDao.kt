package com.example.diariolocale

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {

    @Insert
    suspend fun insert(nota: Nota)

    @Delete
    suspend fun delete(nota: Nota)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Nota>>
}