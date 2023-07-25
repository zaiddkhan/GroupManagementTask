package com.webview.groupmanagementtask.feature.domain.repository

import com.webview.groupmanagementtask.feature.data.entities.UserDto
import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    suspend fun setName(name : String)

    fun getName() : Flow<String>

    suspend fun setUserRegistered(saved : Boolean)


    suspend fun insertUser(userDto: UserDto)
}