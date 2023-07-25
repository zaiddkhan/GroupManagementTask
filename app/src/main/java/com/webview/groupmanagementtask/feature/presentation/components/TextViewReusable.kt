package com.webview.groupmanagementtask.feature.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextViewReusable(
    modifier : Modifier = Modifier,
    name : String,
    onValueChange : (String) -> Unit,
    placeHolder : String
) {

    TextField(
        modifier = modifier
            .fillMaxWidth(0.9f),
        value = name,
        onValueChange = {
        onValueChange(it)
    },
        placeholder = {
            Text(text = placeHolder)
        },
        shape = RoundedCornerShape(13.dp)
    )
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextViewReusablePreview(
    modifier : Modifier = Modifier,
    name : String = "dvk",
    onValueChange : (String) -> Unit = {},
    placeHolder : String = "dsklm"
) {

    TextField(
        modifier = modifier
            .fillMaxWidth(0.9f),
        value = name,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = placeHolder)
        },
        shape = RoundedCornerShape(13.dp)
    )
}