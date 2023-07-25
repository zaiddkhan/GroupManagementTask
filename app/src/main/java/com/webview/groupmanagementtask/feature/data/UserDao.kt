package com.webview.groupmanagementtask.feature.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.webview.groupmanagementtask.feature.data.entities.UserDto

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user : UserDto)

    @Delete
    suspend fun deleteUser(user: UserDto)
}