package com.hivian.randomusers.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Локальный ID
    val remoteId: String, // ID с сервера (uuid)
    val gender: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val cell: String,
    val pictureLarge: String,
    val pictureMedium: String,
    val pictureThumbnail: String,
    val nat: String,
    val city: String,
    val country: String,
    val latitude: String,
    val longitude: String
)