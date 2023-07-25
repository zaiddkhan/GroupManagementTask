package com.webview.groupmanagementtask.feature.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.webview.groupmanagementtask.R
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.AlarmTime
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupListScreenUiState
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.ReminderTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAlarmDialog(
    groupListScreenUiState : GroupListScreenUiState,
    onEvent : (GroupScreenEvent) -> Unit,
) {
    var dialogExpanded by remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = {
        onEvent(GroupScreenEvent.HideSetAlarmDialog)
    }) {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                modifier = Modifier.padding(20.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(200.dp),
                        painter = painterResource(id = R.drawable.alarm),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Notify you after every  : ",style = MaterialTheme.typography.bodyLarge)
                    Box {
                        OutlinedTextField(
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        dialogExpanded = !dialogExpanded
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = ""
                                    )
                                }
                            },
                            value = groupListScreenUiState.reminderAlarmTime.value,
                            onValueChange = {

                            }
                        )
                        DropdownMenu(
                            expanded = dialogExpanded,
                            onDismissRequest = {
                                dialogExpanded = false
                            }) {
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.SetReminderAlarmTime(ReminderTime.FifteenMinutes))
                                dialogExpanded = false

                            }) {
                                Text(text = ReminderTime.FifteenMinutes.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.SetReminderAlarmTime(ReminderTime.ThirtyMinutes))
                                dialogExpanded = false

                            }) {
                                Text(text = ReminderTime.ThirtyMinutes.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.SetReminderAlarmTime(ReminderTime.OneHour))
                                dialogExpanded = false


                            }) {
                                Text(text = ReminderTime.OneHour.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.SetReminderAlarmTime(ReminderTime.TwoHours))
                                dialogExpanded = false


                            }) {
                                Text(text = ReminderTime.TwoHours.value)

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            ,
                        onClick = {
                        onEvent(GroupScreenEvent.SetReminder)
                    }) {
                        Text(text = "Schedule the reminder")
                        
                    }
                }
            }
        }
    }
}