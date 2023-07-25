package com.webview.groupmanagementtask.feature.domain.use_case

import com.webview.groupmanagementtask.feature.data.entities.GroupDto

class ValidateMessageUseCase {
    operator fun invoke(message : String,list : List<GroupDto>) : Boolean {
        return message.isNotEmpty() && list.isNotEmpty()
    }
}

