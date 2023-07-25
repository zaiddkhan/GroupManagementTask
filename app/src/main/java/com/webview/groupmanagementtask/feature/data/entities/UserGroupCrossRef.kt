package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["name","groupName"])
data class UserGroupCrossRef(
    val name : String,
    @ColumnInfo(index = true)
    val groupName : String
)