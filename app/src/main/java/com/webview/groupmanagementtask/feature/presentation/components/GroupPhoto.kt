package com.webview.groupmanagementtask.feature.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupListScreenUiState
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.state.GroupScreenEvent

@Composable
fun GroupPhoto(
    groupListScreenUiState: GroupListScreenUiState,
    imagePicker: ImagePicker,
    onEvent : (GroupScreenEvent) -> Unit
) {

    var bitmap : ImageBitmap? = null

    imagePicker.registerPicker(
        onImagePicked = { onEvent(GroupScreenEvent.GroupIconChanged(it))}
    )

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(40))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                imagePicker.pickImage()
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(40)
            ),
        contentAlignment = Alignment.Center
    ) {
        groupListScreenUiState.newGroupIconByteArray?.let {
            bitmap = rememberBitmapFromBytes(bytes = it)
        }
        if(bitmap != null){
            Image(
                bitmap = bitmap!!,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }else {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add photo",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}