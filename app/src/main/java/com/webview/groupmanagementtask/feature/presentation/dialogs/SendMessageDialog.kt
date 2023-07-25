package com.webview.groupmanagementtask.feature.presentation.dialogs

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.presentation.components.TextViewReusable
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.AlarmTime
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupListScreenUiState
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageDialog(
    groupListScreenUiState: GroupListScreenUiState,
    onEvent : (GroupScreenEvent) -> Unit,
    groups : List<GroupDto>
) {

    Dialog(onDismissRequest = {
        onEvent(GroupScreenEvent.HideSendMessageDialog)
    }) {
        var dialogExpanded by remember{
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onTertiary)
        ){
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Center)
            ) {
                Text(text = "Schedule your messages", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
                TextViewReusable(
                    modifier = Modifier.fillMaxWidth(),
                    name = groupListScreenUiState.message,
                    onValueChange = {
                        onEvent(GroupScreenEvent.MessageEntered(it))
                    },
                    placeHolder = "Type the message to send"
                )
                Spacer(modifier = Modifier.height(22.dp))
                Text(text = "Select groups to send message",style = MaterialTheme.typography.bodyLarge)

                GroupListSelection(
                    modifier = Modifier.height(300.dp),
                    groups = groups,
                    groupListScreenUiState,
                    onEvent
                )
                Spacer(modifier = Modifier.height(22.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Send the messages after",style = MaterialTheme.typography.bodyLarge)
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
                            value = groupListScreenUiState.messageSendTime.value,
                            onValueChange = {

                            }
                        )
                        DropdownMenu(
                            expanded = dialogExpanded,
                            onDismissRequest = {
                                dialogExpanded = false
                            }) {
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.TimeSelected(AlarmTime.TenSeconds))
                            }) {
                                Text(text = AlarmTime.TenSeconds.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.TimeSelected(AlarmTime.TenMinutes))

                            }) {
                                Text(text = AlarmTime.TenMinutes.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.TimeSelected(AlarmTime.TwentyMinutes))

                            }) {
                                Text(text = AlarmTime.TwentyMinutes.value)
                            }
                            DropdownMenuItem(onClick = {
                                onEvent(GroupScreenEvent.TimeSelected(AlarmTime.ThirtyMinutes))

                            }) {
                                Text(text = AlarmTime.ThirtyMinutes.value)

                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.height(22.dp))
                Box(modifier = Modifier
                    .fillMaxWidth())
                {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        Text(text = "CANCEL",color = MaterialTheme.colorScheme.primary,modifier = Modifier.clickable {
                            onEvent(GroupScreenEvent.HideSendMessageDialog)
                        })
                        Button(
                            onClick = {
                                onEvent(GroupScreenEvent.SendMessage)
                        },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.DarkGray
                            )
                        ) {
                            Text(text = "Schedule the message")
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun GroupListSelection(
    modifier: Modifier = Modifier,
    groups : List<GroupDto>,
    groupListScreenUiState: GroupListScreenUiState,
    onEvent : (GroupScreenEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.height(220.dp),
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ){
        items(groups.size){
            GroupListItem(group = groups[it], groupListScreenUiState,onEvent)
        }
    }

}

@Composable
private fun GroupListItem(
    group : GroupDto,
    groupListScreenUiState: GroupListScreenUiState,
    onEvent: (GroupScreenEvent) -> Unit
) {
    val select = remember {
        groupListScreenUiState.selectedGroups.contains(group)
    }
    val (selected, onSelected) = remember { mutableStateOf(false) }
    val topicChipTransitionState = topicChipTransition(selected)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(value = selected, onValueChange = {
                    onSelected(it)
                    onEvent(GroupScreenEvent.GroupSelected(group))
                })
        ) {
            Box {

                if (topicChipTransitionState.selectedAlpha>0) {
                    Surface(
                        color = Color.Yellow.copy(alpha = topicChipTransitionState.selectedAlpha),
                        modifier = Modifier.matchParentSize()
                    ){

                    }
                }
                Text(
                    modifier = Modifier.padding(start = 9.dp),
                    text = group.groupName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp))
    }
}

@Composable
private fun topicChipTransition(topicSelected: Boolean): SelectGroupTransition {
    val transition = updateTransition(
        targetState = if (topicSelected) SelectionState.Selected else SelectionState.Unselected
    )

    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0f
            SelectionState.Selected -> 0.8f
        }
    }
    val checkScale = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0.6f
            SelectionState.Selected -> 1f
        }
    }
    return remember(transition) {
        SelectGroupTransition(selectedAlpha, checkScale)
    }
}

private class SelectGroupTransition(
    selectedAlpha: State<Float>,
    checkScale: State<Float>
) {
    val selectedAlpha by selectedAlpha
    val checkScale by checkScale
}
private enum class SelectionState { Unselected, Selected }
