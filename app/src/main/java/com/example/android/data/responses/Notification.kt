package com.example.android.data.responses

import java.util.*

data class Notification(
    val id : Int,
    val date: Date,
    val message: String,
    val title: String,
    val user: User
)
