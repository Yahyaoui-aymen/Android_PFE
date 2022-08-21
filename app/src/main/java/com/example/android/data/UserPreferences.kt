package com.example.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )
    }
    val authToken: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_AUTH]
    }

    val id: Flow<Long?>
        get() = dataStore.data.map { preferences ->
            preferences[ID_AUTH]
        }

    val role: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[ROLE_AUTH]
        }

    suspend fun saveAuthToken(authToken: String, id:Long, role: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
            preferences[ID_AUTH] = id
            preferences[ROLE_AUTH] = role

        }
    }

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val ID_AUTH = preferencesKey<Long>("id_auth")
        private val ROLE_AUTH = preferencesKey<String>("role_auth")

    }
}