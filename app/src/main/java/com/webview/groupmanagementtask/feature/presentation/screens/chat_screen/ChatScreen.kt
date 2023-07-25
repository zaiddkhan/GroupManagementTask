package com.webview.groupmanagementtask.feature.presentation.screens.chat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states.ChatScreenEvent
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.states.ChatScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    username : String,
    onEvent : (ChatScreenEvent) -> Unit,
    groupName : String,
    loadDetails : (String) -> Unit,
    uiState : ChatScreenUiState,
    navigateToDetailScreen : (String) -> Unit
) {
    LaunchedEffect(Unit) {
        loadDetails(groupName)
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.clickable {
                        navigateToDetailScreen(groupName)
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    elevation = 4.dp
                ) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            "",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp, top = 10.dp)
                    ) {
                        Text(
                            color = Color.White,
                            text = groupName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            color = Color.White,
                            text = uiState.members?.joinToString(limit = 3) ?: "",
                            fontSize = 14.sp
                        )

                    }
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        reverseLayout = true
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                        if (uiState.listMessages != null) {
                            items(uiState.listMessages) { message ->

                                val isOwnMessage = message.from == username

                                Box(
                                    contentAlignment = if (isOwnMessage) {
                                        Alignment.CenterEnd
                                    } else Alignment.CenterStart,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .drawBehind {
                                                val cornerRadius = 10.dp.toPx()
                                                val triangleHeight = 20.dp.toPx()
                                                val triangleWidth = 25.dp.toPx()
                                                val trianglePath = Path().apply {
                                                    if (isOwnMessage) {
                                                        moveTo(
                                                            size.width,
                                                            size.height - cornerRadius
                                                        )
                                                        lineTo(
                                                            size.width,
                                                            size.height + triangleHeight
                                                        )
                                                        lineTo(
                                                            size.width - triangleWidth,
                                                            size.height - cornerRadius
                                                        )
                                                        close()
                                                    } else {
                                                        moveTo(0f, size.height - cornerRadius)
                                                        lineTo(0f, size.height + triangleHeight)
                                                        lineTo(
                                                            triangleWidth,
                                                            size.height - cornerRadius
                                                        )
                                                        close()
                                                    }
                                                }
                                                drawPath(
                                                    path = trianglePath,
                                                    color = if (isOwnMessage) Color.Green else Color.DarkGray
                                                )
                                            }
                                            .background(
                                                color = if (isOwnMessage) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.DarkGray,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = message.from,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = message.message,
                                            color = Color.White
                                        )

                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))

                            }
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().navigationBarsPadding().imePadding()
                    ) {
                        androidx.compose.material3.TextField(
                            value = uiState.currentlyTypedMessage,
                            onValueChange = {
                                onEvent(ChatScreenEvent.OnMessageTyped(it))
                            },
                            placeholder = {
                                Text(text = "Enter a message")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            onEvent(ChatScreenEvent.SendMessage(username))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send"
                            )
                        }

                    }

                }

            }
        }


    }
}