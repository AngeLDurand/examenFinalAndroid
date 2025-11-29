package com.example.levelup.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// 1) Extension property para el DataStore
private val Context.dataStore by preferencesDataStore(name = "levelup_prefs")

object SessionManager {

    private val KEY_TOKEN = stringPreferencesKey("jwt_token")

    // Guardar token (suspend)
    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
        }
    }

    // Obtener token una vez (suspend)
    suspend fun getToken(context: Context): String? {
        val prefs = context.dataStore.data
            .map { it[KEY_TOKEN] }
            .first()
        return prefs
    }

    // Borrar token
    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_TOKEN)
        }
    }
}
