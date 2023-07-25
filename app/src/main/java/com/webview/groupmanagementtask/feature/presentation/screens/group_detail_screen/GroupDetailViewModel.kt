package com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.data.entities.UserGroupCrossRef
import com.webview.groupmanagementtask.feature.domain.repository.ChatScreenRepository
import com.webview.groupmanagementtask.feature.domain.repository.GroupDetailRepository
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val repo : GroupDetailRepository,
    private val chatRepo : ChatScreenRepository
) : ViewModel() {

    var membersList = mutableStateListOf<UserDto>()
        private set

    private val _groupDetailUiState = MutableStateFlow(GroupDetailUiState())
    val groupDetailUiState = _groupDetailUiState.asStateFlow()

    fun onEvent(event : GroupDetailEvent){

        when(event) {
            is GroupDetailEvent.AddMember -> {
                viewModelScope.launch {
                    async {
                        repo.insertUser(event.userDto)
                    }.await()
                    async {
                        repo.insertGroupMemberCrossRef(UserGroupCrossRef(event.userDto.name,_groupDetailUiState.value.group?.groupName ?: ""))
                    }.await()
                    _groupDetailUiState.update {
                        it.copy(
                            isAddMemberDialogShown = false
                        )
                    }
                    loadMembers(_groupDetailUiState.value.group?.groupName ?: "")
                }
                resetDetails()
            }
            is GroupDetailEvent.DeleteMember -> {
                viewModelScope.launch {
                    async {  repo.deleteUser(event.userDto) }.await()

                    async {
                        repo.deleteUserGroupCrossRef(UserGroupCrossRef(event.userDto.name,_groupDetailUiState.value.group?.groupName ?: ""))
                    }.await()

                    _groupDetailUiState.update {
                        it.copy(
                            isDeleteMemberDialogShown = false
                        )
                    }
                    delay(100)
                    loadMembers(_groupDetailUiState.value.group?.groupName ?: "")
                }

            }

            GroupDetailEvent.ShowAddMemberDialog -> {
                _groupDetailUiState.update {
                    it.copy(
                        isAddMemberDialogShown = true
                    )
                }
            }
            is GroupDetailEvent.AddMemberEmailChanged -> {
                _groupDetailUiState.update {
                    it.copy(
                        newMemberEmail = event.email
                    )
                }
            }
            is GroupDetailEvent.AddMemberNameChanged -> {
                _groupDetailUiState.update {
                    it.copy(
                        newMemberName = event.name
                    )
                }
            }
            is GroupDetailEvent.AddMemberPhoneNumberChanged -> {
                _groupDetailUiState.update {
                    it.copy(
                        newMemberPhoneNumber = event.number
                    )
                }
            }
            GroupDetailEvent.HideAddMemberDialog -> {
                _groupDetailUiState.update {
                    it.copy(
                        isAddMemberDialogShown = false
                    )
                }
                resetDetails()
            }
            GroupDetailEvent.HideDeleteMemberDialog -> {
                _groupDetailUiState.update {
                    it.copy(
                        isDeleteMemberDialogShown = false
                    )
                }
                resetDetails()
            }
            is GroupDetailEvent.ShowDeleteMemberDialog -> {
                _groupDetailUiState.update {
                    it.copy(
                        userToDelete = event.userDto ,
                        isDeleteMemberDialogShown = true,
                    )
                }
            }
        }

    }

    fun loadMembers(groupName : String){
        viewModelScope.launch {
            _groupDetailUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            viewModelScope.launch {
                async {
                    _groupDetailUiState.update {
                        it.copy(
                            group = chatRepo.getGroupByGroupName(groupName)
                        )
                    }
                }.await()
                async {
                    chatRepo.getMembersForGroup(groupName)
                        .forEach {
                            if (!membersList.contains(it)) {
                                membersList.add(it)
                            }
                        }
                }.await()

                _groupDetailUiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun resetDetails(){
        _groupDetailUiState.update {
              it.copy(
                  newMemberPhoneNumber = "",
                  newMemberName = "",
                  newMemberEmail = ""
              )
        }
    }
}