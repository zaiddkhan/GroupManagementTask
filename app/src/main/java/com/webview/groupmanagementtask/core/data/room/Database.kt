package com.webview.groupmanagementtask.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.webview.groupmanagementtask.feature.data.GroupDao
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.data.UserDao
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef

@Database(
    entities = [
        UserDto::class,
        GroupDto::class,
        UserGroupCrossRef::class,
        MessageDto::class,
        MessageGroupCrossRef::class
    ],
    version = 3, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract val userDao : UserDao

    abstract val groupDao : GroupDao

}