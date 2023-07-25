package com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen.state


data class RegisterUserUiState(
    val userName : String = "",
    val email : String = "",
    val isEmailInvalid : Boolean = true,
    val isLoading : Boolean = false,
    val selectedPhotoUri : ByteArray? = null,
    val phoneNumber : String = ""
)




