package com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.webview.groupmanagementtask.feature.presentation.components.ImagePicker
import com.webview.groupmanagementtask.feature.presentation.components.TextViewReusable
import com.webview.groupmanagementtask.feature.presentation.components.UserPhoto

@Composable
fun RegisterUserScreen(
    bitmap: ImageBitmap?,
    imagePicker: ImagePicker,
    onPhotoPicked : (ByteArray) -> Unit,
    userName : String,
    email : String,
    onEmailChanged : (String) -> Unit,
    onUserNameChange : (String) -> Unit,
    onRegisterClicked : () -> Unit,
    isDetailValid : Boolean,
    isLoading : Boolean,
    phoneNumber : String,
    onPhoneNumberChanged : (String) -> Unit
) {

    imagePicker.registerPicker(onImagePicked = {
        onPhotoPicked(it)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(isLoading){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }else{
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()

            ) {
                Spacer(modifier = Modifier.height(50.dp))
                UserPhoto(
                    bitmap = bitmap,
                    onClick = {
                        imagePicker.pickImage()
                    }
                )

                TextViewReusable(
                    name = userName,
                    onValueChange = {
                        onUserNameChange(it)
                    },
                    placeHolder = "Enter your name"
                )

                TextViewReusable(
                    name = phoneNumber,
                    onValueChange = {
                        onPhoneNumberChanged(it)
                    },
                    placeHolder = "Enter your phone number"
                )

                TextViewReusable(
                    name = email,
                    onValueChange = {
                        onEmailChanged(it)
                    },
                    placeHolder = "Enter your email"
                )

                Button(
                    enabled = isDetailValid,
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    onClick = {
                        onRegisterClicked()
                    }) {
                    Text(text = "Register")
                }
            }
        }
}

}

