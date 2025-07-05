package com.example.digitaldiary.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String = "",
    val text: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val imageUrl: String? = null,
    val audioUrl: String? = null
)
