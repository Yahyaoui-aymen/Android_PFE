package com.example.android.data.responses

data class GetNotificationResponse(
    val `data`: List<Notification>,
    val error: Any,
    val message: String
)