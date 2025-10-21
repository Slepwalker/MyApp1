package com.example.myapp1.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "productos")
data class CourseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageRes: Int? = null,
    val title: String,
    val description: String,
    val price: String,
    val stock: String,
    val imageUri: String? = null
): Parcelable