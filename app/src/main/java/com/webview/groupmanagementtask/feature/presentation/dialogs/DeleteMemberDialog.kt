package com.webview.groupmanagementtask.feature.presentation.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailEvent
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.states.GroupDetailUiState

@Composable
fun DeleteMemberDialog(
    username : String,
    uiState: GroupDetailUiState,
    onEvent: (GroupDetailEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onEvent(GroupDetailEvent.HideAddMemberDialog)
        }) {

        Card(
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White,
            modifier = Modifier.padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Do you really want to delete the user $username?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row(
                        verticalAlignment= Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 10.dp)
                    ){
                        Text(
                            text = "CANCEL",
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                onEvent(GroupDetailEvent.HideDeleteMemberDialog)
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            onClick = {
                                onEvent(GroupDetailEvent.DeleteMember(userDto = uiState.userToDelete ?: UserDto("","","")))
                            }) {
                            Text(text = "DELETE",color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }

            }
        }

    }

}