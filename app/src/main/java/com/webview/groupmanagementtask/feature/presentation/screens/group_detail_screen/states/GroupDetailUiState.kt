package com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states

import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.UserDto

data class GroupDetailUiState(
    val isLoading : Boolean = false,
    val group : GroupDto? = null,
    val newMemberName : String = "",
    val newMemberEmail : String = "",
    val newMemberPhoneNumber : String = "",
    val isAddMemberDialogShown : Boolean = false,
    val isDeleteMemberDialogShown : Boolean = false,
    val userToDelete : UserDto? = null
)
