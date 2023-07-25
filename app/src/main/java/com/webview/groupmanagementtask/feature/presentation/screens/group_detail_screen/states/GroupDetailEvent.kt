package com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states

import com.webview.groupmanagementtask.feature.data.entities.UserDto

sealed interface GroupDetailEvent{

    data class AddMember(val userDto: UserDto) : GroupDetailEvent

    data class DeleteMember(val userDto: UserDto) : GroupDetailEvent

    object ShowAddMemberDialog : GroupDetailEvent

    object HideAddMemberDialog : GroupDetailEvent

    data class AddMemberNameChanged(val name : String) : GroupDetailEvent

    data class AddMemberPhoneNumberChanged(val number : String) : GroupDetailEvent

    data class AddMemberEmailChanged(val email : String) : GroupDetailEvent

    data class ShowDeleteMemberDialog(val userDto: UserDto) : GroupDetailEvent

    object HideDeleteMemberDialog : GroupDetailEvent

}