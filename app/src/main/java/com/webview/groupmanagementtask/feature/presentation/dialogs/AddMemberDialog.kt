package com.webview.groupmanagementtask.feature.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.presentation.components.TextViewReusable
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailUiState


@Composable
fun AddMemberDialog(
    uiState: GroupDetailUiState,
    onEvent: (GroupDetailEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
        onEvent(GroupDetailEvent.HideAddMemberDialog)
    }) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White)
                //.clip(RoundedCornerShape(15.dp))
        ){
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Add new member details",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
                TextViewReusable(
                    name = uiState.newMemberName,
                    onValueChange = {
                        onEvent(GroupDetailEvent.AddMemberNameChanged(it))
                    },
                    placeHolder = "Enter name"
                )

                TextViewReusable(
                    name = uiState.newMemberPhoneNumber,
                    onValueChange = {
                        onEvent(GroupDetailEvent.AddMemberPhoneNumberChanged(it))
                    },
                    placeHolder = "Enter phone number"
                )

                TextViewReusable(
                    name = uiState.newMemberEmail,
                    onValueChange = {
                        onEvent(GroupDetailEvent.AddMemberEmailChanged(it))
                    },
                    placeHolder = "Enter email"
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                ){

                    Row(
                        verticalAlignment= Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 20.dp)
                    ){
                        Text(
                            text = "CANCEL",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                onEvent(GroupDetailEvent.HideAddMemberDialog)
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                onEvent(GroupDetailEvent.AddMember(UserDto(uiState.newMemberName,uiState.newMemberEmail,uiState.newMemberPhoneNumber)))
                            }) {
                            Text(text = "ADD",color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

            }
            Spacer(modifier = Modifier.height(15.dp))



        }

    }
}