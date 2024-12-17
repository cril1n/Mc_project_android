package com.example.mangiaebasta

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager

object AppDependencies {
    lateinit var databaseManager: DatabaseManager

    lateinit var dataStoreManager: DatastoreManager



    fun init(context: Context, dataStore: DataStore<Preferences>) {
        databaseManager = DatabaseManager(context)
        dataStoreManager = DatastoreManager(dataStore)
    }
}