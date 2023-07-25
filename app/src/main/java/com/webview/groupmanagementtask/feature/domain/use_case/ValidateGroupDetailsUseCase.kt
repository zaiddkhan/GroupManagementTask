package com.webview.groupmanagementtask.feature.domain.use_case

class ValidateGroupDetailsUseCase {

    operator fun invoke(groupName : String) : GroupValidationType{
        return if(groupName.isEmpty()){
            GroupValidationType.INVALID
        }else{
            GroupValidationType.VALID
        }
    }
}

enum class GroupValidationType{
    VALID,INVALID
}