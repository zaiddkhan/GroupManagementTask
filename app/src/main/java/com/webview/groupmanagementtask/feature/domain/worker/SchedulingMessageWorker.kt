package com.webview.groupmanagementtask.feature.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.webview.groupmanagementtask.feature.data.entities.MessageDto
import com.webview.groupmanagementtask.feature.data.entities.MessageGroupCrossRef
import com.webview.groupmanagementtask.feature.domain.repository.GroupListRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@HiltWorker
class SchedulingMessageWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted params : WorkerParameters,
    private val groupListRepository: GroupListRepository
) : CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        val message = inputData.getString("MESSAGE")
        val sender = inputData.getString("SENDER")
        val groups = inputData.getString("GROUPS")
        val groupArray = if(groups != null){
                groups.replace("\\s".toRegex(),"")
                groups.split(",").toTypedArray()
            }
            else emptyArray<String>()

        val time = inputData.getLong("TIME",1000)


        withContext(Dispatchers.IO){
           async {
               sendMessage(message ?: "",
                   sender ?: "",
                   groupArray.toList(),
                   groupListRepository,
                   time)
           }.await()
        }
       return Result.success()

    }
}

private suspend fun sendMessage(message : String,sender : String,groups : List<String>,groupListRepository: GroupListRepository,delay : Long){
    withContext(Dispatchers.IO){
        delay(delay)
        groupListRepository.insertMessage(MessageDto(message,sender))
        groups.forEach {
            groupListRepository.insertMessageGroupCrossRef(MessageGroupCrossRef(message,it.trim()))
        }
    }
}
