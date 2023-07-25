package com.webview.groupmanagementtask.feature.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

@Composable
fun UserPhoto(
    bitmap: ImageBitmap?,
    modifier : Modifier = Modifier,
    onClick : () -> Unit
) {

    val photoModifier = modifier.clip(RoundedCornerShape(35))

    if(bitmap != null) {
        Image(
            bitmap = bitmap,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = photoModifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                           onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "",
                modifier = Modifier.size(200.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}


