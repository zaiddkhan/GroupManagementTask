package com.webview.groupmanagementtask.feature.data.repository

import com.webview.groupmanagementtask.core.data.room.Database
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.domain.repository.ChatScreenRepository
import javax.inject.Inject

class ChatScreenRepositoryImpl @Inject constructor(
    private val db : Database
) : ChatScreenRepository {

    override suspend fun sendMessage(message: String, sender: String) {
        db.groupDao.insertMessage(MessageDto(message,sender))
    }


    override suspend fun getGroupByGroupName(groupName: String): GroupDto {
       return db.groupDao.getGroupByGroupName(groupName).first()
    }

    override suspend fun insertMessageGroupCrossRef(messageGroupCrossRef: MessageGroupCrossRef) {
        db.groupDao.insertMessageGroupCrossRef(messageGroupCrossRef)
    }

    override suspend fun getMembersForGroup(groupName: String): List<UserDto> {
        val members = db.groupDao.getMemberForGroup(groupName)
        return if(members.isNotEmpty()) members.first().userDto else emptyList()
    }

    override suspend fun getAllMessagesForGroup(groupName: String): List<MessageDto> {
       val messagesForGroup = db.groupDao.getAllMessagesForGroup(groupName)
        return if(messagesForGroup.isNotEmpty()) messagesForGroup.first().messages else emptyList()
    }
}