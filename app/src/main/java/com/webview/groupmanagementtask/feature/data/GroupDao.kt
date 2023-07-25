package com.webview.groupmanagementtask.feature.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.GroupWithUsers
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.MessagesForGroup
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserWithGroups

@Dao
interface GroupDao {

    @Transaction
    @Query("Select * from userdto where name = :userName")
    suspend fun getGroupsForCurrentUser(userName : String) : List<UserWithGroups>

    @Insert
    suspend fun insertUserGroupRelation(userGroupCrossRef : UserGroupCrossRef)

    @Insert
    suspend fun insertGroup(groupDto: GroupDto)

    @Insert
    suspend fun insertMessage(messageDto: MessageDto)

    @Insert
    suspend fun insertMessageGroupCrossRef(messageGroupCrossRef: MessageGroupCrossRef)

    @Transaction
    @Query("SELECT * FROM groupdto where groupName = :groupName")
    suspend fun getAllMessagesForGroup(groupName : String) : List<MessagesForGroup>

    @Query("SELECT * FROM groupdto where groupName = :groupName")
    suspend fun getGroupByGroupName(groupName : String) : List<GroupDto>

    @Transaction
    @Query("SELECT * from groupdto where groupName = :groupName")
    suspend fun getMemberForGroup(groupName: String) : List<GroupWithUsers>

    @Delete
    suspend fun deleteUserGroupCrossRef(userGroupCrossRef: UserGroupCrossRef)
}
