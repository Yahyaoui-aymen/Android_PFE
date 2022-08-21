package com.example.android.data.responses

data class Appointment(
    val client: User,
    val date: String,
    val id: Long,
    val status: Status,
    val timeSlot: TimeSlot,
    val isOffer : Boolean
)