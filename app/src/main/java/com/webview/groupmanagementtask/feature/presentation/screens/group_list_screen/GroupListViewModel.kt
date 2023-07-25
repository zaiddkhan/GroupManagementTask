package com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen




import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.domain.repository.GroupListRepository
import com.webview.groupmanagementtask.feature.domain.repository.UserDataStore
import com.webview.groupmanagementtask.feature.domain.use_case.GroupValidationType
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateGroupDetailsUseCase
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateMessageUseCase
import com.webview.groupmanagementtask.feature.domain.worker.SchedulingMessageWorker
import com.webview.groupmanagementtask.feature.domain.worker.SendReminderWorker
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.AlarmTime
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupListScreenUiState
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.ReminderTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupListRepository: GroupListRepository,
    private val userDataStore: UserDataStore,
    private val groupValidateUseCase : ValidateGroupDetailsUseCase,
    private val validateMessageUseCase: ValidateMessageUseCase,
    private val workManager: WorkManager
): ViewModel() {

    lateinit  var builder : NotificationCompat.Builder



    private val _snackMessage = MutableSharedFlow<String>()
    val snackMessage = _snackMessage.asSharedFlow()

    val selectedGroups = mutableStateListOf<GroupDto>()




    private val _groupListUiState = MutableStateFlow(GroupListScreenUiState())
    val groupListUiState = _groupListUiState.asStateFlow()

    val registeredUserName : StateFlow<String> = userDataStore.getName().filter {
        it.isNotEmpty()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(4_000),"")

    var groups = mutableStateListOf<GroupDto>()
        private set




    fun loadGroups(){
        viewModelScope.launch {
            groupListRepository.getListOfGroupsForCurrentUser(registeredUserName.value).forEach{
                if(!groups.contains(it)){
                    groups.add(it)
                }
            }
        }
    }


    fun onEvent(event: GroupScreenEvent){
        when(event){
            GroupScreenEvent.GroupCreated -> {
                viewModelScope.launch {
                   async {
                       groupListRepository.insertGroup(
                           GroupDto(
                               groupName = _groupListUiState.value.newGroupName,
                               maxMembers = _groupListUiState.value.newGroupMaxMembers,
                               groupIcon = _groupListUiState.value.newGroupIconByteArray.toString(),
                               groupDescription = _groupListUiState.value.newGroupDesc
                           )
                       )
                   }.await()
                   async {

                       groupListRepository
                           .insertCrossRef(
                               registeredUserName.value ?: "",
                               _groupListUiState.value.newGroupName
                           )
                   }.await()
                    loadGroups()
                }

            }
            is GroupScreenEvent.GroupIconChanged -> {
                _groupListUiState.update {
                    it.copy(
                        newGroupIconByteArray = event.byteArray
                    )
                }
            }
            is GroupScreenEvent.GroupMaxMembersChanged -> {
                _groupListUiState.update {
                    it.copy(
                        newGroupMaxMembers = event.members
                    )
                }
            }
            is GroupScreenEvent.GroupDescChanged -> {
                _groupListUiState.update {
                    it.copy(
                        newGroupDesc = event.desc
                    )
                }
            }
            is GroupScreenEvent.GroupNameChanged -> {
                _groupListUiState.update {
                    it.copy(
                        newGroupName = event.name
                    )
                }
                setError(_groupListUiState.value.newGroupName)
            }

            is GroupScreenEvent.GroupSelected -> {

                selectedGroups.add(event.groupDto)
                val groups = _groupListUiState.value.selectedGroups
                if(!groups.contains(event.groupDto)){
                    groups.add(event.groupDto)
                }
                _groupListUiState.update {
                    it.copy(
                        selectedGroups = groups
                    )
                }
                if( checkErrorForMessage(_groupListUiState.value.message,selectedGroups)){
                    _groupListUiState.update {
                        it.copy(
                            isMessageValid = true
                        )
                    }
                }
            }

            is GroupScreenEvent.MessageEntered -> {
                _groupListUiState.update {
                    it.copy(
                        message = event.message
                    )
                }
               if( checkErrorForMessage(_groupListUiState.value.message,_groupListUiState.value.selectedGroups)){
                   _groupListUiState.update {
                       it.copy(
                           isMessageValid = true
                       )
                   }
               }
            }
            is GroupScreenEvent.TimeSelected -> {
                _groupListUiState.update {
                    it.copy(
                        messageSendTime = event.time
                    )
                }

            }
             GroupScreenEvent.SendMessage -> {
                 val dataUploadRequest = OneTimeWorkRequestBuilder<SchedulingMessageWorker>()
                     .setInputData(
                         createInputDataForUri(
                             _groupListUiState.value.message,
                             registeredUserName.value,
                             selectedGroups,
                             _groupListUiState.value.messageSendTime
                         )
                     )
                     .build()
                  workManager.enqueue(dataUploadRequest)
                 _groupListUiState.update {
                     it.copy(
                         isSendMessageDialogShown = false
                     )
                 }
                 showSnackbar("Your message has been scheduled")
             }
            GroupScreenEvent.ShowSendMessageDialog -> {
                _groupListUiState.update {
                    it.copy(
                        isSendMessageDialogShown = true
                    )
                }
            }
            GroupScreenEvent.HideSendMessageDialog -> {
                _groupListUiState.update {
                    it.copy(
                        isSendMessageDialogShown = false
                    )
                }
            }
            GroupScreenEvent.ShowSetAlarmDialog -> {
                _groupListUiState.update {
                    it.copy(
                        isSetAlarmDialogShown = true
                    )
                }
            }
            GroupScreenEvent.HideSetAlarmDialog -> {
                _groupListUiState.update {
                    it.copy(
                        isSetAlarmDialogShown = false
                    )
                }
            }

            is GroupScreenEvent.SetReminderAlarmTime -> {
                _groupListUiState.update {
                    it.copy(
                        reminderAlarmTime = event.time
                    )
                }
            }
            GroupScreenEvent.SetReminder -> {
                when(_groupListUiState.value.reminderAlarmTime){
                    ReminderTime.FifteenMinutes -> {

                        val notificationRequest = PeriodicWorkRequestBuilder<SendReminderWorker>(15, TimeUnit.MINUTES).build()
                        workManager.enqueueUniquePeriodicWork("send_message",ExistingPeriodicWorkPolicy.REPLACE,notificationRequest)
                    }
                    ReminderTime.ThirtyMinutes -> {
                        val notificationRequest = PeriodicWorkRequestBuilder<SendReminderWorker>(30, TimeUnit.MINUTES).build()
                        workManager.enqueueUniquePeriodicWork("send_message",ExistingPeriodicWorkPolicy.REPLACE,notificationRequest)


                    }
                    ReminderTime.OneHour -> {
                        val notificationRequest = PeriodicWorkRequestBuilder<SendReminderWorker>(1, TimeUnit.HOURS).build()
                        workManager.enqueueUniquePeriodicWork("send_message",ExistingPeriodicWorkPolicy.REPLACE,notificationRequest)


                    }
                    ReminderTime.TwoHours -> {

                        val notificationRequest = PeriodicWorkRequestBuilder<SendReminderWorker>(2, TimeUnit.HOURS).build()
                        workManager.enqueueUniquePeriodicWork("send_message",ExistingPeriodicWorkPolicy.REPLACE,notificationRequest)


                    }
                }
                _groupListUiState.update {
                    it.copy(
                        isSetAlarmDialogShown = false
                    )
                }
                showSnackbar("Reminder Scheduled")
            }
        }
    }

    private fun showSnackbar(message : String){
        viewModelScope.launch {
            _snackMessage.emit(message)
        }
    }

    private fun setError(groupName:String){

        when(groupValidateUseCase(groupName)) {

            GroupValidationType.VALID -> {
                _groupListUiState.update {
                    it.copy(
                        isError = false
                    )
                }
            }
            GroupValidationType.INVALID -> {
                _groupListUiState.update {
                    it.copy(
                        isError = true
                    )
                }
            }
        }
    }


    private fun checkErrorForMessage(message : String,list : List<GroupDto>) : Boolean{
        return validateMessageUseCase(message,list)
    }

    private fun createInputDataForUri(message: String,sender:String,list: List<GroupDto>,time:AlarmTime): Data {
        val builder = Data.Builder()
        builder.putString("MESSAGE",message)
        builder.putString("SENDER",sender)
        builder.putLong("TIME",convertToMilliSeconds(time))
        val groupNames = mutableListOf<String>()
        list.forEach {
            groupNames.add(it.groupName)
        }
        builder.putString("GROUPS",groupNames.joinToString())
        return builder.build()
    }

    private fun convertToMilliSeconds(time : AlarmTime) : Long{
        return when(time){
            AlarmTime.TenSeconds -> {
                10000
            }
            AlarmTime.TenMinutes -> {
                600000
            }
            AlarmTime.FiveMinutes -> {
                300000
            }
            AlarmTime.OneHour -> {
                3600000
            }
            AlarmTime.ThirtyMinutes -> {
                1800000
            }
            AlarmTime.TwentyMinutes -> {
                1200000
            }

        }
    }



}