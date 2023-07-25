package com.webview.groupmanagementtask.feature.data.repository

import com.webview.groupmanagementtask.core.data.room.Database
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef
import com.webview.groupmanagementtask.feature.domain.repository.GroupDetailRepository
import javax.inject.Inject

class GroupDetailRepositoryImpl @Inject constructor(
    private val db : Database
) : GroupDetailRepository {
    override suspend fun insertUser(userDto: UserDto) {
        db.userDao.insertUser(userDto)
    }

    override suspend fun deleteUser(userDto: UserDto) {
       db.userDao.deleteUser(userDto)
    }

    override suspend fun insertGroupMemberCrossRef(userGroupCrossRef: UserGroupCrossRef) {
        db.groupDao.insertUserGroupRelation(userGroupCrossRef)
    }

    override suspend fun deleteUserGroupCrossRef(userGroupCrossRef: UserGroupCrossRef){
        db.groupDao.deleteUserGroupCrossRef(userGroupCrossRef)
    }
}