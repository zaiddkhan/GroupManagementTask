package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MessagesForGroup(
    @Embedded val groupDto: GroupDto,
    @Relation(
        parentColumn = "groupName",
        entityColumn = "message",
        associateBy = Junction(MessageGroupCrossRef::class)
    )
    val messages : List<MessageDto>
)