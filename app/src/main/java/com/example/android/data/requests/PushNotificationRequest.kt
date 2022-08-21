package com.example.android.data.requests

data class PushNotificationRequest (
                val title : String,
                val message : String,
                val phoneToken: List<String>?
        )