package com.example.ecommerce.core.database.data.dao.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.image.ImageEntity

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("SELECT * FROM image")
    suspend fun getAllImage(): List<ImageEntity>

    @Query ("select * from image where productId in (:productsId)")
    suspend fun getImagesByProductId(productsId: List<Int>) : List<ImageEntity>

    @Query("delete from image")
    suspend fun delete()
}