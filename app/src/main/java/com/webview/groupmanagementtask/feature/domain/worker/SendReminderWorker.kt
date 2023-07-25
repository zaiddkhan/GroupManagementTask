package com.webview.groupmanagementtask.feature.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.webview.groupmanagementtask.core.utils.NotificationHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class SendReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        NotificationHandler.showNotification(context = applicationContext)
        return Result.success()
    }
}