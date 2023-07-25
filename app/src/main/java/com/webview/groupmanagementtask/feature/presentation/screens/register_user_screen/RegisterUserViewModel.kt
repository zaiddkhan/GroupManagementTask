package com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.feature.domain.models.RegisterUserValidationType
import com.webview.groupmanagementtask.feature.domain.repository.UserDataStore
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateRegisterUserUseCase
import com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen.state.RegisterUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
  private val registerUserUserCase : ValidateRegisterUserUseCase,
  private  val userDataStore: UserDataStore
) : ViewModel() {

    private val _registerUserUiState = MutableStateFlow(RegisterUserUiState())
    val registerUserUiState : StateFlow<RegisterUserUiState> = _registerUserUiState.asStateFlow()



    fun onNameChanged(name : String){
        _registerUserUiState.update {
            it.copy(
                userName = name
            )
        }
        val validate = registerUserUserCase(_registerUserUiState.value.userName,_registerUserUiState.value.email)

        when(validate){
            RegisterUserValidationType.Valid -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = false
                    )
                }
            }
            RegisterUserValidationType.NoEmail -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = true
                    )
                }
            }
            RegisterUserValidationType.EmptyField -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = true
                    )
                }
            }
        }
    }

    fun emailChanged(email : String){
        _registerUserUiState.update {
            it.copy(
                email = email
            )
        }
        val validate = registerUserUserCase(_registerUserUiState.value.userName,_registerUserUiState.value.email)

        when(validate){
            RegisterUserValidationType.Valid -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = false
                    )
                }
            }
            RegisterUserValidationType.NoEmail -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = true
                    )
                }
            }
            RegisterUserValidationType.EmptyField -> {
                _registerUserUiState.update {
                    it.copy(
                        isEmailInvalid = true
                    )
                }
            }
        }


    }

    fun onImageSelected(byteArray : ByteArray){
        _registerUserUiState.update {
            it.copy(
                selectedPhotoUri = byteArray
            )
        }
    }

    fun onPhoneNumberChanged(phoneNumber : String){
        _registerUserUiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun onRegister(){

        _registerUserUiState.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            async {  userDataStore.setUserRegistered(true) }.await()
            async {  userDataStore.setName(_registerUserUiState.value.userName)}.await()
            async {  userDataStore.
            insertUser(
                UserDto(
                    name = _registerUserUiState.value.userName,
                    email = _registerUserUiState.value.email,
                    phoneNumber = _registerUserUiState.value.phoneNumber))
            }.await()

        }
        _registerUserUiState.update {
            it.copy(
                isLoading = false
            )
        }
    }

}