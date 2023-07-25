package com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state

import com.webview.groupmanagementtask.feature.data.entities.GroupDto

data class GroupListScreenUiState(
    val groups : List<GroupDto>? = null,
    val newGroupName : String = "",
    val newGroupMaxMembers : Int = 10,
    val newGroupIconByteArray : ByteArray? = null,
    val isError : Boolean = true,
    val newGroupDesc : String = "",
    val message : String = "",
    val selectedGroups : MutableList<GroupDto> = mutableListOf(),
    val messageSendTime : AlarmTime = AlarmTime.TenSeconds,
    val isMessageValid : Boolean = false,
    val isWorkerStared : Boolean = true,
    val isSendMessageDialogShown : Boolean = false,
    val isSetAlarmDialogShown : Boolean = false,
    val reminderAlarmTime : ReminderTime = ReminderTime.FifteenMinutes
)

enum class AlarmTime(val value : String){
    TenSeconds("10 seconds"),
    FiveMinutes("5 Minutes"),
    TenMinutes("10 Minutes"),
    TwentyMinutes("20 Minutes"),
    ThirtyMinutes("30 Minutes"),
    OneHour("1 Hour")
}

enum class ReminderTime(val value: String){
    FifteenMinutes("15 Minutes"),
    ThirtyMinutes("30 Minutes"),
    OneHour("1 Hour"),
    TwoHours("2 Hours")

}