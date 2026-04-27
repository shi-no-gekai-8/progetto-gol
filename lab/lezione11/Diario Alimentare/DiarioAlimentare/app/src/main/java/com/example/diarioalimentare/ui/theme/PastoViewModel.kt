package com.example.diarioalimentare.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarioalimentare.data.AppDatabase
import com.example.diarioalimentare.data.Pasto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PastoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).pastoDao()

    val pasti = dao.getAllPasti()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun inserisciPasto(nome: String, calorie: Int, categoria: String) {
        viewModelScope.launch {
            dao.insert(
                Pasto(
                    nome = nome,
                    calorie = calorie,
                    categoria = categoria
                )
            )
        }
    }


    fun eliminaPasto(pasto: Pasto){
        viewModelScope.launch {
            dao.delete(pasto)
        }
    }
}