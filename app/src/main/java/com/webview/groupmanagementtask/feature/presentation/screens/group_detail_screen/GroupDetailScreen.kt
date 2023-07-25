package com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.presentation.dialogs.AddMemberDialog
import com.webview.groupmanagementtask.feature.presentation.dialogs.DeleteMemberDialog
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailUiState
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent

@Composable
fun GroupDetailScreen(
    groupName : String,
    onEvent: (GroupDetailEvent) -> Unit,
    uiState : GroupDetailUiState,
    members : List<UserDto>,
    loadDetails: (String) -> Unit
) {

    LaunchedEffect(Unit){
        loadDetails(groupName)
    }

    if(uiState.isAddMemberDialogShown){
        AddMemberDialog(
            uiState = uiState,
            onEvent = onEvent
        )
    }
    if(uiState.isDeleteMemberDialogShown){
        DeleteMemberDialog(
            username = uiState.userToDelete?.name ?: "",
            uiState = uiState,
            onEvent = onEvent
        )
    }
    if(uiState.isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp)
            )
        }
    }
    else {

        Surface(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            color = Color.LightGray
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 10.dp, start = 13.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = groupName,
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.Black
                        )
                        androidx.compose.material3.Text(
                            text = uiState.group?.groupDescription
                                ?: "Hello and welcome to our group",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 13.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(13.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Text(
                                text = "Group Members",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                            androidx.compose.material3.Text(
                                modifier = Modifier
                                    .clickable {
                                       onEvent(GroupDetailEvent.ShowAddMemberDialog)
                                    },
                                text = "+ Add Member",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        GroupList(
                            members = members,
                            event = onEvent
                        )
                    }
                }

            }
        }
    }
    }
    @Composable
    fun GroupMemberDisplay(
        userDto: UserDto,
        onEvent : (GroupDetailEvent) -> Unit
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = userDto.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = {

                }) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                               onEvent(GroupDetailEvent.ShowDeleteMemberDialog(userDto))
                            },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
            }
            Divider(modifier = Modifier.fillMaxWidth())
        }

    }

    @Composable
    fun GroupList(
        members : List<UserDto>,
        event: (GroupDetailEvent) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.height(220.dp),
            contentPadding = PaddingValues(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(members){
                GroupMemberDisplay(
                    onEvent = event,
                    userDto = it
                )
            }
        }
    }





