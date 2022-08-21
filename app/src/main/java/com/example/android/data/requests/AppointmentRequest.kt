package com.example.android.data.requests

data class AppointmentRequest (
    val date : String,
    val timeSlotId : Int,
    val isOffer: Boolean
        )