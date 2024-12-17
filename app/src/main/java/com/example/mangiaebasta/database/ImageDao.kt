package com.example.mangiaebasta.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mangiaebasta.model.MenuImage

@Dao
interface ImageDao {
    // Replace: se provo ad inserire un'immagine con la stessa chiave la sostituisce
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: MenuImage)

    @Query("SELECT * FROM MenuImage WHERE mid = :mid")
    suspend fun getImage(mid: Int): MenuImage?

    @Query("SELECT version FROM MenuImage WHERE mid = :mid")
    suspend fun getImageVersion(mid: Int): Int?

    @Query("SELECT base64 FROM MenuImage WHERE mid = :mid")
    suspend fun getImageCode(mid: Int): String?

    @Query("DELETE FROM MenuImage")
    suspend fun resetImagesDb()
}