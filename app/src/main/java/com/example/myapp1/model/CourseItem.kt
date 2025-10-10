package com.example.myapp1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CourseItem(
    val id: String = UUID.randomUUID().toString(),
    val imageRes: Int? = null,
    val title: String,
    val description: String,
    val price: String,
    val stock: String,
    val imageUri: String? = null
) : Parcelable