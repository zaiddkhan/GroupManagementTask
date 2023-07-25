package com.webview.groupmanagementtask.feature.data.repository

import com.webview.groupmanagementtask.core.data.room.Database
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef
import com.webview.groupmanagementtask.feature.domain.repository.GroupListRepository
import javax.inject.Inject

class GroupListRepositoryImpl @Inject constructor(
    private val db : Database
) : GroupListRepository {

    override suspend fun getListOfGroupsForCurrentUser(userName: String): List<GroupDto> {
        val userWithGroups = db.groupDao.getGroupsForCurrentUser(userName = userName)
        return if(userWithGroups.isNotEmpty()) userWithGroups.first().groups else emptyList()

    }

    override suspend fun insertCrossRef(userName: String, groupName: String) {
        db.groupDao.insertUserGroupRelation(UserGroupCrossRef(userName,groupName))
    }

    override suspend fun insertGroup(groupDto: GroupDto) {
        db.groupDao.insertGroup(groupDto)
    }

    override suspend fun insertMessage(messageDto: MessageDto) {
        db.groupDao.insertMessage(messageDto)
    }

    override suspend fun insertMessageGroupCrossRef(messageGroupCrossRef: MessageGroupCrossRef) {
        db.groupDao.insertMessageGroupCrossRef(messageGroupCrossRef = messageGroupCrossRef)
    }
}