package com.example.diariolocale

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testo: String
)