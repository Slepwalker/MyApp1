package com.example.myapp1.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseItem::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun courseItemDao(): CourseItemDAO
    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "producto_db"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }
}