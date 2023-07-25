package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["message","groupName"])
data class MessageGroupCrossRef(
    val message : String,
    @ColumnInfo(index = true)
    val groupName : String
)