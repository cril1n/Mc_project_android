package com.example.mangiaebasta.datasource

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.mangiaebasta.model.MenuImage
import com.example.mangiaebasta.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseManager(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "image-database"
    ).build()
    private val dao = database.imageDao()


    fun saveImageInDatabase(mid: Int, base64: String, version: Int): String {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                dao.insertImage(MenuImage(mid, base64, version))
            }
            Log.d("StorageManager", "Image saved in database")
            return "Image saved in database"
        } catch (e: Exception) {
            Log.e("StorageManager", "Error saving image in database", e)
        }
        return "Error saving image in database"
    }

    suspend fun getImageFromDatabase(mid: Int): MenuImage? {
        try {
            return withContext(Dispatchers.IO) {
                val image = dao.getImage(mid)
                image
            }
        } catch (e: Exception) {
            Log.e("StorageManager", "Error getting image from database", e)
        }
        return null
    }

    fun resetImagesDb() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.resetImagesDb()
            Log.d("StorageManager", "Images database cleared")
        }
    }
}