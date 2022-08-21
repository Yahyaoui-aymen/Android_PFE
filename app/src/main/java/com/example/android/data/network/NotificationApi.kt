package com.example.android.data.network

import com.example.android.data.responses.GetNotificationResponse
import com.example.android.data.responses.SignupResponse
import retrofit2.http.GET

interface NotificationApi {

    @GET("notification/getbyuser")
    suspend fun getNotificationByUser() : GetNotificationResponse

}