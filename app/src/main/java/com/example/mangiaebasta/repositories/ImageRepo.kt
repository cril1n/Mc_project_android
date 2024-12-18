package com.example.mangiaebasta.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mangiaebasta.AppDependencies
import com.example.mangiaebasta.datasource.CommunicationManager
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import com.example.mangiaebasta.model.Menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageRepo {

    private var database: DatabaseManager = AppDependencies.databaseManager

    fun resetImagesDb() {
        database.resetImagesDb()
    }

    suspend fun getImage(menu: Menu): String {
        return withContext(Dispatchers.IO) {
            Log.d("ImageRepo", "Getting image for menu: $menu")
            val imageData = database.getImageFromDatabase(menu.mid)
            if (imageData == null || imageData.version != menu.imageVersion) {
                val imageCodeResponse = CommunicationManager.getMenuImage(menu.mid)
                database.saveImageInDatabase(menu.mid, imageCodeResponse!!.base64, menu.imageVersion)
                Log.d("ImageRepo", "New image saved in database")
                imageCodeResponse.base64
            } else {
                Log.d("ImageRepo", "Image already in database")
                imageData.base64
            }
        }
    }
    

}