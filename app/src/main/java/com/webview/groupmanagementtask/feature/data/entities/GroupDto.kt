package com.webview.groupmanagementtask.feature.data.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDto(

    @PrimaryKey(autoGenerate = false)
    val groupName : String,
    val groupDescription : String,
    val maxMembers : Int = 10,
    val groupIcon : String? = null

)