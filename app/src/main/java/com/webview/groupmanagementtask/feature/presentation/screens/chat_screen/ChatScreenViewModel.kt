package com.webview.groupmanagementtask.feature.presentation.screens.chat_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.domain.repository.ChatScreenRepository
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states.ChatScreenEvent
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states.ChatScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ViewModelComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
  private val repository: ChatScreenRepository
): ViewModel() {


    private val _chatScreenUiState = MutableStateFlow(ChatScreenUiState())
    val chatScreenUiState = _chatScreenUiState.asStateFlow()

    var messages = mutableStateListOf<MessageDto>()
        private set

    fun onEvent(event: ChatScreenEvent){
        when(event){
            is ChatScreenEvent.SendMessage -> {

                viewModelScope.launch {
                    async {
                        repository.sendMessage(
                            message = _chatScreenUiState.value.currentlyTypedMessage,
                            event.message
                        )
                    }.await()
                    async {
                        repository.insertMessageGroupCrossRef(
                            MessageGroupCrossRef(_chatScreenUiState.value.currentlyTypedMessage,_chatScreenUiState.value.group?.groupName ?: "")
                        )
                    }.await()
                    _chatScreenUiState.update {
                        it.copy(
                            currentlyTypedMessage = ""
                        )
                    }

                    loadDetails(_chatScreenUiState.value.group?.groupName ?: "")

                }


            }
            is ChatScreenEvent.LoadDetails -> {
                 viewModelScope.launch {

                     _chatScreenUiState.update {
                         it.copy(
                             isLoading = true
                         )
                     }
                     async {
                         val group = repository.getGroupByGroupName(event.groupName)
                         _chatScreenUiState.update {
                             it.copy(
                                 group = group
                             )
                         }
                     }.await()

                     async {
                         repository.getAllMessagesForGroup(_chatScreenUiState.value.group?.groupName ?: "").forEach {
                             if(!messages.contains(it)){
                                 messages.add(it)
                             }
                         }
                     }.await()

                     _chatScreenUiState.update {
                         it.copy(
                             listMessages = messages
                         )
                     }
                     async {
                         _chatScreenUiState.update {
                             it.copy(
                                 members = repository.getMembersForGroup(event.groupName).getNames()
                             )
                         }
                     }.await()

                     _chatScreenUiState.update {
                         it.copy(
                             isLoading = false
                         )
                     }


                 }
             }
            is ChatScreenEvent.OnMessageTyped -> {
                _chatScreenUiState.update {
                    it.copy(
                        currentlyTypedMessage = event.message
                    )
                }
            }
        }
    }

    fun loadDetails(groupName : String){
        viewModelScope.launch {

            _chatScreenUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            async {
                val group = repository.getGroupByGroupName(groupName)
                _chatScreenUiState.update {
                    it.copy(
                        group = group
                    )
                }
            }.await()

            async {
                repository.getAllMessagesForGroup(_chatScreenUiState.value.group?.groupName ?: "").forEach {
                    if(!messages.contains(it)){
                        messages.add(it)
                    }
                }
            }.await()

            _chatScreenUiState.update {
                it.copy(
                    listMessages = messages
                )
            }
            async {
                _chatScreenUiState.update {
                    it.copy(
                        members = repository.getMembersForGroup(groupName).getNames()
                    )
                }
            }.await()

            _chatScreenUiState.update {
                it.copy(
                    isLoading = false
                )
            }


        }
    }

    private fun  List<UserDto>.getNames() : List<String> {
        return this.map {
            it.name
        }
    }
}