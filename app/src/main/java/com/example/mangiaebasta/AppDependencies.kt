package com.example.mangiaebasta

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import com.example.mangiaebasta.datasource.LocationManager

object AppDependencies {
    lateinit var databaseManager: DatabaseManager
    lateinit var dataStoreManager: DatastoreManager
    lateinit var locationManager: LocationManager

    fun init(context: Context, dataStore: DataStore<Preferences>) {
        databaseManager = DatabaseManager(context)
        dataStoreManager = DatastoreManager(dataStore)
        locationManager = LocationManager(context)
    }
}