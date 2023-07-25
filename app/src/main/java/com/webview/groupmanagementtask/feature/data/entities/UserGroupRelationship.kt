package com.webview.groupmanagementtask.feature.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class UserWithGroups(

    @Embedded val userDto: UserDto,
    @Relation(
        parentColumn = "name",
        entityColumn = "groupName",
        associateBy = Junction(UserGroupCrossRef::class)
    )
    val groups : List<GroupDto>

)

data class GroupWithUsers(
    @Embedded val groupDto: GroupDto,
    @Relation(
        parentColumn = "groupName",
        entityColumn = "name",
        associateBy = Junction(UserGroupCrossRef::class)
    )
    val userDto : List<UserDto>
)