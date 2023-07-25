package com.webview.groupmanagementtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.webview.groupmanagementtask.feature.presentation.components.ImagePicker
import com.webview.groupmanagementtask.ui.theme.GroupManagementTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)

        setContent {
                App(
                    imagePicker = ImagePicker(LocalContext.current as ComponentActivity),
                    finishActivity = { finish() },
                    LocalContext.current
                )
        }
    }
}
