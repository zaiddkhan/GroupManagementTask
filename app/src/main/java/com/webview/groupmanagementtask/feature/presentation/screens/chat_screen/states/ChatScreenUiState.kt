package com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states

import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.UserDto

data class ChatScreenUiState(
    val isLoading : Boolean = false,
    val group : GroupDto? = null,
    val listMessages : MutableList<MessageDto> = mutableListOf(),
    val members : List<String>? = null,
    val currentlyTypedMessage : String = ""
)
