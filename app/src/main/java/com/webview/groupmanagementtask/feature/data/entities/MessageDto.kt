package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageDto(
    @PrimaryKey
    val message : String,
    val from : String
)
