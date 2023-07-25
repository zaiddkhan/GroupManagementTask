package com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webview.groupmanagementtask.feature.data.entities.GroupDto
import com.webview.groupmanagementtask.feature.presentation.components.TextViewReusable
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent
import kotlin.random.Random
import kotlin.random.nextInt
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.webview.groupmanagementtask.R
import com.webview.groupmanagementtask.core.utils.lerp
import com.webview.groupmanagementtask.feature.presentation.components.GroupPhoto
import com.webview.groupmanagementtask.feature.presentation.components.ImagePicker
import com.webview.groupmanagementtask.feature.presentation.dialogs.SendMessageDialog
import com.webview.groupmanagementtask.feature.presentation.dialogs.SetAlarmDialog
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupListScreenUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private val FabSize = 100.dp
private const val ExpandedSheetAlpha = 0.96f

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    groupListScreenUiState: GroupListScreenUiState,
    groups : List<GroupDto>,
    modifier : Modifier = Modifier,
    onEvent: (GroupScreenEvent) -> Unit,
    imagePicker: ImagePicker,
    snackMessage : SharedFlow<String>,
    navigateToChat : (String) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = snackMessage){
        snackMessage.collect{
            snackbarHostState.showSnackbar(it)
        }

    }

    if(groupListScreenUiState.isSetAlarmDialogShown){
        SetAlarmDialog(
            groupListScreenUiState = groupListScreenUiState,
            onEvent = onEvent
        )
    }
    if(groupListScreenUiState.isSendMessageDialogShown){
        SendMessageDialog(
            groupListScreenUiState = groupListScreenUiState,
            onEvent = onEvent,
            groups = groups
        )
    }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState)},
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    backgroundColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = 4.dp
                ) {
                    Text(
                        text = "Manage chats",
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                onEvent(GroupScreenEvent.ShowSetAlarmDialog)
                            },
                        painter = painterResource(id = R.drawable.reminder),
                        contentDescription = "",

                    )
                    IconButton(
                        onClick = { onEvent(GroupScreenEvent.ShowSendMessageDialog)},
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        ) {
            BoxWithConstraints(
                modifier = Modifier.padding(it)
            ) {
                val sheetState = rememberSwipeableState(SheetState.Closed)
                val fabSize = with(LocalDensity.current) { FabSize.toPx() }
                val dragRange = constraints.maxHeight - fabSize
                val scope = rememberCoroutineScope()

                BackHandler(
                    enabled = sheetState.currentValue == SheetState.Open,
                    onBack = {
                        scope.launch {
                            sheetState.animateTo(SheetState.Closed)
                        }
                    }
                )

                Box(
                    Modifier
                        .swipeable(
                            state = sheetState,
                            anchors = mapOf(
                                0f to SheetState.Closed,
                                -dragRange to SheetState.Open
                            ),
                            thresholds = { _, _ -> FractionalThreshold(0.5f) },
                            orientation = Orientation.Vertical
                        )
                        .statusBarsPadding()
                ) {
                    val openFraction = if (sheetState.offset.value.isNaN()) {
                        0f
                    } else {
                        -sheetState.offset.value / dragRange
                    }.coerceIn(0f, 1f)

                    GroupsList(
                        groups = groups,
                        navigateToChat
                    )

                    CreateGroupFormSheet(
                        groupListScreenUiState = groupListScreenUiState,
                        onEvent = onEvent,
                        openFraction = openFraction,
                        width = this@BoxWithConstraints.constraints.maxWidth.toFloat(),
                        height = this@BoxWithConstraints.constraints.maxHeight.toFloat(),
                        updateSheet = { state ->
                            scope.launch {
                                sheetState.animateTo(state)
                            }
                        },
                        imagePicker = imagePicker
                    )
                }
            }

        }


}


