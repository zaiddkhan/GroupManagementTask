package com.webview.groupmanagementtask.feature.domain.repository

import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserDto

interface ChatScreenRepository {

    suspend fun sendMessage(message : String,sender : String)

    suspend fun getAllMessagesForGroup(groupName : String) : List<MessageDto>

    suspend fun getGroupByGroupName(groupName: String) : GroupDto

    suspend fun getMembersForGroup(groupName: String) : List<UserDto>

    suspend fun insertMessageGroupCrossRef(messageGroupCrossRef: MessageGroupCrossRef)
}