package com.example.diariolocale

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).notaDao()

    val note: Flow<List<Nota>> = dao.getAllNotes()

    var testoCorrente by mutableStateOf("")
        private set

    fun onTestoChange(nuovoTesto: String) {
        testoCorrente = nuovoTesto
    }

    fun salvaNota() {
        val testoPulito = testoCorrente.trim()
        if (testoPulito.isEmpty()) return

        viewModelScope.launch {
            dao.insert(Nota(testo = testoPulito))
            testoCorrente = ""
        }
    }

    fun eliminaNota(nota: Nota) {
        viewModelScope.launch {
            dao.delete(nota)
        }
    }
}