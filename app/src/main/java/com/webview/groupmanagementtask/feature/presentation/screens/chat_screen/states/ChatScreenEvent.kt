package com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states

interface ChatScreenEvent {

    data class SendMessage(val message : String) : ChatScreenEvent

    data class LoadDetails(val groupName : String) : ChatScreenEvent

    data class OnMessageTyped(val message : String) : ChatScreenEvent

}