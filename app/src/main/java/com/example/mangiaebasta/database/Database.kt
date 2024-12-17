package com.example.mangiaebasta.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mangiaebasta.model.MenuImage

@Database(entities = [MenuImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}