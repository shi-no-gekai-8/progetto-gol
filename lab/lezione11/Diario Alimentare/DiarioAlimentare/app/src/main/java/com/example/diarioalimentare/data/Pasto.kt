package com.example.diarioalimentare.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasti")
data class Pasto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val calorie: Int,
    val categoria: String
)