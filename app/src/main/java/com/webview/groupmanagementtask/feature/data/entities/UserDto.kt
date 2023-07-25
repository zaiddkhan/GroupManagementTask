package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDto(
    @PrimaryKey(autoGenerate = false)
    val name : String,
    val email : String,
    val phoneNumber : String
)
