package com.example.mangiaebasta.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatastoreManager(private val dataStore: DataStore<Preferences>)  {
    private val SID_KEY = stringPreferencesKey("sid_key")

    fun setSidInDataStore(sid: String?) {
        if (sid == null) return
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit { preferences ->
                preferences[SID_KEY] = sid
                val savedSid = preferences[SID_KEY]
                Log.d("StorageManager", "New saved sid: $savedSid")
            }
        }
    }

    suspend fun getSidFromDataStore(): String? {
        Log.d("StorageManager", "Getting sid from data store")
        return withContext(Dispatchers.IO) {
            val preferences = dataStore.data.first()
            preferences[SID_KEY]
        }
    }

    val LASTSCREEN_KEY = stringPreferencesKey("LastScreen_key")

    fun setLastScreenInDataStore(lastScreen: String?) {
        if (lastScreen == null) return
        CoroutineScope(Dispatchers.Main).launch {
            dataStore?.edit { preferences ->
                preferences[LASTSCREEN_KEY] = lastScreen
                val savedLastScreen = preferences[LASTSCREEN_KEY]
                Log.d("DataStoreManager", "New saved screen: $savedLastScreen")
            }
        }
    }

    suspend fun getLastScreenFromDataStore(): String? {
        return withContext(Dispatchers.IO) {
            val preferences = dataStore?.data?.first()
            preferences?.get(LASTSCREEN_KEY)
        }
    }

    fun clearDataStore() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences.clear() // Rimuove tutte le coppie chiave-valore
            }
            Log.d("StorageManager", "Data store cleared")
        }
    }
}