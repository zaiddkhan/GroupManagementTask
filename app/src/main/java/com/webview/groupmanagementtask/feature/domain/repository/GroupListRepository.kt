package com.webview.groupmanagementtask.feature.domain.repository

import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import kotlinx.coroutines.flow.Flow

interface GroupListRepository {

     suspend fun getListOfGroupsForCurrentUser(userName: String) : List<GroupDto>

     suspend fun insertCrossRef(userName : String,groupName : String)

     suspend fun insertGroup(groupDto: GroupDto)

     suspend fun insertMessage(messageDto: MessageDto)

     suspend fun insertMessageGroupCrossRef(messageGroupCrossRef: MessageGroupCrossRef)
}