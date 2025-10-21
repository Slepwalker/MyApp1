package com.example.myapp1.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface CourseItemDAO {

    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<CourseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(courseItem: CourseItem): Long

    @Update
    suspend fun update(courseItem: CourseItem)

    @Delete
    suspend fun delete(courseItem: CourseItem)
}