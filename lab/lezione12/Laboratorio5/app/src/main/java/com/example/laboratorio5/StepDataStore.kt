package com.example.laboratorio5

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore(name = "step_prefs")

class StepDataStore(private val context: Context) {


    companion object {
        val TOTAL_STEPS_KEY = intPreferencesKey("total_steps")
    }
    val totalStepsFlow: Flow<Int> = context.dataStore.data.map { preferences: Preferences ->
        // preferences[TOTAL_STEPS_KEY] cerca nel cassetto: se è vuoto (?:) restituisce 0
        preferences[TOTAL_STEPS_KEY] ?: 0
    }


    suspend fun saveSteps(steps: Int) {

        context.dataStore.edit { preferences ->
            preferences[TOTAL_STEPS_KEY] = steps
        }
    }
}