@Composable
fun GroupsList(
    groups : List<GroupDto>,
    navigateToChat : (String) -> Unit
) {
    if(groups.isNotEmpty()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                columns = GridCells.Adaptive(120.dp),
            ) {
                item { 
                    Text(text = "Chat Room", style = MaterialTheme.typography.displayMedium)
                }
                items(groups.size) {
                    GroupDisplay(
                        groups[it],
                        navigateToChat
                    )
                }
            }
        }
    }else{
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn{
                item {
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp),
                        text = "Wow so empty here :(",
                        fontSize = 32.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

}


@Composable
fun GroupDisplay(
    group: GroupDto,
    navigateToChat : (String) -> Unit
) {
    val groupColor = remember {
        Color(
            Random.nextInt(0..255),
            Random.nextInt(0..255),
            Random.nextInt(0..255)
        )
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .width(120.dp)
            .height(200.dp)
            .background(groupColor)
            .clickable {
                       navigateToChat(group.groupName)
            }
        ,
        contentAlignment = Alignment.BottomEnd
    ){
        Row(
            modifier = Modifier
                .background(Color.Black.copy(0.5f))
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        ) {
            Text(
                modifier = Modifier,
                text = group.groupName,
                fontSize = 13.sp,
                color = Color.White
            )
        }
    }

}
@Composable
private fun CreateGroupFormSheet(
    groupListScreenUiState: GroupListScreenUiState,
    onEvent : (GroupScreenEvent) -> Unit,
    openFraction: Float,
    width: Float,
    height: Float,
    updateSheet: (SheetState) -> Unit,
    imagePicker: ImagePicker
) {

    val fabSize = with(LocalDensity.current) { FabSize.toPx() }
    val fabSheetHeight = fabSize + WindowInsets.systemBars.getBottom(LocalDensity.current)
    val offsetX = lerp(width - fabSize, 0f, 0f, 0.15f, openFraction)
    val offsetY = lerp(height - fabSheetHeight, 0f, openFraction)
    val tlCorner = lerp(fabSize, 0f, 0f, 0.15f, openFraction)
    val surfaceColor = lerp(
        startColor = Color.Magenta,
        endColor = MaterialTheme.colorScheme.primaryContainer.copy(ExpandedSheetAlpha),
        startFraction = 0f,
        endFraction = 0.3f,
        fraction = openFraction
    )
    Surface(
        color = surfaceColor,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(topStart = tlCorner),
        modifier = Modifier.graphicsLayer {
            translationX = offsetX
            translationY = offsetY
        }
    ) {
        CreateGroupForm(
            onEvent = onEvent,
            openFraction = openFraction,
            surfaceColor = surfaceColor,
            updateSheet = updateSheet,
            groupListScreenUiState = groupListScreenUiState,
            imagePicker = imagePicker
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateGroupForm(
    groupListScreenUiState: GroupListScreenUiState,
    onEvent : (GroupScreenEvent) -> Unit,
    openFraction: Float,
    surfaceColor: Color ,
    updateSheet: (SheetState) -> Unit,
    imagePicker : ImagePicker
) {

    var selectedMembers by remember {
        mutableStateOf(0)
    }

    var dropDownExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val lessonsAlpha = lerp(0f, 1f, 0.2f, 0.8f, openFraction)


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = lessonsAlpha }
                .statusBarsPadding()
        ) {
            TopAppBar(
                backgroundColor = surfaceColor,
                elevation = 4.dp
            ){
                Text(
                    text = "Create a new group",
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { updateSheet(SheetState.Closed) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            GroupPhoto(
                imagePicker = imagePicker,
                onEvent = onEvent,
                groupListScreenUiState = groupListScreenUiState
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextViewReusable(
                name = groupListScreenUiState.newGroupName,
                onValueChange = {
                    onEvent(GroupScreenEvent.GroupNameChanged(it))
                },
                placeHolder = "Enter group name"
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextViewReusable(
                name = groupListScreenUiState.newGroupDesc,
                onValueChange = {
                    onEvent(GroupScreenEvent.GroupDescChanged(it))
                },
                placeHolder = "Enter group description"
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Select max number of group members",color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box {
                OutlinedTextField(

                    modifier = Modifier
                        .clickable{
                            dropDownExpanded = !dropDownExpanded
                        },
                    value = groupListScreenUiState.newGroupMaxMembers.toString(),
                    onValueChange = {
                        onEvent(GroupScreenEvent.GroupMaxMembersChanged(selectedMembers))
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                dropDownExpanded = !dropDownExpanded
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false
                    }
                ) {
                    DropdownMenuItem(onClick = {
                        onEvent(GroupScreenEvent.GroupMaxMembersChanged(15))
                        dropDownExpanded = !dropDownExpanded

                    }) {
                        Text(
                            text = "15"
                        )
                    }
                    DropdownMenuItem(onClick = {
                        onEvent(GroupScreenEvent.GroupMaxMembersChanged(20))
                        dropDownExpanded = !dropDownExpanded

                    }) {
                        Text(text = "20")
                    }
                    DropdownMenuItem(onClick = {
                        onEvent(GroupScreenEvent.GroupMaxMembersChanged(30))
                        dropDownExpanded = !dropDownExpanded

                    }) {
                        Text(text = "30")
                    }
                }

                }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(start = 10.dp, end = 10.dp),
                    enabled = !groupListScreenUiState.isError,
                    onClick = {
                        onEvent(GroupScreenEvent.GroupCreated)
                        updateSheet(SheetState.Closed)
                }) {
                    Text(text = "Create Group")
                }
            }
            }

        // When sheet closed, show the FAB
        val fabAlpha = lerp(1f, 0f, 0f, 0.15f, openFraction)
        Box(
            modifier = Modifier
                .size(FabSize)
                .padding(start = 16.dp, top = 8.dp) // visually center contents
                .graphicsLayer { alpha = fabAlpha }
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = { updateSheet(SheetState.Open) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    contentDescription = ""
                )
            }
        }

    }



    }



private enum class SheetState { Open, Closed }

