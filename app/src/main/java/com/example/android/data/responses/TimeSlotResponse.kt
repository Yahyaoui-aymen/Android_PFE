package com.example.android.data.responses

data class TimeSlotResponse(
    val `data`: List<TimeSlot>,
    val error: String?,
    val message: String?
)