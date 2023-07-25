package com.webview.groupmanagementtask.feature.domain.use_case

import com.webview.groupmanagementtask.feature.domain.models.RegisterUserValidationType

class ValidateRegisterUserUseCase {

    operator fun invoke(userName : String,email:String) : RegisterUserValidationType{

        if(email.isEmpty() || userName.isEmpty()){
            return RegisterUserValidationType.EmptyField
        }
        if("@" !in email){
            return RegisterUserValidationType.NoEmail
        }
        return RegisterUserValidationType.Valid

    }
}