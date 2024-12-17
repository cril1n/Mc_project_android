package com.example.mangiaebasta.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mangiaebasta.AppDependencies
import com.example.mangiaebasta.datasource.CommunicationManager
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageRepo {

    private var database: DatabaseManager = AppDependencies.databaseManager
    private var dsManager: DatastoreManager = AppDependencies.dataStoreManager

    fun resetImagesDb() {
        database.resetImagesDb()
    }

    suspend fun getImage(mid: Int = 1): String {
        return withContext(Dispatchers.IO) {
            val menuData = CommunicationManager.getMenuDetail(mid, 46.34, 9.19)
            val imageData = database.getImageFromDatabase(mid)

            if (imageData == null || imageData.version != menuData?.imageVersion) {
                val imageCodeResponse = CommunicationManager.getMenuImage(mid)
                database.saveImageInDatabase(mid, imageCodeResponse!!.base64, menuData!!.imageVersion)
                Log.d("ImageRepo", "New image saved in database")
                imageCodeResponse.base64
            } else {
                Log.d("ImageRepo", "Image already in database")
                imageData.base64
            }
        }
    }
    

}