package com.example.android.data.repository

import com.example.android.data.network.NotificationApi

class NotificationRepository (
    private val api: NotificationApi
) : BaseRepository() {

    suspend fun getNotificationByUser() = safeApiCall {
        api.getNotificationByUser()
    }

}