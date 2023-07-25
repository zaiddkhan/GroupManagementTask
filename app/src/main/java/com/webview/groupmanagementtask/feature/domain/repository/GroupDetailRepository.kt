package com.webview.groupmanagementtask.feature.domain.repository

import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef

interface GroupDetailRepository {

    suspend fun insertUser(userDto: UserDto)

    suspend fun deleteUser(userDto: UserDto)

    suspend fun insertGroupMemberCrossRef(userGroupCrossRef : UserGroupCrossRef)

    suspend fun deleteUserGroupCrossRef(userGroupCrossRef: UserGroupCrossRef)
}