package com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state

import com.webview.groupmanagementtask.feature.data.GroupDao
import com.webview.groupmanagementtask.feature.data.entities.GroupDto

sealed interface GroupScreenEvent{

    data class GroupNameChanged(val name : String) : GroupScreenEvent

    data class GroupMaxMembersChanged(val members : Int) : GroupScreenEvent

    data class GroupIconChanged(val byteArray : ByteArray) : GroupScreenEvent

    data class GroupDescChanged(val desc : String) : GroupScreenEvent

    object GroupCreated : GroupScreenEvent

    data class MessageEntered(val message : String) : GroupScreenEvent

    data class GroupSelected(val groupDto: GroupDto) : GroupScreenEvent

    data class TimeSelected(val time : AlarmTime) : GroupScreenEvent

    object SendMessage : GroupScreenEvent

    object ShowSendMessageDialog : GroupScreenEvent

    object HideSendMessageDialog : GroupScreenEvent

    object ShowSetAlarmDialog : GroupScreenEvent

    object HideSetAlarmDialog : GroupScreenEvent

    data class SetReminderAlarmTime(val time : ReminderTime) : GroupScreenEvent

    object SetReminder : GroupScreenEvent


}